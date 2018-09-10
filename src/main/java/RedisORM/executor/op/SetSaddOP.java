package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class SetSaddOP extends AbstractOP{

    public SetSaddOP(Method method, Handle handle) {
        super(method, handle);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        throw new WrongCallException("StringSetOP can't use Jedis!");
    }

    @Override
    protected Object opreate(Transaction transaction, Object... objects) {
        String[] set = (String[]) objects[1];
        try {
            method.invoke(handle,transaction,objects[0],set);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
