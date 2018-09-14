package RedisORM.executor.opItem;

import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 这个类对应着Redis中set类型
 * 保存了一个类中以set类型保存的字段
 */
public class SetItem implements Execute{

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
