package RedisORM.maps;

import java.util.ArrayList;
import java.util.List;
/*
<hashs>
    <hash id="" Type="" >
        <key property="" javaType="" />
        <field property="" field="" javaType="" exist="" />
        <list property="" javaType="" key="" exist="" />
        <set property="" key="" exist="" sorted="" />
        <string property="" key="" exist=""/>
        <hash property="" id="" />
    </hash>
</hashs>
 */

public class RHashMap{

    private String id = null;

    private RKeyMap key = null;

    private Class javaType = null;

    private List<RStringMap> RStringMaps = new ArrayList<RStringMap>();

    private List<RListMap> RListMaps = new ArrayList<RListMap>();

    private List<RSetMap> RSetMaps = new ArrayList<RSetMap>();

    private List<RHashMapRef> RHashMapRefs = new ArrayList<RHashMapRef>();

    private List<RFieldMap> RFieldMaps = new ArrayList<RFieldMap>();

    public RHashMap() {
    }

    public String getId() {
        return id;
    }

    public RKeyMap getKey() {
        return key;
    }

    public List<RStringMap> getRStringMaps() {
        return RStringMaps;
    }

    public List<RListMap> getRListMaps() {
        return RListMaps;
    }

    public List<RSetMap> getRSetMaps() {
        return RSetMaps;
    }

    public List<RHashMapRef> getRHashMapRefs() {
        return RHashMapRefs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(RKeyMap key) {
        this.key = key;
    }

    public List<RFieldMap> getRFieldMaps() {
        return RFieldMaps;
    }

    public Class getJavaType() {
        return javaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }



    @Override
    public String toString() {
        return "RHashMap{" +
                "id='" + id + '\'' +
                ", key=" + key +
                ", javaType=" + javaType +
                ", RStringMaps=" + RStringMaps +
                ", RListMaps=" + RListMaps +
                ", RSetMaps=" + RSetMaps +
                ", RHashMapRefs=" + RHashMapRefs +
                ", RFieldMaps=" + RFieldMaps +
                '}';
    }
}
