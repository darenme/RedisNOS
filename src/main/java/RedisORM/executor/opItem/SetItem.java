package RedisORM.executor.opItem;

import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class SetItem implements Execute{

    private OP saveop;

    private OP getop;

    private Method setMethod;

    private Method getMethod;

    private String fieldName;

    public SetItem(OP saveop, OP getop, Method setMethod, Method getMethod, String fieldName) {
        this.saveop = saveop;
        this.getop = getop;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.fieldName = fieldName;
    }

    @Override
    public void save(Transaction transaction, String id, Object t) {
        try {
            Set<String> set = (Set<String>) getMethod.invoke(t);
            String[] ss = new String[0];
            ss = set.toArray(ss);
            saveop.op(transaction,id+"."+fieldName,ss);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(Jedis jedis,String id,Object t) {
        Set<String> set = (Set<String>) getop.op(jedis,id+"."+fieldName);
        if(set==null) return null;
        try {
            setMethod.invoke(t,set);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getProperty() {
        return fieldName;
    }

}
