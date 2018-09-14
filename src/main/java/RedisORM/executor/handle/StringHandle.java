package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 *  string类型操作接口，不直接使用Jedis的操作方法而另外定义了操作接口的原因是：
 *  在今后会使用责任链的模式和代理模式设计插件接口，使得用户能够扩展这些操作
 */
public interface StringHandle {

    /**
     * @Description: string类型的插入操作
     * @Date 2018/9/12 15:41
     * @param transaction 使用的事务
     * @param key 插入string的key
     * @param value 插入的值
     * @return
     */
    public void set(Transaction transaction, String key, String value);

    /**
     * @Description: string类型的获取操作
     * @Date 2018/9/12 14:51
     * @param jedis 使用的Jedis
     * @param key 获取的string的key
     * @return 返回获得的结果
     */
    public String get(Jedis jedis, String key);

}
