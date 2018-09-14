package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Map;
import java.util.Set;

/**
 *  Set类型操作接口，不直接使用Jedis的操作方法而另外定义了操作接口的原因是：
 *  在今后会使用责任链的模式和代理模式设计插件接口，使得用户能够扩展这些操作
 */
public interface SetHandle {

    /**
     * @Description: sortedset类型的插入操作
     * @Date 2018/9/12 15:36
     * @param transaction 使用的事务
     * @param key 插入sortedset的key
     * @param map 插入的值
     * @return
     */
    public void zadd(Transaction transaction, String key, Map<String,Double> map);

    /**
     * @Description: sortedset类型的获取操作
     * @Date 2018/9/12 15:36
     * @param transaction 使用的事务
     * @param key 插入set的key
     * @param value 插入的值
     * @return
     */
    public void sadd(Transaction transaction,String key,String... value);

    /**
     * @Description: set类型的获取操作
     * @Date 2018/9/12 14:51
     * @param jedis 使用的Jedis
     * @param key 获取的set的key
     * @return 返回一个保存了所有返回结果的set
     */
    public Set<String> smember(Jedis jedis, String key);

    /**
     * @Description: sortedset类型的获取操作
     * @Date 2018/9/12 14:51
     * @param jedis 使用的Jedis
     * @param key 获取的set的key
     * @return 返回一个保存了所有返回结果的set
     */
    public Set<String> zrange(Jedis jedis,String key);

}
