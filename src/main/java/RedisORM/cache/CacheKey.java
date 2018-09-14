package RedisORM.cache;

/**
 * 这个类是缓存的Key。缓存保存的是一个对象，以这个对象的类名和对象的id作为缓存的Key.
 */
public class CacheKey {

    // 对象的类型名
    private String className;

    // 对象的id
    private String id;

    /**
     * @Description: 构造函数
     * @Date 2018/9/12 12:12
     * @param className 类名
     * @param id        id
     */
    public CacheKey(String className, String id) {
        this.className = className;
        this.id = id;
    }

    /**
     * @Description: 重写equals()函数
     * @Date 2018/9/12 12:13
     * @param object 做比较的对象
     * @return boolean true:相等 false:不相等
     */
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

    /**
     * @Description: 重写hashCode函数
     * @Date 2018/9/12 12:14
     * @param
     * @return int hash值
     */
    @Override
    public int hashCode() {
        return className.hashCode()&id.hashCode();
    }

    @Override
    public String toString() {
        return "<"+className+":"+id+">";
    }
}
