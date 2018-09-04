package RedisORM.command;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

public class global {


    // 使用链接池进行链接
    @Test
    public void test1(){

        // 获得连接池的配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大链接数
        config.setMaxTotal(30);
        // 设置最大空闲连接数
        config.setMaxIdle(10);
        // 获得连接池
        JedisPool jedispool = new JedisPool(config,"localhost",6379);
        // 获得核心对象
        Jedis jedis = null;

        try {
            // 通过连接池获得链接
            jedis = jedispool.getResource();
            jedis.set("name2","mzw2");
            System.out.println(jedis.get("name2"));
        } catch (Exception e) {
            // TODO: handle exceptions
        }finally{
            if(jedis!=null){
                jedis.close();
            }
            if(jedispool!=null){
                jedispool.close();
            }
        }

    }

    @Test
    public void test_2(){
        Jedis jedis = new Jedis("localhost", 6379);

        jedis.set("name","mzw");

        System.out.println(jedis.get("name"));

        jedis.close();
    }

    @Test
    public void getAllKeys(){
        Jedis jedis = new Jedis("localhost", 6379);
        Set<String> keys = jedis.keys("*");
        for(String key:keys){
            System.out.println(key);
        }

    }

    @Test
    public void setSubKey(){
        Jedis jedis = new Jedis("localhost", 6379);

        jedis.set("id.signature","signature");

        System.out.println(jedis.get("id.signature"));

        jedis.close();
    }

    @Test
    public void setNxEx(){
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println(jedis.set("nxxx","1234","dd","ex",100));
        jedis.close();
    }

    @Test
    public void keysTest(){
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println(jedis.keys("hash.2"));
        jedis.close();
    }

    @Test
    public void typeTest(){
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println(jedis.type("hash.1"));
        jedis.close();
    }



}
