package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.annotation.*;
import RedisORM.maps.*;
import RedisORM.parse.exceptions.ErrorTypeException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

// 这个类用来解析一个类
public class ParseClass {

    Configuration configuration;

    RHashMap rHashMap=null;

    Class clazz=null;

    public ParseClass(Configuration configuration) {
        this.configuration = configuration;
    }

    // 调用的解析函数
    public void parse(Class clazz){

        this.clazz = clazz;
        // 创建一个RHashMap
        this.rHashMap = new RHashMap();
        // 开始解析
        parseType(clazz);
    }

    // 解析类
    private void parseType(Class clazz) {
        RHash hash = (RHash)clazz.getAnnotation(RHash.class);
        // 如果是注解过的类
        if(hash!=null){
            // 存入id
            rHashMap.setId(hash.id());
            rHashMap.setJavaType(clazz);
            // 解析每一个字段
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                parseField(field);
            }
            // 将解析形成的RHashMap放入Configuration
            configuration.getHashMaps().put(rHashMap.getId(),rHashMap);
        }
    }

    // 解析类中的每一个字段
    private void parseField(Field field) {
        // 解析注解@RKey
        if(field.isAnnotationPresent(RKey.class)){
            parseFieldRKey(field);
            return;
        }
        // 解析注解@RField(field = "name",exist = Exist.Default)
        if(field.isAnnotationPresent(RField.class)){
            parseFieldRField(field);
            return;
        }
        // 解析注解@RHash(id="test")
        if(field.isAnnotationPresent(RHash.class)){
            parseFieldRHash(field);
            return;
        }
        // 解析注解@RSet(key="teachers",javaType="", sorted = false)
        if(field.isAnnotationPresent(RSet.class)){
            parseFieldRSet(field);
            return;
        }
        // 解析注解@RList(key="courses",javaType=ArrayList.class,Exist=Exist.ex)
        if(field.isAnnotationPresent(RList.class)){
            parseFieldRList(field);
            return;
        }
        // 解析注解@RString(key = "signature" , exist = Exist.EX)
        if(field.isAnnotationPresent(RString.class)){
           parseFieldRString(field);
           return;
        }
    }


    private void parseFieldRKey(Field field){
        RKeyMap rKeyMap = (RKeyMap) new RKeyMap(field.getName(),field.getType());
        rHashMap.setKey(rKeyMap);
    }

    private void parseFieldRField(Field field){
        // 是否是基础类
        if(!isBaseType(field.getType()))
            throw new ErrorTypeException(field.getName()+" in "+this.clazz.getName()+" should be BaseType");
        RField rField = field.getAnnotation(RField.class);
        String key = rField.field();
        boolean ex = rField.exist();
        rHashMap.getRFieldMaps().add(new RFieldMap(field.getName(),field.getType(),key,ex));
    }

    private void parseFieldRHash(Field field){
        if(isBaseType(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+this.clazz.getName()+"should not be BaseType");
        RHash rHash = field.getAnnotation(RHash.class);
        String id = rHash.id();
        rHashMap.getRHashMapRefs().add(new RHashMapRef(field.getName(),id));
    }

    private void parseFieldRSet(Field field){
        if(!Set.class.isAssignableFrom(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+this.clazz.getName()+"should be Set");
        RSet rSet = field.getAnnotation(RSet.class);
        String key = rSet.key();
        Class clazz =rSet.javaType();
        boolean sorted = rSet.sorted();
        boolean ex = rSet.exist();
        rHashMap.getRSetMaps().add(new RSetMap(field.getName(),clazz,key,ex,sorted));
    }

    private void parseFieldRList(Field field){
        if(!List.class.isAssignableFrom(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+this.clazz.getName()+"should be List");
        RList rList = field.getAnnotation(RList.class);
        String key = rList.key();
        Class clazz = rList.javaType();
        boolean ex = rList.exist();
        rHashMap.getRListMaps().add(new RListMap(field.getName(),clazz,key,ex));
    }

    private void parseFieldRString(Field field){
        if(!String.class.isAssignableFrom(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+this.clazz.getName()+"should be String");
        RString rString = field.getAnnotation(RString.class);
        String key = rString.key();
        boolean ex = rString.exist();
        rHashMap.getRStringMaps().add(new RStringMap(field.getName(),key,ex));
    }

    private boolean isBaseType(Class clazz){
        if(clazz==int.class||clazz==short.class||clazz==char.class||clazz==byte.class||clazz==float.class||clazz==double.class||clazz==long.class) return true;
        if(java.lang.Number.class.isAssignableFrom(clazz)) return true;
        if(java.lang.String.class.isAssignableFrom(clazz)) return true;
        return false;
    }


}
