package RedisORM.cache.decorators;

import RedisORM.cache.Cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Deque;
import java.util.LinkedList;


/**
 * 软引用缓存,核心是SoftReference
 * 目的是当value没用时使它失效
 */
public class SoftCache implements Cache {
    // 存放最近访问的缓存，防止被回收
    private final Deque<Object> hardLinksToAvoidGarbageCollection;
    // 被垃圾回收的引用队列
    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries;
    private final Cache delegate;
    private int numberOfHardLinks;

    public SoftCache(Cache delegate) {
        this.delegate = delegate;
        //默认链表可以存256元素
        this.numberOfHardLinks = 256;
        this.hardLinksToAvoidGarbageCollection = new LinkedList<Object>();
        this.queueOfGarbageCollectedEntries = new ReferenceQueue<Object>();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getSize() {
        removeGarbageCollectedItems();
        return delegate.getSize();
    }


    public void setSize(int size) {
        this.numberOfHardLinks = size;
    }

    @Override
    public void putObject(Object key, Object value) {
        removeGarbageCollectedItems();
        //putObject存了一个SoftReference，这样value没用时会自动垃圾回收
        delegate.putObject(key, new SoftEntry(key, value, queueOfGarbageCollectedEntries));
    }


    @Override
    public Object getObject(Object key) {
        Object result = null;

        SoftReference<Object> softReference = (SoftReference<Object>) delegate.getObject(key);
        if (softReference != null) {
            //核心调用SoftReference.get取得元素
            result = softReference.get();
            if (result == null) {
                delegate.removeObject(key);
            } else {
                synchronized (hardLinksToAvoidGarbageCollection) {
                    //存入经常访问的键值到链表(最多256元素),防止垃圾回收
                    hardLinksToAvoidGarbageCollection.addFirst(result);
                    if (hardLinksToAvoidGarbageCollection.size() > numberOfHardLinks) {
                        hardLinksToAvoidGarbageCollection.removeLast();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object removeObject(Object key) {
        removeGarbageCollectedItems();
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        synchronized (hardLinksToAvoidGarbageCollection) {
            hardLinksToAvoidGarbageCollection.clear();
        }
        removeGarbageCollectedItems();
        delegate.clear();
    }


    private void removeGarbageCollectedItems() {
        SoftEntry sv;
        //查看被垃圾回收的引用队列,然后调用removeObject移除他们
        while ((sv = (SoftEntry) queueOfGarbageCollectedEntries.poll()) != null) {
            delegate.removeObject(sv.key);
        }
    }

    private static class SoftEntry extends SoftReference<Object> {
        private final Object key;

        SoftEntry(Object key, Object value, ReferenceQueue<Object> garbageCollectionQueue) {
            super(value, garbageCollectionQueue);
            this.key = key;
        }
    }

}