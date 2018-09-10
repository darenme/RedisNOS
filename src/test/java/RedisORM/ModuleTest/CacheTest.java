package RedisORM.ModuleTest;

import RedisORM.cache.Cache;
import RedisORM.cache.CacheKey;
import RedisORM.cache.decorators.*;
import RedisORM.useTest.Student;
import org.junit.Test;

public class CacheTest {


    @Test
    public void LruCacheTest(){
        Cache delegate = new BaseCache(this.getClass().getName());
        Cache lru = new LruCache(delegate,2);

        lru.putObject(1,1);
        lru.putObject(2,2);
        lru.putObject(3,3);

        System.out.println(lru.getSize());
        System.out.println(lru.getObject(1));

        lru.putObject(2,22);
        lru.putObject(4,4);

        System.out.println(lru.getSize());
        System.out.println(lru.getObject(3));
    }

    @Test
    public void FifoCacheTest(){
        Cache delegate = new BaseCache(this.getClass().getName());
        Cache fifo = new FifoCache(delegate,2);

        fifo.putObject(1,1);
        fifo.putObject(2,2);
        fifo.putObject(3,3);

        System.out.println(fifo.getSize());
        System.out.println(fifo.getObject(1));

        fifo.putObject(2,22);
        fifo.putObject(4,4);
        System.out.println(fifo.getSize());
        System.out.println(fifo.getObject(3));
    }

    @Test
    public void LogingCacheTest(){
        Cache delegate = new BaseCache(this.getClass().getName());
        Cache log = new LoggingCache(delegate);

        log.putObject(1,1);
        log.putObject(2,2);
        log.putObject(3,3);

        log.getObject(1);
        log.getObject(4);
        log.getObject(2);

    }


    @Test
    public void CacheTest(){
        Cache base = new BaseCache(this.getClass().getName());
        Cache lru = new LruCache(base,2);
        Cache block = new BlockingCache(lru);
        Cache login = new LoggingCache(block);
        Cache sheduled = new ScheduledCache(login,5*1000l);
        Cache cache = new SoftCache(sheduled);

        CacheKey k1 = new CacheKey("m1","n1");
        CacheKey k2 = new CacheKey("m2","n2");
        CacheKey k3 = new CacheKey("m3","n3");

        cache.getObject(k1);
        cache.putObject(k1,1);

        cache.getObject(k2);
        cache.putObject(k2,2);

        cache.getObject(k3);
        cache.putObject(k3,3);

        System.out.println(cache.getObject(k1));//null
        System.out.println(cache.getObject(k2));//2

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(cache.getObject(k2));//null
    }
}
