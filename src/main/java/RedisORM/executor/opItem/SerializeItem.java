package RedisORM.executor.opItem;

import RedisORM.executor.Execute;
import RedisORM.executor.op.OP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SerializeItem implements Execute{

    // 保存hash字段的操作方法
    private OP saveop;

    // 获取hash字段的操作方法
    private OP getop;

    // 字段的set函数
    private Method setMethod;

    // 字段的get函数
    private Method getMethod;

    private Class javaType;

    private String property;

    public SerializeItem(OP saveop, OP getop, Method setMethod, Method getMethod, Class javaType, String property) {
        this.saveop = saveop;
        this.getop = getop;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
        this.javaType = javaType;
        this.property = property;
    }

    @Override
    public void save(Transaction transaction, String key, Object t) {

        byte[] keybyte = key.getBytes();
        byte[] fieldbyte = property.getBytes();


        try {
            Object save = getMethod.invoke(t);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(save);
            saveop.op(transaction,keybyte,fieldbyte,out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object get(Jedis jedis, String key, Object t) {
        byte[] keybyte = key.getBytes();
        byte[] fieldbyte = property.getBytes();

        Object value = null;
        try {
            byte[] ans = (byte[]) getop.op(jedis,keybyte,fieldbyte);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(ans));
            value = ois.readObject();
            setMethod.invoke(t,value);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getProperty() {
        return property;
    }
}
