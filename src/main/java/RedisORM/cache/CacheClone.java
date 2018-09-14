package RedisORM.cache;
/**
 * 这个类是用来进行对象的序列化拷贝，也就是深拷贝
 */

import java.io.*;

public class CacheClone {

    /**
     * @Description: 拷贝并返回这个对象的一个拷贝
     * @Date 2018/9/12 12:10
     * @param value 需要被复制的对象
     * @return java.lang.Object
     */
    public static Object cloneObject(Object value){
        Object ans = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(value);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(in);
            ans = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ans;
    }
}
