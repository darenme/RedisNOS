package RedisORM;

import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.SameAliasNameException;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类用来记录框架预设和用户添加的别名
 */
public class TypeAlias {

    private Map<String,String> alias= new HashMap<String,String>();

    private Log log;

    public TypeAlias() {
        init();
    }

    private void init() {
        log = LogFactory.getLog(TypeAlias.class);
        alias.put("boolean","java.lang.Boolean");
        alias.put("char","java.lang.Char");
        alias.put("short","java.lang.Short");
        alias.put("int","java.lang.Integer");
        alias.put("double","java.lang.Double");
        alias.put("long","java.lang.Long");
        alias.put("float","java.lang.Float");
        alias.put("byte","java.lang.Byte");
        alias.put("string","java.lang.String");
    }

    public void put(String alias, String name){
        if(this.alias.get(alias)!=null){
            try {
                log.warn("--------the alias: "+alias +" has been replaced");
                throw new SameAliasNameException("The alias:"+alias +" has been replaced");
            } catch (SameAliasNameException e) {
                e.printStackTrace();
            }
        }
        this.alias.put(alias,name);
    }

    public String get(String alias){
        return this.alias.get(alias);
    }

    @Override
    public String toString() {
        return "TypeAlias{" +
                "alias=" + alias +
                '}';
    }
}
