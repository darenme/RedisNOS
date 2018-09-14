package RedisORM.parse;

import org.dom4j.Element;

/**
 * 解析<scan>节点接口
 */
public interface ParseAnnotationHashs {

    public void parseScan(Element element);
}
