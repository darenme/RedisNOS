package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * hash类型的插入操作
 */
public class HashSetOP extends AbstractOP{

    private Log log;

    public HashSetOP(Method method, Handle handle) {

        super(method, handle);
        log = LogFactory.getLog(HashSetOP.class);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        throw new WrongCallException("HashSetOP can't use Jedis!");

    }

    @Override
    protected Object opreate(Transaction transaction, Object... objects) {
        try {
            method.invoke(handle,transaction,objects[0],objects[1],objects[2]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
