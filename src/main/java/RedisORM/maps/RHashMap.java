package RedisORM.maps;

import java.util.ArrayList;
import java.util.List;
/**
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

/**
 * 这个类记录了一个类中的所有映射
 */

public class RHashMap{

    // 这个类映射的id
    private String id ;

    // 这个类中的id的映射
    private RKeyMap key = null;

    // 如果这个字段部位null，则表示这个RHashmap代表着某个嵌套类
    private Class javaType;

    // string类型的映射
    private List<RStringMap> RStringMaps;

    // list类型的映射
    private List<RListMap> RListMaps;

    // set类型的映射
    private List<RSetMap> RSetMaps;

    // 嵌套类型的映射
    private List<RHashMapRef> RHashMapRefs;

    // field类型的映射
    private List<RFieldMap> RFieldMaps;

    // 是否以序列化方式保存
    private boolean serialize;


    public RHashMap(String id,  Class javaType, boolean serialize) {
        this.id = id;
        this.javaType = javaType;
        this.serialize = serialize;
        if(!serialize){
            RStringMaps = new ArrayList<RStringMap>();
            RListMaps = new ArrayList<RListMap>();
            RSetMaps = new ArrayList<RSetMap>();
            RHashMapRefs = new ArrayList<RHashMapRef>();
            RFieldMaps = new ArrayList<RFieldMap>();
        }
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

    public boolean isSerialize() {
        return serialize;
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
                ", serialize=" + serialize +
                '}';
    }
}
