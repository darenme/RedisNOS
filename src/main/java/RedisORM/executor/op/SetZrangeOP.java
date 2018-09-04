package RedisORM.executor.op;

import RedisORM.executor.handle.Handle;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class SetZrangeOP extends AbstractOP{

    public SetZrangeOP(Method method, Handle handle) {
        super(method, handle);
    }

    @Override
    protected Object opreate(Jedis jedis, Object... objects) {
        Set<String> ans=null;
        try {
            ans = (Set) method.invoke(handle,jedis,objects[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ans;
    }
}
