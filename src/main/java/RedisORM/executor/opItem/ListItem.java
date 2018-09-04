package RedisORM.executor.opItem;


import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ListItem implements Execute{

    private OP saveop;

    private OP getop;

    private Method setMethod;

    private Method getMethod;

    private String fieldName;

    public ListItem(OP saveop, OP getop, Method setMethod, Method getMethod, String fieldName) {
        this.saveop = saveop;
        this.getop = getop;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.fieldName = fieldName;
    }

    @Override
    public void save(Jedis jedis,String id,Object t) {
        List<String> list = null;
        try {
            list = (List<String>) getMethod.invoke(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        saveop.op(jedis,fieldName,list);
    }

    @Override
    public Object get(Jedis jedis,String id,Object t) {
        try {
            List<String> ans = (List) getop.op(jedis,fieldName);
            if(ans==null) return null;
            setMethod.invoke(t,ans);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
