package RedisORM.executor.opItem;

import RedisORM.executor.Execute;
import RedisORM.executor.ItemBuilderAssist;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个类对应着Redis中hash类型的一个field
 * 保存了一个类中某个字段的一系列操作方法
 */
public class FieldItem implements Execute {

    // 保存hash字段的操作方法
    private OP saveop;

    // 获取hash字段的操作方法
    private OP getop;

    // 字段名
    private String fieldName;

    // 字段类型
    private Class fieldType;

    // 字段的set函数
    private Method setMethod;

    // 字段的get函数
    private Method getMethod;


    public FieldItem(OP saveop, OP getop,  Method setMethod, Method getMethod ,String fieldName, Class fieldType) {
        this.saveop = saveop;
        this.getop = getop;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
    }

    @Override
    public void save(Transaction transaction, String key, Object t) {

        try {
            saveop.op(transaction,key,fieldName,getMethod.invoke(t).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object get(Jedis jedis,String key,Object t) {
        try {
            String value = (String) getop.op(jedis,key,fieldName);
            if(value==null) return null;
            Object setValue = ItemBuilderAssist.ChangeType(fieldType,value);
            setMethod.invoke(t,setValue);
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
