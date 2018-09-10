package RedisORM.cache;

public class CacheKey {

    private String className;

    private String id;

    public CacheKey(String className, String id) {
        this.className = className;
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CacheKey)) {
            return false;
        }
        final CacheKey cacheKey = (CacheKey) object;
        return (className.equals(cacheKey.className)&&id.equals(cacheKey.id));

    }

    @Override
    public int hashCode() {
        return className.hashCode()&id.hashCode();
    }

    @Override
    public String toString() {
        return "<"+className+":"+id+">";
    }
}
