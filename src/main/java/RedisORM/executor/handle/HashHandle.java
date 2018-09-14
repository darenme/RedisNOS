package RedisORM.executor.handle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Map;

/**
 *  hash类型操作接口，不直接使用Jedis的操作方法而另外定义了操作接口的原因是：
 *  在今后会使用责任链的模式和代理模式设计插件接口，使得用户能够扩展这些操作
 */
public interface HashHandle {

    /**
     * @Description: hash类型的插入操作
     * @Date 2018/9/12 14:11
     * @param transaction 使用的事务
     * @param key 对应hash类型的key
     * @param field 对应hash类型的field
     * @param value 需要保存的值             
     * @return
     */
    public void hset(Transaction transaction, String key, String field, String value);
    
    /**
     * @Description: hash类型的获取操作
     * @Date 2018/9/12 14:19
     * @param jedis 使用的Jedis
     * @param key 对应hash类型的key
     * @param field 对应hash类型的field
     * @return 获取到的值
     */
    public String hget(Jedis jedis,String key,String field);

    /**
     * @Description:
     * @Date 2018/9/12 14:19
     * @param jedis 使用的Jedis
     * @param key 对应hash类型的key
     * @return 获取到的值的一个Map
     */
    public Map<String,String> hgetAll(Jedis jedis,String key);

    /**
     * @Description: hash类型的获取操作,key和field都是byte类型
     * @Date 2018/9/12 14:19
     * @param jedis 使用的Jedis
     * @param key 对应hash类型的key
     * @param field 对应hash类型的field
     * @return 获取到的值
     */
    public byte[] hgetbyte(Jedis jedis,byte[] key,byte[] field);

    /**
     * @Description: hash类型的插入操作，key,field,value都是byte类型
     * @Date 2018/9/12 14:11
     * @param transaction 使用的事务
     * @param key 对应hash类型的key
     * @param field 对应hash类型的field
     * @param value 需要保存的值
     * @return
     */
    public void hsetbyte(Transaction transaction,byte[] key,byte[] field,byte[] value);

}
