package RedisORM.executor.opItem;


import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 这个类对应着Redis中list
 * 保存了一个类中以list类型保存的字段
 */
public class ListItem implements Execute{

    // 保存的操作
    private OP saveop;

    // 获取的操作
    private OP getop;

    // 字段的set方法
    private Method setMethod;

    // 字段的get方法
    private Method getMethod;

    // 字段的名字
    private String fieldName;

    public ListItem(OP saveop, OP getop, Method setMethod, Method getMethod, String fieldName) {
        this.saveop = saveop;
        this.getop = getop;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.fieldName = fieldName;
    }

    @Override
    public void save(Transaction transaction, String id, Object t) {
        List<String> list = null;
        try {
            list = (List<String>) getMethod.invoke(t);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        saveop.op(transaction,id+"."+fieldName,list);
    }

    @Override
    public Object get(Jedis jedis,String id,Object t) {
        try {
            List<String> ans = (List) getop.op(jedis,id+"."+fieldName);
            if(ans==null) return null;
            setMethod.invoke(t,ans);
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

    @Override
    public String toString() {
        return "ListItem{" +
                "saveop=" + saveop +
                ", getop=" + getop +
                ", setMethod=" + setMethod +
                ", getMethod=" + getMethod +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}
