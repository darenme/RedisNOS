package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.annotations.*;
import RedisORM.maps.*;
import RedisORM.parse.exceptions.ErrorTypeException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

// 这个类用来解析一个类
public class ParseClass {

    Configuration configuration;

//    RHashMap rHashMap=null;
//
//    Class clazz=null;

    public ParseClass(Configuration configuration) {
        this.configuration = configuration;
    }

    // 调用的解析函数
    public void parse(Class clazz){

        // 开始解析
        parseClass(clazz);
    }

    // 解析类
    private void parseClass(Class clazz) {
        RHash hash = (RHash)clazz.getAnnotation(RHash.class);
        // 如果是RHash注解过的类
        if(hash!=null){
            // 创建一个新的RHashMap
            RHashMap rHashMap = new RHashMap(hash.id(),clazz,false);
            // 解析每一个字段
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                parseField(rHashMap,clazz,field,false);
            }
            // 将解析形成的RHashMap放入Configuration
            configuration.getHashMaps().put(rHashMap.getId(),rHashMap);
        }else{// 如果是Rserialize注解过的类
            RSerialize RSerialize = (RSerialize) clazz.getAnnotation(RSerialize.class);
            if(RSerialize !=null){
                RHashMap rHashMap = new RHashMap(RSerialize.id(),clazz,true);
                Field[] fields = clazz.getDeclaredFields();
                for(Field field:fields){
                    if(parseField(rHashMap,clazz,field,true))
                        break;
                }
                configuration.getHashMaps().put(rHashMap.getId(),rHashMap);
            }
        }
    }

    // 解析类中的每一个字段
    private boolean parseField(RHashMap rHashMap,Class clazz,Field field,boolean serialize) {
        if(serialize){
            // 解析注解@RKey
            if(field.isAnnotationPresent(RKey.class)){
                parseFieldRKey(rHashMap,clazz,field);
                return true;
            }
        }else{
        // 解析注解@RKey
        if(field.isAnnotationPresent(RKey.class)){
            parseFieldRKey(rHashMap,clazz,field);
            return false;
        }
        // 解析注解@RField(field = "name",exist = Exist.Default)
        if(field.isAnnotationPresent(RField.class)){
            parseFieldRField(rHashMap,clazz,field);
            return false;
        }
        // 解析注解@RHash(id="test")
        if(field.isAnnotationPresent(RHash.class)){
            parseFieldRHash(rHashMap,clazz,field);
            return false;
        }
        // 解析注解@RSet(key="teachers",javaType="", sorted = false)
        if(field.isAnnotationPresent(RSet.class)){
            parseFieldRSet(rHashMap,clazz,field);
            return false;
        }
        // 解析注解@RList(key="courses",javaType=ArrayList.class,Exist=Exist.ex)
        if(field.isAnnotationPresent(RList.class)){
            parseFieldRList(rHashMap,clazz,field);
            return false;
        }
        // 解析注解@RString(key = "signature" , exist = Exist.EX)
        if(field.isAnnotationPresent(RString.class)){
           parseFieldRString(rHashMap,clazz,field);
           return false;
        }
        if(field.isAnnotationPresent(RSerialize.class)){
            parseFieldRSerialize(rHashMap,clazz,field);
            return false;
        }
        }
        return false;
    }



    // 解析注解为RKey的字段
    private void parseFieldRKey(RHashMap rHashMap,Class clazz,Field field){
        RKeyMap rKeyMap = (RKeyMap) new RKeyMap(field.getName(),field.getType());
        rHashMap.setKey(rKeyMap);
    }

    // 解析注解为RField的字段
    private void parseFieldRField(RHashMap rHashMap,Class clazz,Field field){
        // 是否是基础类
        if(!isBaseType(field.getType()))
            throw new ErrorTypeException(field.getName()+" in "+clazz.getName()+" should be BaseType");
        RField rField = field.getAnnotation(RField.class);
        String key = rField.field();
        boolean ex = rField.exist();
        rHashMap.getRFieldMaps().add(new RFieldMap(field.getName(),field.getType(),key,ex));
    }

    // 解析注解为RHash的字段
    private void parseFieldRHash(RHashMap rHashMap,Class clazz,Field field){
        if(isBaseType(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+clazz.getName()+"should not be BaseType");
        RHash rHash = field.getAnnotation(RHash.class);
        String id = rHash.id();
        rHashMap.getRHashMapRefs().add(new RHashMapRef(field.getName(),id,field.getType(),false));
    }

    // 解析注解为RSerialize的字段
    private void parseFieldRSerialize(RHashMap rHashMap, Class clazz, Field field) {
        if(isBaseType(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+clazz.getName()+"should not be BaseType");
        RSerialize rSerialize = field.getAnnotation(RSerialize.class);
        rHashMap.getRHashMapRefs().add(new RHashMapRef(field.getName(),rSerialize.id(),field.getType(),true));
    }

    // 解析注解为RSet的字段
    private void parseFieldRSet(RHashMap rHashMap,Class clazz,Field field){
        if(!Set.class.isAssignableFrom(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+clazz.getName()+"should be Set");
        RSet rSet = field.getAnnotation(RSet.class);
        String key = rSet.key();
        Class cla =rSet.javaType();
        boolean sorted = rSet.sorted();
        boolean ex = rSet.exist();
        rHashMap.getRSetMaps().add(new RSetMap(field.getName(),cla,key,ex,sorted));
    }

    // 解析注解为RList的字段
    private void parseFieldRList(RHashMap rHashMap,Class clazz,Field field){
        if(!List.class.isAssignableFrom(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+clazz.getName()+"should be List");
        RList rList = field.getAnnotation(RList.class);
        String key = rList.key();
        Class cla = rList.javaType();
        boolean ex = rList.exist();
        rHashMap.getRListMaps().add(new RListMap(field.getName(),cla,key,ex));
    }

    // 解析注解为RString的字段
    private void parseFieldRString(RHashMap rHashMap,Class clazz,Field field){
        if(!String.class.isAssignableFrom(field.getType()))
            throw new ErrorTypeException(field.getName()+"in"+clazz.getName()+"should be String");
        RString rString = field.getAnnotation(RString.class);
        String key = rString.key();
        boolean ex = rString.exist();
        rHashMap.getRStringMaps().add(new RStringMap(field.getName(),key,ex));
    }

    // 检测是否是基本类型
    private boolean isBaseType(Class clazz){
        if(clazz==int.class||clazz==short.class||clazz==char.class||clazz==byte.class||clazz==float.class||clazz==double.class||clazz==long.class) return true;
        if(java.lang.Number.class.isAssignableFrom(clazz)) return true;
        if(java.lang.String.class.isAssignableFrom(clazz)) return true;
        return false;
    }


}
