package RedisORM.parse;


import org.dom4j.Element;

/**
 * 解析<setting>节点接口
 */
public interface ParseSettings {

    public void parseSettings(Element element);

}
