package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import RedisORM.parse.exceptions.ErrorTypeException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractOP implements OP{


    protected Method method;

    protected Handle handle;

    public AbstractOP(Method method, Handle handle) {
        this.method = method;
        this.handle = handle;
    }

    @Override
    public Object op(Object object, Object... objects) {
        Object ans;
        if(object.getClass() == Jedis.class){
            ans = opreate((Jedis)object,objects);
        }else if(object.getClass()==Transaction.class){
            ans = opreate((Transaction)object,objects);
        }else {
            throw new ErrorTypeException("Except Jedis.class or Transaction.class but get "+object.getClass().getSimpleName()+".class");
        }
        return ans;
    }

    protected abstract Object opreate(Jedis jedis,Object... objects);

    protected abstract Object opreate(Transaction transaction,Object... objects);
}
