package RedisORM.executor.opItem;

import RedisORM.Configuration;
import RedisORM.executor.Execute;
import RedisORM.executor.ItemBuilderAssist;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import redis.clients.jedis.Jedis;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class HashItem implements Execute{

    private Configuration configuration;

    private Class javaType;

    // 类中的字段
    private List<FieldItem> fieldItems = new ArrayList<>();

    // 内嵌hash类,需要在执行时动态获取
    private List<Class> hashItems = new ArrayList<>();

    // 获取id值的方法
    private Method idGetMethod;

    // 设置id值的方法
    private Method idSetMethod;

    private Class idType;

    // 获取内嵌类对象的方法
    private List<Method> getHashField = new ArrayList<>();

    // 设置内嵌类对象的方法
    private List<Method> setHashField = new ArrayList<>();

    // string字段
    private List<StringItem> stringItem = new ArrayList<>();

    // set字段
    private List<SetItem> setItems = new ArrayList<>();

    // sortedset字段
    private List<SortedSetItem> sortedSetItems = new ArrayList<>();

    // list字段
    private List<ListItem> listItems = new ArrayList<>();

    // 记录所有字段对应的执行器，用于延迟加载
    List<String> hashproperty = new ArrayList<>();
    private Map<String,Execute> executes = new HashMap<>();

    private Log log;


    public HashItem(Configuration configuration,Class javaType, Method idGetMethod, Method idSetMethod, Class idType) {
        this.configuration = configuration;
        this.javaType = javaType;
        this.idGetMethod = idGetMethod;
        this.idSetMethod = idSetMethod;
        this.idType = idType;
        log = LogFactory.getLog(HashItem.class);
    }

    private String createId(String id,Object t){
        // hash的key设置为 className$id
        if(id==null){
            id = "";
        }else{
            id = id+".";
        }
        try {
            id = id + javaType.getCanonicalName().toString()+"$"+idGetMethod.invoke(t).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void save(Jedis jedis, String id, Object t) {

        id = createId(id,t);

        // 保存id
        try {
            jedis.hset(id,"#{id}",idGetMethod.invoke(t).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 保存field字段
        for(FieldItem f:fieldItems){
                f.save(jedis,id,t);
        }
        String subIds = null;
        // 保存嵌套类
        for(int i=0;i<hashItems.size();i++){
            String subid = "";
            try {
                subid = configuration.getHashItemMap().get(hashItems.get(i)).getIdGetMethod().invoke(getHashField.get(i).invoke(t)).toString();
                configuration.getHashItemMap().get(hashItems.get(i)).save(jedis,id,getHashField.get(i).invoke(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(subIds==null){
                subIds = subid;
            }else{
                subIds += "|"+subid;
            }
        }

        if(subIds!=null){
            jedis.hset(id,"#{subIds}",subIds);
        }
        // 保存String
        for(StringItem si:stringItem){
            si.save(jedis,id,t);
        }
        // 保存Set
        for(SetItem si:setItems){
            si.save(jedis,id,t);
        }
        // 保存有序Set
        for(SortedSetItem ssi:sortedSetItems){
            ssi.save(jedis,id,t);
        }
        // 保存List
        for(ListItem li : listItems){
            li.save(jedis,id,t);
        }
    }


    @Override
    public Object get(Jedis jedis,String parent,Object o) {
        // hash的key设置为 className$id
        String id = o.toString();
        if(id==null||id.equals("")) return null;
        id = javaType.getCanonicalName().toString()+"$"+id;

        if(parent!=null){
            id = parent +"."+ id;
        }

        if(jedis.keys(id).size()==0) {
            log.warn("There is no "+id+" in Redis");
            return null;
        }

        if(!jedis.type(id).equals("hash")) {
            log.warn(id+" in Redis is not hash type");
            return null;
        }

        Object t = null;
        try {
            t = javaType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 获取id
        String idvalue = jedis.hget(id,"#{id}");
        Object realValue = ItemBuilderAssist.ChangeType(idType,idvalue);
        try {
            idSetMethod.invoke(t,realValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 获取子id
        String[] subIds=null;
        String subId = jedis.hget(id,"#{subIds}");
        if(subId!=null){
           subIds = subId.split("\\|");
        }

        // 获取嵌套类
        if(subIds!=null){
            for(int i=0;i<hashItems.size();i++){
                try {
//                    System.out.println(setHashField.size());
//                    System.out.println(hashItems.size());
//                    System.out.println(subIds.length);
                    setHashField.get(i).invoke(t, configuration.getHashItemMap().get(hashItems.get(i)).get(jedis,id,subIds[i]));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


        for(FieldItem fi:fieldItems){
            fi.get(jedis,id,t);
        }

        for(StringItem si:stringItem){
            si.get(jedis,id,t);
        }

        for(SetItem si:setItems){
            si.get(jedis,id,t);
        }

        for(SortedSetItem ssi:sortedSetItems){
            ssi.get(jedis,id,t);
        }

        for(ListItem li : listItems){
            li.get(jedis,id,t);
        }

        return t;
    }

    public void update(Set<String> property,Object object){
        try {
            String id = (String) idGetMethod.invoke(object);
            String key = object.getClass().getGenericSuperclass().getTypeName()+"$"+id;
            Jedis jedis = configuration.getDataSource().getJedis();
            for(String s:property){
                Execute execute = executes.get(s);
                execute.save(jedis,key,object);
            }
            jedis.close();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    public Class getJavaType() {
        return javaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    public List<FieldItem> getFieldItems() {
        return fieldItems;
    }

    public List<Class> getHashItems() {
        return hashItems;
    }

    public List<Method> getGetHashField() {
        return getHashField;
    }

    public List<Method> getSetHashField() {
        return setHashField;
    }

    public List<StringItem> getStringItem() {
        return stringItem;
    }

    public List<SetItem> getSetItems() {
        return setItems;
    }

    public List<SortedSetItem> getSortedSetItems() {
        return sortedSetItems;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public Method getIdGetMethod() {
        return idGetMethod;
    }

    public Map<String, Execute> getExecutes() {
        return executes;
    }

    public Method getIdSetMethod() {
        return idSetMethod;
    }

    public Class getIdType() {
        return idType;
    }

    public List<String> getHashproperty() {
        return hashproperty;
    }

}
