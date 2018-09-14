package RedisORM.parse;

import RedisORM.TypeAlias;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.ErrorJavaTypeException;
import RedisORM.parse.exceptions.NoSuchElementException;
import RedisORM.parse.exceptions.NoValueException;
import org.dom4j.Element;

/**
 * 解析助手类，用于帮助解析配置文件
 */
public class ParseAssistant {

    static Log log;

    static{
        log = LogFactory.getLog(ParseAssistant.class);
    }

    /**
     * @Description: 获取子节点
     * @Date 2018/9/12 22:26
     * @param root 父节点
     * @param name 子节点名
     * @param exist 是否必须存在
     * @return 子节点
     */
    public static Element getElemet(Element root,String name,boolean exist){
        Element e;
        e = root.element(name);
        if(exist&&e==null) {
            log.error("Missing child element"+name+" in the element: "+root.getName());
            throw new NoSuchElementException(name);
        }
        return e;
    }

    /**
     * @Description: 获取节点属性
     * @Date 2018/9/12 22:28
     * @param e 节点
     * @param attribute 属性名
     * @param noneable  是否必须存在
     * @return 属性值
     */
    public static String getAttribute(Element e, String attribute, boolean noneable){
        String value = e.attributeValue(attribute);
        if(!noneable&&value==null){
            log.error("Missing attribute '"+attribute+"' in the Element: "+e.getName());
            throw new NoValueException(e.getName()+"."+attribute+" is null");
        }
        return value;
    }

    /**
     * @Description: 获取节点值
     * @Date 2018/9/12 22:29
     * @param e 节点
     * @return 节点值
     */
    public static String getValue(Element e){
        String value = e.getStringValue();
        if(value==null||value.equals("")) {
            log.error("Element "+e.getName()+" has no value");
            throw new NoValueException(e.getName());
        }
        return value;
    }

    /**
     * @Description: 转换属性值
     * @Date 2018/9/12 22:31
     * @param exist
     * @return java.lang.Boolean
     */
    public static Boolean parseExist(String exist){
        if(exist==null) return false;
        if(exist.equals("true")) return true;
        if(exist.equals("false")) return false;
        return null;
    }

    /**
     * @Description: 转换属性值
     * @Date 2018/9/12 22:33
     * @param typeAlias 别名
     * @param javaType  类型的名字
     * @return 类型
     */
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
