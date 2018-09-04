package RedisORM.maps;

// <field property="" field="" javaType="" exist="" />
public class RFieldMap extends RBaseMap{

    private String field = null;

    private boolean exist = false;


    public RFieldMap(String property, Class javaType, String field, boolean exist) {
        super(property, javaType);
        this.field = field;
        this.exist = exist;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean getExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    @Override
    public String toString() {
        return "RFieldMap{" +
                "field='" + field + '\'' +
                ", exist=" + exist + super.toString()+
                '}';
    }
}
