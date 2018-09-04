package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.TypeAlias;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.NoValueException;
import org.dom4j.Element;

import java.util.List;

public class DefaultParseAlias implements ParseAlias{

    Configuration configuration;

    TypeAlias typeAlias;

    Log log;

    public DefaultParseAlias(Configuration configuration) {
        this.configuration = configuration;
        typeAlias = new TypeAlias();
        configuration.setTypeAlias(typeAlias);
        log = LogFactory.getLog(DefaultParseAlias.class);
    }

    /*
     <typeAlias>
        <alias name="" javaType=""/>
     </typeAlias>
      */
    @Override
    public void parseAlias(Element e) {
        if(e==null) return;
        List<Element> aliass = e.elements();
        for(Element alias : aliass){
            if(alias.getName().equals("alias")){
                String name = ParseAssistant.getAttribute(alias,"name",false);
                String javaType = ParseAssistant.getAttribute(alias,"javaType",false);
                typeAlias.put(name,javaType);
                if(log.isDebugEnabled()){
                    log.debug("--------add alias:'"+name+"' to typeAlias");
                }
            }
        }
    }
}
