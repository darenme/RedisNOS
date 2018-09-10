package RedisORM.session;

import RedisORM.cache.Cache;

public interface SessionFactory {

    public RedisSession opSession();

    public boolean isReadonly();

    public Cache getCache();

}
