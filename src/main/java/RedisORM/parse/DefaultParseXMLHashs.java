package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.TypeAlias;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.maps.*;
import RedisORM.parse.exceptions.ErrorValueException;
import RedisORM.parse.exceptions.NoSuchElementException;
import RedisORM.parse.exceptions.UndefinedNodeException;
import org.dom4j.Element;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析<hash>节点的接口实现类
 */
public class DefaultParseXMLHashs implements ParseXMLHashs {

    private Configuration configuration;

    private TypeAlias typeAlias;

    private Map<String,RHashMap> hashMaps;

    Log log;

    public DefaultParseXMLHashs(Configuration configuration) {
        this.configuration = configuration;
        typeAlias = configuration.getTypeAlias();
        hashMaps = new HashMap<String,RHashMap>();
        configuration.setHashMaps(hashMaps);
        log = LogFactory.getLog(DefaultParseXMLHashs.class);
    }

    // 解析<string>节点 <string property="" key="" exist=""/>
    public RStringMap parseString(Element e) {
        String property = ParseAssistant.getAttribute(e,"property",false);
        String key = ParseAssistant.getAttribute(e,"key",true);
        String exist = ParseAssistant.getAttribute(e,"exist",true);
        Boolean ex = ParseAssistant.parseExist(exist);
        if(ex==null) {
            throw new ErrorValueException(e.getName()+".exists : error value '"+exist+"'");
        }else{
            return new RStringMap(property,key,ex);
        }

    }
    // 解析<key property="" javaType="" />节点
    public RKeyMap parseKey(Element e,Class pType) {
        String property = ParseAssistant.getAttribute(e,"property",false);
        Class clazz = null;
        try {
            clazz = pType.getDeclaredField(property).getType();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        return new RKeyMap(property,clazz);
    }

    // <list property="" javaType="" key="" exist="" />
    public RListMap parseList(Element e,Class pType) {
        String property = ParseAssistant.getAttribute(e,"property",false);
        String key = ParseAssistant.getAttribute(e,"key",true);
        String exist = ParseAssistant.getAttribute(e,"exist",true);
        Class clazz = null;
        try {
            clazz = pType.getDeclaredField(property).getType();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        Boolean ex = ParseAssistant.parseExist(exist);
        if(ex==null) {
            throw new ErrorValueException(e.getName()+".exists : error value '"+exist+"'");
        }else{
            return new RListMap(property,clazz,key,ex);
        }

    }

    // <set property="" javaType="" key="" exist="" sorted="" />
    public RSetMap parseSet(Element e,Class pType) {
        String property = ParseAssistant.getAttribute(e,"property",false);
        String key = ParseAssistant.getAttribute(e,"key",true);
        String exist = ParseAssistant.getAttribute(e,"exist",true);
        String sort = ParseAssistant.getAttribute(e,"sorted",true);
        Boolean ex = ParseAssistant.parseExist(exist);
        Class clazz = null;
        try {
            clazz = pType.getDeclaredField(property).getType();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        boolean sorted=false;
        if(sort!=null){
            if(sort.equals("true")) {
                sorted=true;
            }else if(sort.equals("false")) {
                sorted=false;
            }else{
                throw new ErrorValueException(e.getName()+".sorted error: "+sort);
            }
        }
        if(ex==null) {
            throw new ErrorValueException(e.getName()+".exists : error value '"+exist+"'");
        }else{
            return new RSetMap(property,clazz,key,ex,sorted);
        }

    }

    // <hash property="" id="" serialize="true"/>
    // 字段为hash，将它封装为一个RHashMapRef
    public RHashMapRef parseHash (Element e,Class pType) {
        String property = ParseAssistant.getAttribute(e,"property",false);
        String id = ParseAssistant.getAttribute(e,"id",false);
        String ser = ParseAssistant.getAttribute(e,"serialize",true);
        Class clazz = null;
        try {
            clazz = pType.getDeclaredField(property).getType();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        boolean serialize = false;
        if(ser!=null){
            if(ser.equals("true")){
                serialize = true;
            }else if(ser.equals("false")){
                serialize = false;
            }else {
                throw new ErrorValueException("value of attribute serialize is error!");
            }
        }
        return new RHashMapRef(property,id,clazz,serialize);
    }

    // <field property="" field="" javaType="" exist="" />
    public RFieldMap parseField(Element e,Class pType){
        String property = ParseAssistant.getAttribute(e,"property",false);
        String field = ParseAssistant.getAttribute(e,"field",true);
        String exist = ParseAssistant.getAttribute(e,"exist",true);
        Class clazz = null;
        try {
            clazz = pType.getDeclaredField(property).getType();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        Boolean ex = ParseAssistant.parseExist(exist);
        if(ex==null) {
            log.error(e.getName()+".exists : error value '"+exist+"'");
            throw new ErrorValueException(e.getName()+".exists : error value '"+exist+"'");
        }else{
            return new RFieldMap(property,clazz,field,ex);
        }
    }

    // <hash id="" javaType="" serialize="true">
    // 解析一个类的对应的配置
    public RHashMap parseHashMap(Element e){

        // 获取id
        String id = ParseAssistant.getAttribute(e, "id", false);
        if(log.isDebugEnabled()){
            log.debug("--------start parsing mapper:'"+id+"'");
        }
        // 获取serialize
        String ser = ParseAssistant.getAttribute(e,"serialize",true);
        boolean serialize = false;
        if(ser!=null){
            if(ser.equals("true")){
                serialize = true;
            }else if(ser.equals("false")){
                serialize = false;
            }else{
                throw new ErrorValueException("value of attribute 'serialize' in "+ e.getName()+"is error");
            }
        }
        // 获取javaType
        String javaType = ParseAssistant.getAttribute(e, "javaType", false);
        Class clazz = ParseAssistant.parseJavaType(typeAlias,javaType);
        RHashMap rHashMap = new RHashMap(id,clazz,serialize);
        if(e.element("key")==null){
            String error = "Lack of child nodes 'key' in hash element "+ParseAssistant.getAttribute(e,"id",false);
            log.error(error);
            throw new NoSuchElementException(error);
        }
        // 解析子节点
        List<Element> elements = e.elements();

        for (Element element : elements) {
            // 节点名
            String name = element.getName();

            if(serialize){
                if (name.equals("key")) {
                    rHashMap.setKey(parseKey(element,clazz));
                    break;
                }
            }else{
                if (name.equals("string")) {
                    rHashMap.getRStringMaps().add(parseString(element));
                    continue;
                }
                if (name.equals("list")) {
                    rHashMap.getRListMaps().add(parseList(element,clazz));
                    continue;
                }
                if (name.equals("set")) {
                    rHashMap.getRSetMaps().add(parseSet(element,clazz));
                    continue;
                }
                if (name.equals("key")) {
                    rHashMap.setKey(parseKey(element,clazz));
                    continue;
                }
                if (name.equals("field")) {
                    rHashMap.getRFieldMaps().add(parseField(element,clazz));
                    continue;
                }
                if (name.equals("hash")) {
                    rHashMap.getRHashMapRefs().add(parseHash(element,clazz));
                    continue;
                }
                throw new UndefinedNodeException(element.getName() + " is not defined");
            }
        }
        if(log.isDebugEnabled()){
            log.debug("--------finish parsing mapper:'"+id+"'");
        }

        return rHashMap;
    }

    @Override
    public void parseHashs(Element e) {
        if(e==null) return;
        List<Element> hashs = e.elements();
        for(Element hash : hashs){
            if(hash.getName().equals("hash")){
                hashMaps.put(ParseAssistant.getAttribute(hash,"id",false),parseHashMap(hash));
            }
        }
    }
}
