package RedisORM.builder;

import RedisORM.Configuration;
import RedisORM.executor.Execute;
import RedisORM.executor.exceptions.ErrorMethodException;
import RedisORM.executor.handle.DefaultHandle;
import RedisORM.executor.handle.Handle;
import RedisORM.executor.op.OPBuilderHelper;
import RedisORM.executor.opItem.*;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.maps.*;
import java.lang.reflect.Method;
import java.util.*;


public class BuilderItemMap {

    private Configuration configuration;

    private Handle handle;

    private Map hashItemMap;

    private Log log;

    /**
     * @Description: 构造一个ItemMap构造器
     * @Date 2018/9/11 22:24
     * @param configuration 使用的configuration
     */
    public BuilderItemMap(Configuration configuration) {
        this.configuration = configuration;
        handle = new DefaultHandle();
        hashItemMap = new HashMap<Class,HashItem>();
        configuration.setHashItemMap(hashItemMap);
        log = LogFactory.getLog(BuilderItemMap.class);
    }

    /**
     * @Description: 构建函数，负责整个的构建
     * @Date 2018/9/11 22:25
     * @return void
     */
    public void build() {
        Map<String,RHashMap> map = configuration.getHashMaps();
        Iterator<Map.Entry<String,RHashMap>> it = map.entrySet().iterator();
        // 根据RHashMap来构建HashItem
        while(it.hasNext()){
            Map.Entry<String,RHashMap> entry = it.next();
            String id = entry.getKey();
            RHashMap rHashMap = entry.getValue();
            HashItem hashItem = createHashItem(rHashMap);
            hashItemMap.put(rHashMap.getJavaType(),hashItem);
        }

        // 对每一个HashItem进行补充，主要是完成每个HashItem中的executes字段的填充
        // executes字段代表着所有内嵌类对应的执行器
        Iterator<Map.Entry<String,HashItem>> items = hashItemMap.entrySet().iterator();
        Map<Class,HashItem> itemMap = configuration.getHashItemMap();
        while(items.hasNext()){
            Map.Entry<String,HashItem> entry = items.next();
            HashItem hi= entry.getValue();
            if(!hi.isSerialize()){
                Map<String,Execute> executes = hi.getExecutes();
                List<String> itemproperty = hi.getHashproperty();
                List<Class> clazzs = hi.getHashItems();
                for (int i = 0; i < itemproperty.size(); i++) {
                    executes.put(itemproperty.get(i),itemMap.get(clazzs.get(i)));
                }
            }
        }
    }

    /**
     * @Description: 构造一个HashItem（除了HashItem中的executes字段初始化）
     * @Date 2018/9/11 22:27
     * @param rHashMap 构造HashItem所根据的RHashMap
     * @return HashItem 返回一个构造好的HashItem
     */
    private HashItem createHashItem(RHashMap rHashMap){
        if(log.isDebugEnabled()){
            log.debug("----building HashItem: "+rHashMap.getJavaType().getCanonicalName());
        }
        Class javaType = rHashMap.getJavaType();
        RKeyMap keymap = rHashMap.getKey();
        String property = keymap.getProperty();

        String idget = getorsetName(property,true);
        String idset = getorsetName(property,false);

        Method  idGetMethod = getorsetMethod(idget,javaType,null,true);
        Method  idSetMethod = getorsetMethod(idset,javaType,keymap.getJavaType(),false);

        HashItem hashItem = null;

        if(rHashMap.isSerialize()){
            hashItem = new HashItem(configuration,javaType,idGetMethod,idSetMethod,keymap.getJavaType(),true);
        }else{
            hashItem = new HashItem(configuration,javaType,idGetMethod,idSetMethod,keymap.getJavaType(),false);
            completeHashItem_StringItem(javaType,hashItem,rHashMap);
            completeHashItem_FieldItem(javaType,hashItem,rHashMap);
            completeHashItem_ListItem(javaType,hashItem,rHashMap);
            completeHashItem_SetItem(javaType,hashItem,rHashMap);
            completeHashItem_HashItem(javaType,hashItem,rHashMap);
            completeHashItem_SerializeItem(javaType,hashItem,rHashMap);
        }

        return hashItem;
    }

    /**
     * @Description: 完善HashItem中的每一个字段的值
     * @Date 2018/9/11 22:48
     * @param javaType  对应的类型
     * @param hashItem  对应的HashItem
     * @param rHashMap  所使用的RHashMap
     * @return void
     */
    private void completeHashItem_HashItem(Class javaType, HashItem hashItem, RHashMap rHashMap) {
        List<RHashMapRef> rHashMapRefs = rHashMap.getRHashMapRefs();
        Map<String,RHashMap> hashMaps = configuration.getHashMaps();
        // hashItem中的嵌套类集合
        List<Class> hashItems = hashItem.getHashItems();
        // hashItem中嵌套类的字段名集合
        List<String> itemproperty = hashItem.getHashproperty();
        for(int i=0;i<rHashMapRefs.size();i++){
            if(!rHashMapRefs.get(i).isSerialize()) {
                // 填充两个集合
                hashItems.add(hashMaps.get(rHashMapRefs.get(i).getId()).getJavaType());
                itemproperty.add(rHashMapRefs.get(i).getProperty());
                // 补充嵌套字段对应的set、get函数
                Method getMethod = getorsetMethod(getorsetName(rHashMapRefs.get(i).getProperty(), true), javaType, hashMaps.get(rHashMapRefs.get(i).getId()).getJavaType(), true);
                Method setMethod = getorsetMethod(getorsetName(rHashMapRefs.get(i).getProperty(), false), javaType, hashMaps.get(rHashMapRefs.get(i).getId()).getJavaType(), false);
                hashItem.getSetHashField().add(setMethod);
                hashItem.getGetHashField().add(getMethod);
            }
        }
    }


    /**
     * @Description: 构造一个SerializeItem
     * @Date 2018/9/11 22:40
     * @param javaType 对应的类型
     * @param rHashMapRef
     * @return RedisORM.executor.opItem.SerializeItem
     */
    private SerializeItem createSerializeItem(Class javaType, RHashMapRef rHashMapRef) {
        if(log.isDebugEnabled()){
            log.debug("------------creating SerializeItem: '"+rHashMapRef.getProperty()+"'");
        }
        String property = rHashMapRef.getProperty();
        Method getMethod = getorsetMethod(getorsetName(property,true),javaType,null,true);
        Method setMethod = getorsetMethod(getorsetName(property,false),javaType,rHashMapRef.getJavaType(),false);
        return new SerializeItem(OPBuilderHelper.hashSetByteOP(handle),OPBuilderHelper.hashGetByteOP(handle),setMethod,getMethod,rHashMapRef.getJavaType(),property);

    }

    /**
     * @Description: 填充SerializeItem的执行器到executes字段中
     * @Date 2018/9/11 22:39
     * @param javaType  此SerializeItem所对应的类型
     * @param hashItem  所属的HashItem
     * @param rHashMap  所根据的RHashMap
     * @return void
     */
    private void completeHashItem_SerializeItem(Class javaType, HashItem hashItem, RHashMap rHashMap) {
        if(log.isDebugEnabled()){
            log.debug("--------building SerializeItem");
        }
        List<RHashMapRef> rHashMapRefs = rHashMap.getRHashMapRefs();
        for(int i=0;i<rHashMapRefs.size();i++){
            if(rHashMapRefs.get(i).isSerialize()) {
                List<SerializeItem> serializeItems = hashItem.getSerializeItems();
                RHashMapRef rHashMapRef = rHashMapRefs.get(i);
                SerializeItem si = createSerializeItem(javaType,rHashMapRef);
                serializeItems.add(si);
                hashItem.getExecutes().put(rHashMapRefs.get(i).getProperty(),si);
            }
        }
    }

    /**
     * @Description: 构造一个FieldItem
     * @Date 2018/9/12 10:50
     * @param javaType  对应的类型
     * @param rFieldMap 所使用的RFieldMap
     * @return RedisORM.executor.opItem.FieldItem
     */
    private FieldItem createFieldItem(Class javaType, RFieldMap rFieldMap) {
        if(log.isDebugEnabled()){
            log.debug("------------building FieldItem: '"+rFieldMap.getProperty()+"'");
        }
        String property = rFieldMap.getProperty();
        Method getMethod = getorsetMethod(getorsetName(property,true),javaType,null,true);
        Method setMethod = getorsetMethod(getorsetName(property,false),javaType,rFieldMap.getJavaType(),false);
        return new FieldItem(OPBuilderHelper.hashSetOP(handle),OPBuilderHelper.hashGetOP(handle),setMethod,getMethod,property,rFieldMap.getJavaType());
    }

    /**
     * @Description: 填充FieldItem的执行器到executes字段中
     * @Date 2018/9/12 10:47
     * @param javaType 对应的类型
     * @param hashItem 所属的hashItem
     * @param rHashMap 构造所使用的rHashMap
     * @return void
     */
    private void completeHashItem_FieldItem(Class javaType, HashItem hashItem, RHashMap rHashMap) {
        if(log.isDebugEnabled()){
            log.debug("--------building FieldItem");
        }
        // 根据  rFieldMaps --> fieldItems
        List<RFieldMap> rFieldMaps = rHashMap.getRFieldMaps();
        List<FieldItem> fieldItems = hashItem.getFieldItems();
        for(int i=0;i<rFieldMaps.size();i++){
            RFieldMap rFieldMap = rFieldMaps.get(i);
            FieldItem fi = createFieldItem(javaType,rFieldMap);
            fieldItems.add(fi);
            hashItem.getExecutes().put(rFieldMap.getProperty(),fi);
        }
    }

    /**
     * @Description: 构造SetItem
     * @Date 2018/9/12 10:51
     * @param javaType 对应的类型
     * @param rSetMap 构造所使用的RSetMap
     * @return RedisORM.executor.opItem.SetItem
     */
    private SetItem createSetItem(Class javaType, RSetMap rSetMap) {
        if(log.isDebugEnabled()){
            log.debug("--------building SetItem: '"+rSetMap.getProperty()+"'");
        }
        String property = rSetMap.getProperty();
        Method getMethod = getorsetMethod(getorsetName(property,true),javaType,rSetMap.getJavaType(),true);
        Method setMethod = getorsetMethod(getorsetName(property,false),javaType,rSetMap.getJavaType(),false);
        return new SetItem(OPBuilderHelper.setSaddOP(handle),OPBuilderHelper.setSmemberOP(handle),setMethod,getMethod,property);
    }


    /**
     * @Description: 填充SetItem的执行器到executes字段中
     * @Date 2018/9/12 10:53
     * @param javaType 对应的类型
     * @param hashItem 所属的HashItem
     * @param rHashMap 所使用的RHashMap
     * @return void
     */
    private void completeHashItem_SetItem(Class javaType, HashItem hashItem, RHashMap rHashMap) {
        List<RSetMap> rSetMaps = rHashMap.getRSetMaps();
        List<SetItem> setItems = hashItem.getSetItems();
        List<SortedSetItem> sortedSetItems = hashItem.getSortedSetItems();
        for(int i=0;i<rSetMaps.size();i++){
            RSetMap rSetMap = rSetMaps.get(i);
            if(rSetMap.isSorted()){
                SortedSetItem ssi = createSortedSetItem(javaType,rSetMap);
                sortedSetItems.add(ssi);
                hashItem.getExecutes().put(rSetMap.getProperty(),ssi);
            }else{
                SetItem si = createSetItem(javaType,rSetMap);
                setItems.add(si);
                hashItem.getExecutes().put(rSetMap.getProperty(),si);
            }
        }
    }

    /**
     * @Description: 构造SortedSetItem
     * @Date 2018/9/12 10:51
     * @param javaType 对应的类型
     * @param rSetMap 构造所使用的RSetMap
     * @return SortedSetItem
     */
    private SortedSetItem createSortedSetItem(Class javaType, RSetMap rSetMap) {
        String property = rSetMap.getProperty();
        Method getMethod = getorsetMethod(getorsetName(property,true),javaType,rSetMap.getJavaType(),true);
        Method setMethod = getorsetMethod(getorsetName(property,false),javaType,rSetMap.getJavaType(),false);
        return new SortedSetItem(OPBuilderHelper.setZaddOP(handle),OPBuilderHelper.setZrangeOP(handle),setMethod,getMethod,property);
    }


    /**
     * @Description: 填充ListItem的执行器到executes字段中
     * @Date 2018/9/12 10:53
     * @param javaType 对应的类型
     * @param hashItem 所属的HashItem
     * @param rHashMap 所使用的RHashMap
     * @return void
     */
    private void completeHashItem_ListItem(Class javaType, HashItem hashItem, RHashMap rHashMap) {
        List<RListMap> rListMaps = rHashMap.getRListMaps();
        List<ListItem> listItems = hashItem.getListItems();
        for(int i=0;i<rListMaps.size();i++){
            RListMap rListMap = rListMaps.get(i);
            ListItem li = createListItem(javaType,rListMap);
            listItems.add(li);
            hashItem.getExecutes().put(rListMap.getProperty(),li);
        }
    }

    /**
     * @Description: 构造ListItem
     * @Date 2018/9/12 10:51
     * @param javaType 对应的类型
     * @param rListMap 构造所使用的RListMap
     * @return ListItem
     */
    private ListItem createListItem(Class javaType, RListMap rListMap) {
        if(log.isDebugEnabled()){
            log.debug("--------building ListItem: '"+rListMap.getProperty()+"'");
        }
        String property = rListMap.getProperty();
        Method getMethod = getorsetMethod(getorsetName(property,true),javaType,rListMap.getJavaType(),true);
        Method setMethod = getorsetMethod(getorsetName(property,false),javaType,rListMap.getJavaType(),false);
        return new ListItem(OPBuilderHelper.listPushOP(handle),OPBuilderHelper.listRangeOP(handle),setMethod,getMethod,property);
    }


    /**
     * @Description: 填充StringItem的执行器到executes字段中
     * @Date 2018/9/11 22:21
     * @param javaType 对应字段的类型
     * @param hashItem 所属的HashItem
     * @param rHashMap 构造所使用的RHashMap
     * @return void
     */
    private void completeHashItem_StringItem(Class javaType,HashItem hashItem,RHashMap rHashMap) {

        // 所有的RStringMap
        List<RStringMap> rStringMaps = rHashMap.getRStringMaps();
        // StringItem存放的地方
        List<StringItem> stringItems = hashItem.getStringItem();
        for(int i=0;i<rStringMaps.size();i++){
            RStringMap rStringMap = rStringMaps.get(i);
            // 创造一个StringItem
            StringItem si = createStringItem(javaType,rStringMap);
            // 添加到相应的list中
            stringItems.add(si);
            // 添加到executes中
            hashItem.getExecutes().put(rStringMap.getProperty(),si);
        }
    }

    /**
     * @Description: 构造StringItem
     * @Date 2018/9/12 10:51
     * @param javaType 对应的类型
     * @param rStringMap 构造所使用的RStringMap
     * @return StringItem
     */
    private StringItem createStringItem(Class javaType,RStringMap rStringMap) {
        if(log.isDebugEnabled()){
            log.debug("--------building StringItem: '"+rStringMap.getProperty()+"'");
        }
        String property = rStringMap.getProperty();
        Method getMethod = getorsetMethod(getorsetName(property,true),javaType,null,true);
        Method setMethod = getorsetMethod(getorsetName(property,false),javaType,rStringMap.getJavaType(),false);
        return new StringItem(OPBuilderHelper.stringSetOP(handle),OPBuilderHelper.stringGetOP(handle),setMethod,getMethod,property);
    }

    /**
     * @Description: 构造set或者get函数的名字
     * @Date 2018/9/12 11:00
     * @param name 字段的名字
     * @param get  是否是构造get函数
     * @return java.lang.String
     */
    private String getorsetName(String name,boolean get){
        if(get){
            return "get"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
        }else{
            return "set"+name.substring(0,1).toUpperCase()+name.substring(1,name.length());
        }
    }

    /**
     * @Description: 构造get或者set函数
     * @Date 2018/9/12 11:01
     * @param name  字段的名字
     * @param from  所属的类
     * @param field 字段对应的类型
     * @param get   是否是要构造get函数
     * @return java.lang.reflect.Method
     */
    private Method getorsetMethod(String name,Class from,Class field,boolean get){
        Method method;
        try {
            if(get){
                method = from.getMethod(name);
            }else{
                if(List.class.isAssignableFrom(field)){
                    field = List.class;
                }
                if(Set.class.isAssignableFrom(field)){
                    field = Set.class;
                }
                method = from.getMethod(name,field);
            }
        } catch (NoSuchMethodException e) {
            log.error("there is no method '"+name+"' in "+from.getClass());
            throw new ErrorMethodException("there is no method '"+name+"' in "+from.getClass());
        }
        return method;
    }


}
