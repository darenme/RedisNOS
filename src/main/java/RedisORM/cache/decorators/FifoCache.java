package RedisORM.cache.decorators;

import RedisORM.cache.Cache;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;


/**
 * FIFO缓存，使用一个链表来保存缓存插入的顺序
 */
public class FifoCache implements Cache {


    private final Cache delegate;
    // 保存插入的顺序
    private Deque<Object> keyList;
    // 缓存的大小
    private int size;

    public FifoCache(Cache delegate, int size) {
        this.delegate = delegate;
        this.keyList = new LinkedList<Object>();
        this.size = size;
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void putObject(Object key, Object value) {
        cycleKeyList(key);
        delegate.putObject(key, value);
    }

    /**
     * @Description: 检查是否移除链头缓存
     * @Date 2018/9/12 13:00
     * @param key 加入的缓存
     * @return void
     */
    private void cycleKeyList(Object key) {
        keyList.addLast(key);
        if (keyList.size() > size) {
            Object oldestKey = keyList.removeFirst();
            delegate.removeObject(oldestKey);
        }
    }

    @Override
    public Object getObject(Object key) {
        return delegate.getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        delegate.clear();
        keyList.clear();
    }

}
