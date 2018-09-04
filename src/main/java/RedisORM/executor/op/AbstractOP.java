package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractOP implements OP{


    protected Method method;

    private Lock lock = new ReentrantLock();

    protected Handle handle;

    public AbstractOP(Method method, Handle handle) {
        this.method = method;
        this.handle = handle;
    }

    @Override
    public Object op(Jedis jedis, Object... objects) {
        Object ans=null;
        try {
            lock.lock();
            ans = opreate(jedis,objects);
        }finally {
            lock.unlock();
        }
        return ans;
    }

    protected abstract Object opreate(Jedis jedis,Object... objects);
}
