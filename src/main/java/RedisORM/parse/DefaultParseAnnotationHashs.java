package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.UndefinedNodeException;
import org.dom4j.Element;

import java.util.List;

/**
 * 解析类注解的实现接口
 */
public class DefaultParseAnnotationHashs implements ParseAnnotationHashs{

    private Configuration configuration;
    private Log log;
    private ParseClass pc;


    public DefaultParseAnnotationHashs(Configuration configuration) {
        this.configuration = configuration;
        log = LogFactory.getLog(DefaultParseAnnotationHashs.class);
        pc = new ParseClass(configuration);
    }

    @Override
    public void parseScan(Element scan) {
        if(scan==null) return;
        List<Element> elements = scan.elements();
        for(Element element:elements){
            if(element.getName().equals("package")){
                if(log.isDebugEnabled()){
                    log.debug("--------parsing <package>");
                }
                parsePackage(element);
                continue;
            }
            if(element.getName().equals("class")){
                parseClassElement(element);
                continue;
            }
            log.error("<"+scan.getName()+">"+" is undefined");
            throw new UndefinedNodeException(element.getName()+"in"+scan.getName()+"is undefined");
        }
    }

    // 解析<package>节点
    public void parsePackage(Element pack){
        // 获取包名
        String packagename = ParseAssistant.getAttribute(pack,"name",false);
        if(log.isDebugEnabled()){
            log.debug("----parsing package: "+packagename);
        }
    }

    // 解析<class type="com.mzw.Student">节点
    public void parseClassElement(Element element){
        String className = ParseAssistant.getAttribute(element,"type",false);
        if(log.isDebugEnabled()){
            log.debug("--------parsing class: '"+className+"'");
        }
        String realName = configuration.getTypeAlias().get(className);
        className = realName==null?className:realName;
        Class clazz=null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        parseClass(clazz);
    }

    private void parseClass(Class clazz) {
        pc.parse(clazz);
    }

}
