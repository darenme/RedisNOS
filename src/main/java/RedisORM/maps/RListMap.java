package RedisORM.maps;

import java.util.ArrayList;

// <list property="" key="" exist="" />
public class RListMap extends RBaseMap{

    private String key = null;

    private boolean exist = false;

    public RListMap(String property, Class javaType) {
        super(property, javaType);
    }

    public RListMap(String property, Class javaType, String key, boolean exist) {
        super(property, javaType==null?ArrayList.class:javaType);
        this.key = key;
        this.exist = exist;
    }

    public String getKey() {
        return key;
    }

    public boolean getExist() {
        return exist;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "RListMap{" +
                "key='" + key + '\'' +
                ", exist=" + exist +
                '}';
    }
}
