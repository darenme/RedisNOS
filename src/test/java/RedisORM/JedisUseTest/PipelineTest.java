package RedisORM.JedisUseTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/*
使用管道
优点：可以获得较快的速度,将与redis的交互减少至2次
缺点：不能立即获得数据。

 */

public class PipelineTest {

    // 435
    @Test
    public void Pipeline(){
        Jedis jedis = new Jedis("localhost", 6379);
        Pipeline p = jedis.pipelined();

        long t1 = System.currentTimeMillis();

        int i=0;
        while(i<99999){
            p.set("tt","vv");
//            p.get("tt");
            i++;
        }
        p.sync();
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
        jedis.close();
    }

    // 5635 ms
    @Test
    public void Normal(){
        Jedis jedis = new Jedis("localhost", 6379);

        long t1 = System.currentTimeMillis();

        int i=0;
        while(i<99999){
            jedis.set("tt","vv");
//            jedis.get("tt");
            i++;
        }

        long t2 = System.currentTimeMillis();

        System.out.println(t2-t1);
        jedis.close();
    }
}
