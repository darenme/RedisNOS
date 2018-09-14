package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import RedisORM.parse.exceptions.ErrorTypeException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 这个抽象类实现了数据加工的大致流程，子类只要实现具体的加工过程就可以了
 */
public abstract class AbstractOP implements OP{

    // 数据操作使用的方法
    protected Method method;

    // 方法属于的Handle实现类
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

    /**
     * @Description: 使用Jedis来操作，主要用于获取
     * @Date 2018/9/12 16:17
     * @param jedis 使用的jedis对象
     * @param objects 数据操作使用到的值
     * @return
     */
    protected abstract Object opreate(Jedis jedis,Object... objects);

    /**
     * @Description: 使用Jedis来操作，主要用于插入数据
     * @Date 2018/9/12 16:17
     * @param transaction 使用的Transaction
     * @param objects 数据操作使用到的值
     * @return
     */
    protected abstract Object opreate(Transaction transaction,Object... objects);
}
