package RedisORM.session;

import RedisORM.Configuration;
import RedisORM.cache.Cache;
import RedisORM.cache.CacheSetting;
import RedisORM.cache.decorators.*;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;

public class DefaultSessionFactory implements SessionFactory{

    Configuration configuration;

    Cache cache;

    Log log;

    boolean readonly;

    public DefaultSessionFactory(Configuration configuration) {
        this.configuration = configuration;
        log = LogFactory.getLog(DefaultSessionFactory.class);
        cache = buildCache(configuration.getCacheSetting());
        readonly = configuration.getCacheSetting().isReadOnly();
    }

    @Override
    public RedisSession opSession() {
        return new DefaultRedisSession(configuration,this);

    }

    private Cache buildCache(CacheSetting cs) {
        if(log.isDebugEnabled()){
            log.debug("--------start building Cache");
        }
        Cache base = new BaseCache(cs.getEviction()+" cache");
        Cache block = new BlockingCache(base);
        Cache eviction = null;
        if(cs.getEviction().equals("lru")){
            eviction = new LruCache(block,cs.getSize());
        }
        if(cs.getEviction().equals("fifo")) {
            eviction = new FifoCache(block, cs.getSize());
        }
        Cache login = new LoggingCache(eviction);
        Cache sheduled = new ScheduledCache(login,cs.getFlush());
        Cache soft = new SoftCache(sheduled);
        return soft;
    }

    @Override
    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

}
