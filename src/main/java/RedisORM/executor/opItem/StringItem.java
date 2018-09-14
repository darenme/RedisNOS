package RedisORM.executor.opItem;

import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个类对应着Redis中string类型
 * 保存了一个类中以string类型保存的字段
 */
public class StringItem implements Execute{

    // 保存hash字段的操作方法
    private OP saveop;

    // 获取hash字段的操作方法
    private OP getop;

    // 字段的set函数
    private Method setMethod;

    // 字段的get函数
    private Method getMethod;

    // 字段的名字
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
