package RedisORM.cache;

public class CacheSetting {

    String eviction = "lru";

    long flush = 60*60*1000;

    int size = 1024;

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
