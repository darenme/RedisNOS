package RedisORM.maps;


/**
 * <set property="" key="" exist="" sorted="" />
 * 这个类记录映射为set类型的字段的映射
 */

public class RSetMap extends RBaseMap {
    private String key = null;

    private boolean exist = false;

    private boolean sorted = false;

    public RSetMap(String property, Class javaType, String key, boolean exist,  boolean sorted) {
        super(property,javaType);
        this.key = key;
        this.exist = exist;
        this.sorted = sorted;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    @Override
    public String toString() {
        return "RSetMap{" +
                "key='" + key + '\'' +
                ", exist=" + exist +
                ", sorted=" + sorted +
                '}';
    }
}
