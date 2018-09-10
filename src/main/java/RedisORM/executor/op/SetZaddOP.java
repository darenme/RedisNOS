package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SetZaddOP extends AbstractOP{

    public SetZaddOP(Method method, Handle handle) {
        super(method, handle);
    }

    // key , set<String>
    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        throw new WrongCallException("StringSetOP can,t use Transaction!");

    }

    @Override
    protected Object opreate(Transaction transaction, Object... objects) {
        Set<String> set = (Set<String>) objects[1];
        Map<String,Double> map = new HashMap<>();
        double i = 1;
        for(String s:set){
            map.put(s,i++);
        }
        try {
            method.invoke(handle,transaction,objects[0],map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
