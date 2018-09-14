package RedisORM;

import RedisORM.cache.CacheSetting;
import RedisORM.datasource.DataSource;
import RedisORM.executor.opItem.HashItem;
import RedisORM.maps.RHashMap;
import RedisORM.builder.Builder;
import RedisORM.builder.DefaultBuilder;
import RedisORM.parse.ParseAlias;
import RedisORM.parse.ParseAnnotationHashs;
import RedisORM.parse.ParseXMLHashs;
import java.io.File;
import java.util.Map;

/**
 * 配置类，用来保存框架的整个信息
 */
public class Configuration {

    // 配置文件名
    private File configurationFile;

    // 记录别名
    private TypeAlias typeAlias;

    // 记录所有映射配置，key=id,value=对应的映射
    private Map<String,RHashMap> hashMaps;

    // 解析<typeAlias>节点的实现类
    private ParseAlias parseAlias;

    // 解析<mapper>节点的实现类
    private ParseXMLHashs parseXMLHashs;

    // 解析<<scan>>节点的实现类
    private ParseAnnotationHashs parseAnnotationHashs;

    // 存放每个类对应的执行器
    private Map<Class,HashItem> hashItemMap;

    // 数据源
    private DataSource dataSource;

    // 是否开启懒加载
    private boolean lazy;

    // Cache设置
    private CacheSetting cacheSetting;

    public Configuration(String filename){
        // 初始化
        init(filename);
    }

    private void init(String filename){
        //开始构建
        Builder builder = new DefaultBuilder(this);
        builder.build(filename);
    }

    public TypeAlias getTypeAlias() {
        return typeAlias;
    }

    public void setTypeAlias(TypeAlias typeAlias) {
        this.typeAlias = typeAlias;
    }

    public Map<String, RHashMap> getHashMaps() {
        return hashMaps;
    }

    public void setHashMaps(Map<String, RHashMap> hashMaps) {
        this.hashMaps = hashMaps;
    }

    public File getConfigurationFile() {
        return configurationFile;
    }

    public ParseXMLHashs getParseXMLHashs() {
        return parseXMLHashs;
    }

    public void setParseXMLHashs(ParseXMLHashs parseXMLHashs) {
        this.parseXMLHashs = parseXMLHashs;
    }

    public ParseAnnotationHashs getParseAnnotationHashs() {
        return parseAnnotationHashs;
    }

    public void setParseAnnotationHashs(ParseAnnotationHashs parseAnnotationHashs) {
        this.parseAnnotationHashs = parseAnnotationHashs;
    }

    public void setConfigurationFile(File configurationFile) {
        this.configurationFile = configurationFile;
    }

    public Map<Class, HashItem> getHashItemMap() {
        return hashItemMap;
    }

    public void setHashItemMap(Map<Class, HashItem> hashItemMap) {
        this.hashItemMap = hashItemMap;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ParseAlias getParseAlias() {
        return parseAlias;
    }

    public void setParseAlias(ParseAlias parseAlias) {
        this.parseAlias = parseAlias;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public CacheSetting getCacheSetting() {
        return cacheSetting;
    }

    public void setCacheSetting(CacheSetting cacheSetting) {
        this.cacheSetting = cacheSetting;
    }
}
