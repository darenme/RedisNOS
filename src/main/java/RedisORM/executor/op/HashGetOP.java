package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HashGetOP extends AbstractOP{

    public HashGetOP(Method method, Handle handle) {
        super(method, handle);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        String ans = null;
        try {
            ans = (String) method.invoke(handle,jedis,objects[0],objects[1]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ans;
    }
}
