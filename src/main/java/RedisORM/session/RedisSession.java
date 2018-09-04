package RedisORM.session;

import RedisORM.Configuration;
import RedisORM.executor.opItem.HashItem;
import RedisORM.proxy.DefaultLazyProxy;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import redis.clients.jedis.Jedis;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class RedisSession implements Session{

    private Map<Object,Set<String>> changeMap;

    private Log log;

    Configuration configuration = null;

    public RedisSession(Configuration configuration) {
        this.configuration = configuration;
        changeMap = new HashMap<>();
        log = LogFactory.getLog(RedisSession.class);
    }


    @Override
    public void save(Object object) {
        if(log.isDebugEnabled()){
            log.debug("start saving:"+ object.getClass().getCanonicalName());
        }
        HashItem hashItem = configuration.getHashItemMap().get(object.getClass());
        Jedis jedis = configuration.getDataSource().getJedis();
        hashItem.save(jedis,null,object);
        jedis.close();
        if(log.isDebugEnabled()){
            log.debug("finish saving:"+ object.getClass().getCanonicalName());
        }
    }

    @Override
    public <T> T get(Class<T> clazz, String id) {
        if(log.isDebugEnabled()){
            log.debug("start getting ["+ clazz.getCanonicalName()+"] which id is: "+id);
        }
        HashItem hashItem = configuration.getHashItemMap().get(clazz);
        Jedis jedis = configuration.getDataSource().getJedis();
        Object object = null;
        if(configuration.isLazy()){
            try {
                object = clazz.newInstance();
                Set<String> change = new HashSet<>();
                Object proxyObject = new DefaultLazyProxy(configuration,change,object,configuration.getHashItemMap().get(clazz),id).getProxyInstance();
                changeMap.put(proxyObject,change);
                return (T)proxyObject;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            object = hashItem.get(jedis,null,id);
        }

        jedis.close();
        if(log.isDebugEnabled()){
            log.debug("finish getting:"+ clazz.getCanonicalName()+" : "+id);
        }
        return (T)object;
    }

    @Override
    public void delete(Object object) {
        String[] subids;
        HashItem hashItem = configuration.getHashItemMap().get(object.getClass());
        if(hashItem!=null){
            String id=null;
            try {
                id = hashItem.getIdGetMethod().invoke(object).toString();
                if(log.isDebugEnabled()){
                    log.debug("start deleting:"+ object.getClass().getCanonicalName()+" : "+id);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            String key = object.getClass().getCanonicalName()+"$"+id;
            Jedis jedis = configuration.getDataSource().getJedis();
            subids = jedis.hget(key,"#{subIds}").split("\\|");
            List<Class> classes = hashItem.getHashItems();
            for (int i = 0; i < subids.length; i++) {
                String subkey = key+"."+classes.get(i).getCanonicalName()+"$"+subids[i];
                jedis.del(subkey);
            }
            jedis.del(key);
            if(log.isDebugEnabled()){
                log.debug("finish deleting:"+ object.getClass().getCanonicalName()+" : "+id);
            }
        }
        changeMap.remove(object);

    }

    @Override
    public void update(Object object) {
        if(log.isDebugEnabled()){
            log.debug("start updating:"+ object.getClass().getCanonicalName());
        }
        if(!configuration.isLazy()){
            save(object);
        }else{
            Set<String> changes= changeMap.get(object);
            if(changes!=null&&changes.size()!=0){
                HashItem hi= configuration.getHashItemMap().get(object.getClass().getGenericSuperclass());
                hi.update(changes,object);
            }
        }
        if(log.isDebugEnabled()){
            log.debug("finish updating:"+ object.getClass().getCanonicalName());
        }
    }
}
