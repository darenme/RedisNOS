package RedisORM.parse;

/**
 * 解析配置文件接口和注解
 */
public interface Parse {

    // 解析<setting>节点
    public void parseSettings();

    // 解析<alias>节点
    public void parseAlias();

    // 解析<datasource>节点
    public void parseDataSource();

    // 解析<mapper>节点
    public void parseMapper();

    // 解析注解
    public void parseAnnotation();

}
