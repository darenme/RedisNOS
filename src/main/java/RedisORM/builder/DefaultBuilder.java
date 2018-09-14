package RedisORM.builder;

import RedisORM.Configuration;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.DefaultParse;
import RedisORM.parse.Parse;
import java.io.File;



public class DefaultBuilder implements Builder {

    private Configuration configuration;

    private Log log;


    /**
     * @Description: 构造函数
     * @Date 2018/9/12 11:48
     * @param configuration 使用的配置类
     * @return
     */
    public DefaultBuilder(Configuration configuration) {

        this.configuration = configuration;

        log = LogFactory.getLog(DefaultBuilder.class);

    }

    /**
     * @Description: 构建函数
     * @Date 2018/9/12 11:49
     * @param filename 配置文件的文件名
     * @return void
     */
    public void build(String filename) {
        configuration.setConfigurationFile(searchFile(filename));
        // 解析配置文件
        parse();
        // 构造执行器
        generate();
    }


    /**
     * @Description: 寻找配置文件
     * @Date 2018/9/12 11:50
     * @param filename 配置文件名
     * @return java.io.File 返回一个File对象
     */
    private File searchFile(String filename){
        String path = ClassLoader.getSystemResource("").getPath();
        path = path.substring(1,path.length());
        path = path.replaceAll("////","////////");
        path = path +filename;
        return new File(path);
    }

    /**
     * @Description: 解析配置文件
     * @Date 2018/9/12 11:51
     * @param
     * @return void
     */
    private void parse(){
        if(log.isDebugEnabled()){
            log.debug("【--start parsing configuration file--】");
        }
        // 初始化一个解析器
        Parse parse = new DefaultParse(configuration);

        // 解析setting
        parse.parseSettings();

        // 解析DataSource
        parse.parseDataSource();

        // 解析别名
        parse.parseAlias();

        // 解析配置文件中的mapper
        parse.parseMapper();

        // 解析被注解的类
        parse.parseAnnotation();

    }

    /**
     * @Description: 生成执行器
     * @Date 2018/9/12 11:51
     * @param
     * @return void
     */
    private void generate(){
        if(log.isDebugEnabled()){
            log.debug("【--start building executor--】");
        }
        // item建造器
        BuilderItemMap builderItemMap = new BuilderItemMap(configuration);

        // 构建
        builderItemMap.build();
    }

}
