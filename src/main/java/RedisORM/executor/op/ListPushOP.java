package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * list类型的插入操作
 */
public class ListPushOP extends AbstractOP{


    public ListPushOP(Method method, Handle handle) {
        super(method, handle);
    }

    // objects中的参数为key ,list
    @Override
    protected Object opreate(Transaction transaction, Object... objects) {
        List list = (List) objects[1];
        String[] values = new String[list.size()];
        for(int i=0;i<list.size();i++){
            values[i]= (String) list.get(i);
        }
        try {
            method.invoke(handle,transaction,objects[0],values);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        throw new WrongCallException("ListPushOP can't use Jedis!");
    }
}
