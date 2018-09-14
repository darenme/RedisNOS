package RedisORM.maps;


/**
 * <string property="" key="" exist=""/>
 * 这个类记录映射为string类型的字段的映射
 */
public class RStringMap extends RBaseMap {

    private String key;

    private boolean exist;

    public RStringMap(String property, String key, boolean exist) {
        super(property, java.lang.String.class);
        this.key = key;
        this.exist = exist;
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

    @Override
    public String toString() {
        return "RStringMap{" +
                "key='" + key + '\'' +
                ", exist=" + exist +
                '}';
    }
}
