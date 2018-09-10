package RedisORM.maps;

public class RHashMapRef {

    private String property = null;

    // 指向的hashMap的id
    private String id = null;

    private Class javaType;

    private boolean serialize;

    public RHashMapRef(String property, String id, Class javaType, boolean serialize) {
        this.property = property;
        this.id = id;
        this.javaType = javaType;
        this.serialize = serialize;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

    @Override
    public String toString() {
        return "RHashMapRef{" +
                "property='" + property + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
