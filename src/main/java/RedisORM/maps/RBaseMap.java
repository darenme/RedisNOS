package RedisORM.maps;


public class RBaseMap {

    private String property;

    private Class javaType;

    RBaseMap(){
    }

    public RBaseMap(String property, Class javaType) {
        this.property = property;
        this.javaType = javaType;
    }

    public String getProperty() {
        return property;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }

    @Override
    public String toString() {
        return "RBaseMap{" +
                "property='" + property + '\'' +
                ", javaType=" + javaType +
                '}';
    }
}
