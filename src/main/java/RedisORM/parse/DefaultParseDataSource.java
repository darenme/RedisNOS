package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.datasource.PooledDataSource;
import RedisORM.datasource.UnPooledDataSource;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.NoSuchElementException;
import org.dom4j.Element;

/**
<datasource>
	<host></host>
	<port></port>
	<pooled maxtotal="" maxidle="">true</pooled>
</datasource>
 */

/**
 * 解析<datasource>的接口实现类
 */
public class DefaultParseDataSource implements ParseDataSource{

    Configuration configuration = null;

    Log log;

    public DefaultParseDataSource(Configuration configuration) {
        this.configuration = configuration;
        log = LogFactory.getLog(DefaultParseDataSource.class);
    }

    @Override
    public void parseDataSource(Element root) {
        if(root==null) {
            log.error("--------Missing 'datasource' Element");
            throw new NoSuchElementException("datasource");
        }
        Element host = ParseAssistant.getElemet(root,"host",true);
        Element port = ParseAssistant.getElemet(root,"port",true);
        String hostValue = ParseAssistant.getValue(host);
        int portvalue = Integer.parseInt(ParseAssistant.getValue(port));
        Element pooled = ParseAssistant.getElemet(root,"pooled",true);
        String maxtotal = null;
        String maxidle = null;
        if(ParseAssistant.getValue(pooled).equals("true")){
            maxtotal = ParseAssistant.getAttribute(pooled,"maxtotal",false);
            maxidle = ParseAssistant.getAttribute(pooled,"maxidle",false);
            int maxt = maxtotal==null?30:Integer.parseInt(maxtotal);
            int maxi = maxidle==null?10:Integer.parseInt(maxidle);
            if(log.isDebugEnabled()){
                log.debug("--------create pooledDataSource");
            }
            configuration.setDataSource(new PooledDataSource(hostValue,portvalue,maxt,maxi));
        }else{
            if(log.isDebugEnabled()){
                log.debug("--------create UnpooledDataSource");
            }
            configuration.setDataSource(new UnPooledDataSource(hostValue,portvalue));
        }
    }
}
