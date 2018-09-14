package RedisORM.parse;


import org.dom4j.Element;

/**
 * 解析<datasource>节点接口
 */
public interface ParseDataSource {

    public void parseDataSource(Element root);

}
