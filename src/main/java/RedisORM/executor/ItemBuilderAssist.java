package RedisORM.executor;

import RedisORM.executor.handle.Handle;
import RedisORM.executor.op.*;
import RedisORM.executor.opItem.*;
import RedisORM.parse.exceptions.ErrorTypeException;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.Map;

public class ItemBuilderAssist {

//    public static Method getMethod(Class clazz,String name,Class... classes){
//
//        try {
//            return clazz.getMethod(name,classes);
//        } catch (ErrorMethodException e) {
//            e.printStackTrace();
//        }
//    }
    private Handle handle = null;

    private OP stringSetOP = null;

    private OP stringGetOP = null;

    private OP fieldSetOP = null;

    private OP fieldGetOP = null;

    private OP listPushOP = null;

    private OP listRangeOP = null;

    private OP setZaddOP = null;

    private OP setZrangeOP = null;

    private OP setSaddOP = null;

    private OP setSmemberOP = null;

    private OP fieldSetByteOP = null;

    private OP fieldGetByteOP = null;

    public ItemBuilderAssist(Handle handle) {
        this.handle = handle;
        init();
    }


    public void init(){

        try {
            stringSetOP = new StringSetOP(handle.getClass().getMethod("set",Jedis.class,String.class,String.class),handle);

            stringGetOP = new StringGetOP(handle.getClass().getMethod("get",Jedis.class,String.class),handle);

            fieldSetOP = new HashSetOP(handle.getClass().getMethod("hset",Jedis.class,String.class,String.class,String.class),handle);

            fieldGetOP = new HashGetOP(handle.getClass().getMethod("hget",Jedis.class, String.class, String.class),handle);

            listPushOP = new ListPushOP(handle.getClass().getMethod("push", Jedis.class, String.class, String[].class),handle);

            listRangeOP = new ListRangeOP(handle.getClass().getMethod("range", Jedis.class, String.class),handle);

            setZaddOP = new SetZaddOP(handle.getClass().getMethod("zadd", Jedis.class, String.class, Map.class),handle);

            setZrangeOP = new SetZrangeOP(handle.getClass().getMethod("zrange", Jedis.class, String.class),handle);

            setSaddOP = new SetSaddOP(handle.getClass().getMethod("sadd", Jedis.class, String.class, String[].class),handle);

            setSmemberOP = new SetSmemberOP(handle.getClass().getMethod("smember", Jedis.class, String.class),handle);

            fieldGetByteOP = new HashGetByteOP(handle.getClass().getMethod("hgetbyte", Jedis.class, byte[].class, byte[].class),handle);

            fieldSetByteOP = new HashSetByteOP(handle.getClass().getMethod("hsetbyte", Jedis.class, byte[].class, byte[].class),handle);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Execute createExecute(Class item,Method setMethod,Method getMethod,String property,Class clazz){
        if(item == FieldItem.class){
            return new FieldItem(fieldSetOP,fieldGetOP,setMethod,getMethod,property,clazz);
        }
        if(item == StringItem.class){
            return new StringItem(stringSetOP,stringGetOP,setMethod,getMethod,property);
        }
        if(item == ListItem.class){
            return new ListItem(listPushOP,listRangeOP,setMethod,getMethod,property);
        }
        if(item == SetItem.class){
            return new SetItem(setSaddOP,setSmemberOP,setMethod,getMethod,property);
        }
        if(item == SortedSetItem.class){
            return new SortedSetItem(setZaddOP,setZrangeOP,setMethod,getMethod,property);
        }
        if(item == SerializeItem.class){
            return new SerializeItem(fieldSetByteOP,fieldGetByteOP,setMethod,getMethod,clazz,property);
        }
        return null;
    }

    public static Object ChangeType(Class fieldType,String value) {

        if(fieldType==null||fieldType==String.class) return value;
        if(fieldType==java.lang.Integer.class||fieldType==int.class){
            return Integer.parseInt(value);
        }
        if(fieldType==java.lang.Short.class||fieldType==short.class){
            return Short.parseShort(value);
        }
        if(fieldType==java.lang.Double.class||fieldType==double.class){
            return Double.parseDouble(value);
        }
        if(fieldType==java.lang.Float.class||fieldType==float.class){
            return Float.parseFloat(value);
        }
        if(fieldType==java.lang.Long.class||fieldType==long.class){
            return Long.parseLong(value);
        }
        if(fieldType==java.lang.Character.class||fieldType==char.class){
            return value.charAt(0);
        }
        throw new ErrorTypeException("Type is error");
    }

}
