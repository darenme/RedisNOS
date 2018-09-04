package RedisORM.optest;

import RedisORM.executor.handle.DefaultHandle;
import RedisORM.executor.handle.Handle;
import RedisORM.executor.op.OP;
import RedisORM.executor.op.StringSetOP;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OPtest {

    String name = "methodtest";

    public String getName() {
        return name;
    }

    @Test
    public void test1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Jedis jedis = new Jedis("localhost", 6379);

        Handle handle = new DefaultHandle();

        Method method = DefaultHandle.class.getMethod("set",Jedis.class,String.class,String.class);

        OP op = new StringSetOP(method,handle);

        OPtest optest = new OPtest();

        Method me = OPtest.class.getMethod("getName");

        op.op(jedis,"nametest",me.invoke(optest));

        jedis.close();
    }

    @Test
    public void saveItemTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Jedis jedis = new Jedis("localhost", 6379);
        Handle handle = new DefaultHandle();
        Method method = DefaultHandle.class.getMethod("set",Jedis.class,String.class,String.class);
        OP op = new StringSetOP(method,handle);
//        FieldItem<OPtest> fieldItem = new FieldItem<OPtest>();

    }
}
