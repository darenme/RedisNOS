package RedisORM.cache.decorators;

import RedisORM.cache.Cache;
import RedisORM.cache.CacheException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 这个类实现了阻塞式获取缓存。
 *
 * 存在这样一种情况：两个线程同时查找缓存，都没有找到，那么它们都会从Redis中来加载，这样的重复的加载造成了资源的浪费
 * 所以，只需要一个加载即可，另一个要等待前一个线程获取到数据。
 *
 */

public class BlockingCache implements Cache {
    // 基础的cache操作
    private final Cache delegate;
    // 每个key对应的锁
    private final ConcurrentHashMap<Object, ReentrantLock> locks;
    // 锁超时时间
    private long timeout=1000;

    public BlockingCache(Cache delegate) {
        this.delegate = delegate;
        this.locks = new ConcurrentHashMap<Object, ReentrantLock>();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void putObject(Object key, Object value) {
        try {
            delegate.putObject(key, value);
        } finally {
            releaseLock(key);
        }
    }

    // 阻塞式获取缓存
    @Override
    public Object getObject(Object key) {
        // 先获取该key对应的锁
        acquireLock(key);
        // 获取缓存
        Object value = delegate.getObject(key);
        // 如果获取到了缓存，那么释放锁
        if (value != null) {
            releaseLock(key);
        }
        // 返回缓存
        return value;
    }

    /**
     * @Description: 移除某个缓存
     * @Date 2018/9/12 12:26
     * @param key 对应的key
     * @return java.lang.Object
     */
    @Override
    public Object removeObject(Object key) {
        return delegate.removeObject(key);
    }

    /**
     * @Description: 清除所有缓存
     * @Date 2018/9/12 12:28
     * @param
     * @return void
     */
    @Override
    public void clear() {
        delegate.clear();
    }


    /**
     * @Description: 获取key对应的锁，如果没有则创建一个
     * @Date 2018/9/12 12:28
     * @param key 对应的key
     * @return java.util.concurrent.locks.ReentrantLock 返回的锁
     */
    private ReentrantLock getLockForKey(Object key) {
        // 创建锁
        ReentrantLock lock = new ReentrantLock();
        // 尝试添加到locks集合中，如果已有了则使用已有的，如果没有则使用这个锁
        ReentrantLock previous = locks.putIfAbsent(key, lock);
        return previous == null ? lock : previous;
    }

    /**
     * @Description: 获取key对应的锁
     * @Date 2018/9/12 12:24
     * @param key 对应的key
     * @return void
     */
    private void acquireLock(Object key) {
        // 获取key对应的锁
        Lock lock = getLockForKey(key);
        if (timeout > 0) {
            try {
                boolean acquired = lock.tryLock(timeout, TimeUnit.MILLISECONDS);
                if (!acquired) {
                    throw new CacheException("Couldn't get a lock in " + timeout + " for the key " + key + " at the cache " + delegate.getId());
                }
            } catch (InterruptedException e) {
                throw new CacheException("Got interrupted while trying to acquire lock for key " + key, e);
            }
        } else {
            lock.lock();
        }
    }

    /**
     * @Description: 释放锁
     * @Date 2018/9/12 12:25
     * @param key 对应的key
     * @return void
     */
    private void releaseLock(Object key) {
        ReentrantLock lock = locks.get(key);
        if (lock!=null&&lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

}