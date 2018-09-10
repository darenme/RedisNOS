package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.Method;
import java.util.Map;

public class OPBuilderHelper {



    public static StringGetOP stringGetOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("get", Jedis.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new StringGetOP(method,handle);
    }

    public static StringSetOP stringSetOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("set", Transaction.class, String.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new StringSetOP(method,handle);
    }

    public static HashGetOP hashGetOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("hget", Jedis.class, String.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new HashGetOP(method,handle);
    }

    public static HashSetOP hashSetOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("hset", Transaction.class, String.class, String.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new HashSetOP(method,handle);
    }

    public static HashSetByteOP hashSetByteOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("hsetbyte", Transaction.class, byte[].class, byte[].class, byte[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new HashSetByteOP(method,handle);
    }

    public static HashGetByteOP hashGetByteOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("hgetbyte", Jedis.class, byte[].class, byte[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new HashGetByteOP(method,handle);
    }



    public static ListPushOP listPushOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("push", Transaction.class, String.class, String[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new ListPushOP(method,handle);
    }

    public static ListRangeOP listRangeOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("range", Jedis.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new ListRangeOP(method,handle);
    }

    public static SetSaddOP setSaddOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("sadd", Transaction.class, String.class, String[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new SetSaddOP(method,handle);
    }

    public static SetSmemberOP setSmemberOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("smember", Jedis.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new SetSmemberOP(method,handle);
    }

    public static SetZaddOP setZaddOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("zadd", Transaction.class, String.class, Map.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new SetZaddOP(method,handle);
    }

    public static SetZrangeOP setZrangeOP(Handle handle){
        Method method = null;
        try {
            method = handle.getClass().getMethod("zrange", Jedis.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new SetZrangeOP(method,handle);
    }






}
