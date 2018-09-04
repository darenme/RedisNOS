package RedisORM.parse;

import RedisORM.TypeAlias;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.ErrorJavaTypeException;
import RedisORM.parse.exceptions.NoSuchElementException;
import RedisORM.parse.exceptions.NoValueException;
import org.dom4j.Element;

public class ParseAssistant {

    static Log log;

    static{
        log = LogFactory.getLog(ParseAssistant.class);
    }

    public static Element getElemet(Element root,String name,boolean exist){
        Element e;
        e = root.element(name);
        if(exist&&e==null) {
            log.error("Missing child element"+name+" in the element: "+root.getName());
            throw new NoSuchElementException("name");
        }
        return e;
    }

    public static String getAttribute(Element e, String attribute, boolean noneable){
        String value = e.attributeValue(attribute);
        if(!noneable&&value==null){
            log.error("Missing attribute '"+attribute+"' in the Element: "+e.getName());
            throw new NoValueException(e.getName()+"."+attribute+" is null");
        }
        return value;
    }

    public static String getValue(Element e){
        String value = e.getStringValue();
        if(value==null||value.equals("")) {
            log.error("Element "+e.getName()+" has no value");
            throw new NoValueException(e.getName());
        }
        return value;
    }

    public static Boolean parseExist(String exist){
        if(exist==null) return false;
        if(exist.equals("true")) return true;
        if(exist.equals("false")) return false;
        return null;
    }

    public static Class parseJavaType(TypeAlias typeAlias,String javaType){
        if(javaType==null) return null;
        String name = typeAlias.get(javaType);
        Class clazz;
        if(name!=null){
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                log.error("Can't find Class: "+name );
                throw new ErrorJavaTypeException("can't find Class: "+name);
            }
        }else{
            try {
                clazz = Class.forName(javaType);
            } catch (ClassNotFoundException e) {
                log.error("Can't find Class: "+javaType );
                throw new ErrorJavaTypeException("can't find Class: "+javaType);
            }
        }
        return clazz;
    }
}
