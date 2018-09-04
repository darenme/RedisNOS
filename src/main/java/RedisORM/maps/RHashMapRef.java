package RedisORM.maps;

public class RHashMapRef {

    private String property = null;

    private String id = null;

    public RHashMapRef(String property, String id) {
        this.property = property;
        this.id = id;
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

    @Override
    public String toString() {
        return "RHashMapRef{" +
                "property='" + property + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
