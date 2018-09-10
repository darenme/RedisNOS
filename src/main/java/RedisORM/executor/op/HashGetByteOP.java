package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HashGetByteOP extends AbstractOP{

    private Log log;

    public HashGetByteOP(Method method, Handle handle) {
        super(method, handle);
        log = LogFactory.getLog(HashSetOP.class);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {

        Object ans = null;
        try {
            ans = method.invoke(handle,jedis,objects[0],objects[1]);
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
