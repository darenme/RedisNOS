package RedisORM.cache;

/**
 * Cache设置的封装，记录用户的配置
 */
public class CacheSetting {

    // 使用的淘汰策略，默认是最近最久未使用
    String eviction = "lru";

    // 清空缓存的时间
    long flush = 60*60*1000;

    // 缓存的大小，默认是1024
    int size = 1024;

    // 是否只读，默认不是
    boolean readOnly = false;

    public CacheSetting() {
    }

    public void setEviction(String eviction) {
        this.eviction = eviction;
    }

    public void setFlush(long flush) {
        this.flush = flush;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getEviction() {
        return eviction;
    }

    public long getFlush() {
        return flush;
    }

    public int getSize() {
        return size;
    }

    public boolean isReadOnly() {
        return readOnly;
    }


}
