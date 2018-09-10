package RedisORM.executor.opItem;

import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringItem implements Execute{

    private OP saveop;

    private OP getop;

    private Method setMethod;

    private Method getMethod;

    private String fieldName;

    public StringItem(OP saveop, OP getop,Method setMethod, Method getMethod,String fieldName) {
        this.saveop = saveop;
        this.getop = getop;
        this.fieldName = fieldName;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
    }

    @Override
    public void save(Transaction transaction, String id, Object t) {
        try {
            saveop.op(transaction,id,getMethod.invoke(t));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(Jedis jedis,String id,Object t) {
        try {
            String s = (String) getop.op(jedis,fieldName);
            if(s==null) return null;
            setMethod.invoke(t,s);
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
