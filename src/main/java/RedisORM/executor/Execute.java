package RedisORM.executor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Set;

/**
 * 执行器接口，使用执行器的好处是：不需要每次都解析对应的类，使用执行器就可以直接操作，因为执行器中已经保存了相关操作方法。
 */
public interface Execute {

    /**
     * @Description: 保存数据
     * @Date 2018/9/12 13:49
     * @param transaction 使用的事务
     * @param id 保存的key或者id
     * @param t 被保存的对象
     * @return
     */
    public void save(Transaction transaction, String id, Object t);

    /**
     * @Description: 获取数据
     * @Date 2018/9/12 13:50
     * @param jedis 使用的Jedis对象
     * @param id 数据的id
     * @param t 数据属于的对象
     * @return
     */
    public Object get(Jedis jedis,String id,Object t);

    /**
     * @Description: 获取执行器对应的字段名（如果执行器的实现类是HashItem则无用）
     * @Date 2018/9/12 13:54
     * @return 返回对应的字段名
     */
    public String getProperty();


}
