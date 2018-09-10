package RedisORM.cache.decorators;

import RedisORM.cache.Cache;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;


/*
 * FIFO缓存，这个类就是维护一个FIFO链表
 * 这个策略与LRU类似，只不过getObject不算是使用了一次
 */
public class FifoCache implements Cache {


    private final Cache delegate;
    // 队列
    private Deque<Object> keyList;
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
