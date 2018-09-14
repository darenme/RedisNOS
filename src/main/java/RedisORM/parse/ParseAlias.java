package RedisORM.parse;

import org.dom4j.Element;

/**
 * 解析alias接口
 */
public interface ParseAlias {

    // 解析<alias>节点
    public void parseAlias(Element e);
}
