package RedisORM.cache.decorators;

import RedisORM.cache.Cache;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 这个是最基础的缓存
public class BaseCache implements Cache {

    // 使用HashMap来保存缓存
    private Map<Object, Object> cache = new ConcurrentHashMap<>();

    private String id;

    public BaseCache(String id) {
        this.id = id;
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

}
