package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 默认的解析实现类
 */
public class DefaultParse implements Parse{

    Configuration configuration;

    Element root;

    Log log;

    public DefaultParse(Configuration configuration) {

        this.configuration=configuration;

        log = LogFactory.getLog(DefaultParse.class);

        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            if(log.isDebugEnabled()){
                log.debug("----read configuration file: "+configuration.getConfigurationFile());
            }
            document = saxReader.read(configuration.getConfigurationFile());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // 获取根元素
        root = document.getRootElement();

    }

    @Override
    public void parseSettings() {
        ParseSettings ps = new DefaultParseSettings(configuration);
        ps.parseSettings(root.element("settings"));

    }

    @Override
    public void parseAlias() {
        if(log.isDebugEnabled()){
            log.debug("----start parsing <typeAlias>");
        }
        ParseAlias pa = configuration.getParseAlias()==null?(new DefaultParseAlias(configuration)):configuration.getParseAlias();
        pa.parseAlias(root.element("typeAlias"));
    }

    @Override
    public void parseDataSource() {
        if(log.isDebugEnabled()){
            log.debug("----start parsing <datasource>");
        }
        ParseDataSource pd = new DefaultParseDataSource(configuration);
        pd.parseDataSource(root.element("datasource"));
    }

    @Override
    public void parseMapper() {
        if(log.isDebugEnabled()){
            log.debug("----start parsing <mappers>");
        }
        ParseXMLHashs ph = configuration.getParseXMLHashs()==null?(new DefaultParseXMLHashs(configuration)):configuration.getParseXMLHashs();
        ph.parseHashs(root.element("mappers"));
    }

    @Override
    public void parseAnnotation() {
        if(log.isDebugEnabled()){
            log.debug("----start parsing <scan>");
        }
        ParseAnnotationHashs pah = configuration.getParseAnnotationHashs()==null?(new DefaultParseAnnotationHashs(configuration)):configuration.getParseAnnotationHashs();
        pah.parseScan(root.element("scan"));
    }
}
