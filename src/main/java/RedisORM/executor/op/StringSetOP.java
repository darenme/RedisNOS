package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class StringSetOP extends AbstractOP{


    public StringSetOP(Method method, Handle handle) {
        super(method, handle);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        throw new WrongCallException("StringSetOP can't use Jedis!");
    }

    @Override
    protected Object opreate(Transaction transaction, Object... objects) {
        try {
            method.invoke(handle,transaction,objects[0],objects[1]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
