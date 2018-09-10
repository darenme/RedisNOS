package RedisORM.executor.opItem;

import RedisORM.Configuration;
import RedisORM.cache.CacheKey;
import RedisORM.executor.Execute;
import RedisORM.executor.ItemBuilderAssist;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.ErrorTypeException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class HashItem implements Execute{

    private Configuration configuration;

    private Class javaType;

    // 是否使用序列化保存
    private boolean serialize;

    // 类中的字段
    private List<FieldItem> fieldItems;

    // 内嵌hash类,需要在执行时动态获取
    private List<Class> hashItems;

    // 获取id值的方法
    private Method idGetMethod;

    // 设置id值的方法
    private Method idSetMethod;

    private Class idType;

    // 获取内嵌类对象的方法
    private List<Method> getHashField;

    // 设置内嵌类对象的方法
    private List<Method> setHashField;

    // string字段
    private List<StringItem> stringItem;

    // set字段
    private List<SetItem> setItems;

    // sortedset字段
    private List<SortedSetItem> sortedSetItems;

    // list字段
    private List<ListItem> listItems;

    // serialize保存字段
    private List<SerializeItem> serializeItems;

    List<String> hashproperty;

    // 记录所有字段对应的执行器，用于延迟加载
    private Map<String,Execute> executes;

    private Log log;

    public HashItem(Configuration configuration,Class javaType, Method idGetMethod, Method idSetMethod, Class idType,boolean serialize) {
        this.configuration = configuration;
        this.javaType = javaType;
        this.idGetMethod = idGetMethod;
        this.idSetMethod = idSetMethod;
        this.idType = idType;
        this.serialize = serialize;
        log = LogFactory.getLog(HashItem.class);
        if(!serialize){
            fieldItems = new ArrayList<>();
            hashItems = new ArrayList<>();
            getHashField = new ArrayList<>();
            setHashField = new ArrayList<>();
            stringItem = new ArrayList<>();
            setItems = new ArrayList<>();
            sortedSetItems = new ArrayList<>();
            listItems = new ArrayList<>();
            hashproperty = new ArrayList<>();
            executes = new HashMap<>();
            serializeItems = new ArrayList<>();
        }
    }

    // key = Parent.ClassName$id
    // key = Parent.ClassName$serialize$id
    private String createKey(String parent,Object t,boolean serialize){
        // hash的key设置为 className$id
        if(parent==null){
            parent = "";
        }else{
            parent = parent+".";
        }
        String key = null;
        key = parent + javaType.getCanonicalName().toString()+"$";
        if(serialize){
            key = key + "serialize$";
        }
        key = key + getId(t);
        return key;
    }

    private String getId(Object object){
        try {
            return idGetMethod.invoke(object).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void save(Transaction transaction, String parent, Object t) {

        String key = createKey(parent,t,serialize);

        // 如果需要使用序列化，使用序列化保存
        if(serialize){
            saveAsSerialize(transaction, key, t);
            return;
        }else{
            saveAsHash(transaction,key,t);
            return;
        }

    }

    // 使用hash保存
    private void saveAsHash(Transaction transaction, String key, Object t) {

        // 保存id
        try {
            transaction.hset(key,"#{id}",idGetMethod.invoke(t).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 保存field字段
        for(FieldItem f:fieldItems){
            f.save(transaction,key,t);
        }
        String subIds = null;
        // 保存嵌套类
        for(int i=0;i<hashItems.size();i++){
            String subid = "";
            try {
                subid = configuration.getHashItemMap().get(hashItems.get(i)).getIdGetMethod().invoke(getHashField.get(i).invoke(t)).toString();
                configuration.getHashItemMap().get(hashItems.get(i)).save(transaction,key,getHashField.get(i).invoke(t));
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
            transaction.hset(key,"#{subIds}",subIds);
        }

        // 保存String
        for(StringItem si:stringItem){
            si.save(transaction,javaType.getCanonicalName()+"."+si.getProperty(),t);
        }
        // 保存Set
        for(SetItem si:setItems){
            si.save(transaction,key,t);
        }
        // 保存有序Set
        for(SortedSetItem ssi:sortedSetItems){
            ssi.save(transaction,key,t);
        }
        // 保存List
        for(ListItem li : listItems){
            li.save(transaction,key,t);
        }

        // 保存Serialize类型的数据
        for(int i=0;i<serializeItems.size();i++){
            serializeItems.get(i).save(transaction,key,t);
        }

    }

    private void saveAsSerialize(Transaction transaction, String key, Object t) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(t);
            transaction.set(key.getBytes(),out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean exist(Jedis jedis,Object ido){

        if(ido==null||ido.toString().equals("")) return false;

        String id = ido.toString();

        String key;

        if(serialize){
            key = javaType.getCanonicalName().toString()+"$serialize$"+id; //ClassName$serialize$id
        }else {
            key = javaType.getCanonicalName().toString()+"$"+id; //ClassName$id
        }

        if(jedis.keys(key).size()==0) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Object get(Jedis jedis,String parent,Object ido) {

        if(ido==null||ido.toString().equals("")) return null;

        String id = ido.toString();

        String key = null;

        if(serialize){
            key = javaType.getCanonicalName().toString()+"$serialize$"+id; //ClassName$serialize$id
        }else {
            key = javaType.getCanonicalName().toString()+"$"+id; //ClassName$id
        }

        if(parent!=null){
            key = parent +"."+ key;
        }

        // 如果是使用序列化方法保存
        if(serialize){
            return getAsSerialize(jedis,key);
        }else{
            return getAsHash(jedis,key);
        }

    }

    @Override
    public String getProperty() {
        return null;
    }

    private Object getAsHash(Jedis jedis, String key) {

        Object value = null;

        // 如果不存在，返回null
        if(jedis.keys(key).size()==0) {
            log.warn("There is no "+ key +" in Redis");
            return null;
        }

        // 如果类型不是hash，返回null
        if(!jedis.type(key).equals("hash")) {
            log.warn(key + " in Redis is not hash type");
            return null;
        }

        try {
            value = javaType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 获取id
        String idvalue = jedis.hget(key,"#{id}");
        Object realValue = ItemBuilderAssist.ChangeType(idType,idvalue);

        try {
            idSetMethod.invoke(value,realValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 获取子id
        String[] subIds=null;

        String subId = jedis.hget(key,"#{subIds}");
        if(subId!=null){
            subIds = subId.split("\\|");
        }

        // 获取嵌套类
        if(subIds!=null){
            for(int i=0;i<hashItems.size();i++){
                try {
                    setHashField.get(i).invoke(value, configuration.getHashItemMap().get(hashItems.get(i)).get(jedis,key,subIds[i]));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }


        for(FieldItem fi:fieldItems){
            fi.get(jedis,key,value);
        }

        for(StringItem si:stringItem){
            si.get(jedis,javaType.getCanonicalName()+"."+si.getProperty(),value);
        }

        for(SetItem si:setItems){
            si.get(jedis,key,value);
        }

        for(SortedSetItem ssi:sortedSetItems){
            ssi.get(jedis,key,value);
        }

        for(ListItem li : listItems){
            li.get(jedis,key,value);
        }

        for(SerializeItem si : serializeItems){
            si.get(jedis,key,value);
        }

        for(int i=0;i<serializeItems.size();i++){
             serializeItems.get(i).get(jedis,key,value);
        }
        return value;
    }

    // 获取并反序列化存入jedis的对象
    private Object getAsSerialize(Jedis jedis, String key) {
        Object value = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(jedis.get(key.getBytes())));
            value = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  value;
    }


    public void update(Transaction transaction,Set<String> property,Object object){
        try {
            String id = (String) idGetMethod.invoke(object);
            String key = object.getClass().getGenericSuperclass().getTypeName()+"$"+id;
            for(String s:property){
                Execute execute = executes.get(s);
                execute.save(transaction,key,object);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public CacheKey getCacheKey(Object object){
        return new CacheKey(javaType.getCanonicalName(),getId(object));
    }

    public List<String> getSubKeys(String parent,Object object){

        String keyns = parent==null?createKey(parent,object,false):parent+"."+createKey(null,object,false);

        List<String> list = new ArrayList<>();

        // 将当前对象在Redis中的key加入列表
        list.add(keyns);

        // 获取所有list的key
        for(ListItem li:listItems){
            list.add(keyns+"."+li.getProperty());
        }
        // 获取所有set的key
        for(SetItem si:setItems){
            list.add(keyns+"."+si.getProperty());
        }
        // 获取所有sortedset的key
        for(SortedSetItem ssi : sortedSetItems){
            list.add(keyns+"."+ssi.getProperty());
        }

        // 获取所有嵌套类的所有key
        for(int i=0;i<hashItems.size();i++){
            HashItem hi = configuration.getHashItemMap().get(hashItems.get(i));
            try {
                List<String> l = hi.getSubKeys(keyns,getHashField.get(i).invoke(object));
                for(String s:l){
                    list.add(s);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean delete(Transaction transaction,Object object){
        List<String> keys = getSubKeys(null,object);
        if(keys!=null){
            for(String key:keys){
                transaction.del(key);
            }
        }
        transaction.del(createKey(null,object,serialize));
        return true;
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

    public List<SerializeItem> getSerializeItems() {
        return serializeItems;
    }

    public boolean isSerialize() {
        return serialize;
    }

}
