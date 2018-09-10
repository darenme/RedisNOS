package RedisORM.parse;

import RedisORM.Configuration;
import RedisORM.cache.Cache;
import RedisORM.cache.CacheSetting;
import RedisORM.cache.decorators.*;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import RedisORM.parse.exceptions.ErrorValueException;
import org.dom4j.Element;

import java.util.*;

public class DefaultParseSettings implements ParseSettings{

    Configuration configuration;

    Log log;

    public DefaultParseSettings(Configuration configuration) {
        this.configuration = configuration;
        log = LogFactory.getLog(DefaultParseSettings.class);
    }

    // 解析Settings节点
    @Override
    public void parseSettings(Element settings){
        if(log.isDebugEnabled()){
            log.debug("----start parsing <settings>");
        }
        if(settings==null) return;
        parseParse(settings);
        parseLazyLoad(settings);
        parseCache(settings);
    }

    private void parseCache(Element settings) {
        if(log.isDebugEnabled()){
            log.debug("--------start parsing <cache>");
        }
        Element cache = ParseAssistant.getElemet(settings,"cache",false);
        CacheSetting cs = new CacheSetting();
        if(cache!=null){
            // eviction
            String eviction = ParseAssistant.getAttribute(cache,"eviction",true);
            if(eviction!=null){
                if(!(eviction.equals("lru")||eviction.equals("fifo"))){
                    throw new ErrorValueException("value of attribute in <cache> 'eviction' is error");
                }
                if(log.isDebugEnabled()){
                    log.debug("------------eviction is: "+eviction);
                }
                cs.setEviction(eviction);
            }else {
                if(log.isDebugEnabled()){
                    log.debug("------------eviction is: lru");
                }
            }
            // flush
            String flush = ParseAssistant.getAttribute(cache,"flush",true);
            if(flush!=null){
                cs.setFlush(Long.parseLong(flush));
                if(log.isDebugEnabled()){
                    log.debug("------------flush is: "+flush+"ms");
                }
            }else {
                if(log.isDebugEnabled()){
                    log.debug("------------flush is: 6000000ms");
                }
            }

            // size
            String size = ParseAssistant.getAttribute(cache,"size",true);
            if(size!=null){
                cs.setSize(Integer.parseInt(size));
                if(log.isDebugEnabled()){
                    log.debug("------------size is: "+ size);
                }
            }else {
                if(log.isDebugEnabled()){
                    log.debug("------------size is: 1024");
                }
            }
            // readonly
            String readonly = ParseAssistant.getAttribute(cache,"readonly",true);
            if(readonly!=null){
                if(readonly.equals("true")){
                    cs.setReadOnly(true);
                }else if(readonly.equals("false")){
                    cs.setReadOnly(false);
                }else{
                    throw new ErrorValueException("value of attribute in <cache> 'readonly' is error");
                }
                if(log.isDebugEnabled()){
                    log.debug("------------readonly is: "+ readonly);
                }
            }else {
                if(log.isDebugEnabled()){
                    log.debug("------------readonly is: false");
                }
            }
        }
        configuration.setCacheSetting(cs);
    }


    private void parseLazyLoad(Element settings) {
        if(log.isDebugEnabled()){
            log.debug("--------start parsing <lazyload>");
        }
        Element e = ParseAssistant.getElemet(settings,"lazyload",false);
        if(e!=null){
            String lazy = ParseAssistant.getValue(e);
            if(lazy.equals("true")){
                configuration.setLazy(true);
                if(log.isDebugEnabled()){
                    log.debug("------------lazyload is 'true'");
                }
                return;
            }
            if(lazy.equals("false")){
                configuration.setLazy(false);
                if(log.isDebugEnabled()){
                    log.debug("------------lazyload is 'false'");
                }
                return;
            }
            log.error("value in element "+e.getName()+" is error");
            throw new ErrorValueException("value in element "+e.getName()+" is error");
        }else{
            configuration.setLazy(false);
            if(log.isDebugEnabled()){
                log.debug("------------lazyload is: false(default)");
            }
        }
    }

    // 解析<settings>中的<parse>节点
    /*
        <parse>
            <setting name="parseAlias" class="" />
            <setting name="parseXMLHashs" class="" />
        <parse/>
     */
    private void parseParse(Element settings){
        if(log.isDebugEnabled()){
            log.debug("--------start parsing <parse>");
        }
        Element parse = ParseAssistant.getElemet(settings,"parse",false);
        if(parse==null) return;
        List<Element> parses = parse.elements();
        for(Element setting:parses){
            String name = ParseAssistant.getAttribute(setting,"name",false);
            String clazz = ParseAssistant.getAttribute(setting,"class",false);
            setParseType(name,clazz);
        }
    }

    // 初始化configuration中的具体的解析器
    private void setParseType(String name,String clazz){
        if(name.equals("parseAlias")){
            try {
                configuration.setParseAlias((ParseAlias) (Class.forName(clazz).newInstance()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return;
        }
        if(name.equals("parseXMLHashs")){
            try {
                configuration.setParseXMLHashs((ParseXMLHashs) (Class.forName(clazz).newInstance()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return;
        }
        if(name.equals("parseAnnotationHashs")){
            try {
                configuration.setParseAnnotationHashs((ParseAnnotationHashs) (Class.forName(clazz).newInstance()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return;
        }
    }


}
