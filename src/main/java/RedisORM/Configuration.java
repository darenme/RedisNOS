package RedisORM;


import RedisORM.cache.Cache;
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

public class Configuration {

    // 配置文件名
    private File configurationFile;

    // 记录别名
    private TypeAlias typeAlias;

    // 记录所有hash
    private Map<String,RHashMap> hashMaps;

    private ParseAlias parseAlias;

    private ParseXMLHashs parseXMLHashs;

    private ParseAnnotationHashs parseAnnotationHashs;

    private Map<Class,HashItem> hashItemMap;

    private DataSource dataSource;

    private boolean lazy;

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
