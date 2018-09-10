package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class HashGetAllOP extends AbstractOP{

    public HashGetAllOP(Method method, Handle handle) {
        super(method, handle);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        Map<String,String> ans=null;
        try {
            ans = (Map<String, String>) method.invoke(handle,jedis,objects[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    protected Object opreate(Transaction transaction, Object... objects) {
        throw new WrongCallException("StringSetOP can't use Jedis!");
    }
}
