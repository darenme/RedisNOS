package RedisORM.session;

import RedisORM.Configuration;
import RedisORM.cache.Cache;
import RedisORM.cache.CacheClone;
import RedisORM.cache.CacheKey;
import RedisORM.executor.exceptions.UnSupportException;
import RedisORM.executor.opItem.HashItem;
import RedisORM.parse.exceptions.ErrorTypeException;
import RedisORM.proxy.DefaultLazyProxy;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 会话的一个默认实现类
 */

public class DefaultRedisSession implements RedisSession {

    // 记录每个对象被改变的字段
    private Map<Object,Set<String>> changeMap;

    private Log log;

    Configuration configuration;

    // Session工厂
    SessionFactory sessionFactory;

    // 使用的Jedis
    Jedis jedisForTransaction;

    // 使用的事务
    Transaction transaction;


    public DefaultRedisSession(Configuration configuration, SessionFactory sessionFactory) {
        this.configuration = configuration;
        this.sessionFactory = sessionFactory;
        changeMap = new HashMap<>();
        jedisForTransaction = configuration.getDataSource().getJedis();
        transaction = jedisForTransaction.multi();
        log = LogFactory.getLog(DefaultRedisSession.class);
    }

    @Override
    public void save(Object object) {
        if(log.isDebugEnabled()){
            log.debug("start saving:"+ object.getClass().getCanonicalName());
        }
        // 获取相应的HashItem
        HashItem hashItem = configuration.getHashItemMap().get(object.getClass());

        if(hashItem==null){
            throw new UnSupportException(object.getClass().getCanonicalName());
        }
        // 保存到Redis中
        hashItem.save(transaction,null,object);

        // 创建对应的CacheKey
        CacheKey cacheKey = hashItem.getCacheKey(object);
        // 存放到Cache中
        sessionFactory.getCache().putObject(cacheKey,object);

        if(log.isDebugEnabled()){
            log.debug("finish saving:"+ object.getClass().getCanonicalName());
        }
    }

    @Override
    public <T> T get(Class<T> clazz, Object getid) {
        HashItem hashItem = configuration.getHashItemMap().get(clazz);
        CacheKey cacheKey = new CacheKey(clazz.getCanonicalName(),getid.toString());
        Object value = sessionFactory.getCache().getObject(cacheKey);
        if(value!=null){ //如果cache中已存在
            if(configuration.isLazy()==false&&!sessionFactory.isReadonly()){
                Object ans = CacheClone.cloneObject(value);
                if(log.isDebugEnabled()){
                    log.debug("clone Object from Cache");
                }
                return (T) ans;
            }else{
                return (T) value;
            }
        }else {// 如果不存在，那么就从redis中加载
            String id = getid.toString();
            if(log.isDebugEnabled()){
                log.debug("start getting ["+ clazz.getCanonicalName()+"] which id is: "+id+" from Redis");
            }

            Jedis jedis = configuration.getDataSource().getJedis();
            Object object = null;

            // 如果是懒加载，则返回代理对象
            if(configuration.isLazy()){
                try {
                    if(hashItem.exist(jedis,id)){
                        object = clazz.newInstance();
                        Set<String> change = new HashSet<>();
                        Object proxyObject = new DefaultLazyProxy(configuration,change,object,configuration.getHashItemMap().get(clazz),id).getProxyInstance();
                        changeMap.put(proxyObject,change);
                        if(log.isDebugEnabled()){
                            log.debug("finish getting ["+ clazz.getCanonicalName()+"] which id is: "+id+" from Redis");
                        }
                        sessionFactory.getCache().putObject(cacheKey,proxyObject);
                        return (T)proxyObject;
                    }else{
                        if(log.isDebugEnabled()){
                            log.debug("fail getting ["+ clazz.getCanonicalName()+"] which id is: "+id+" from Redis, no such Object");
                        }
                        return null;
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                object = hashItem.get(jedis,null,id);
                jedis.close();

                if(object!=null){// 如果获取到了
                    if(log.isDebugEnabled()){
                        log.debug("finish getting ["+ clazz.getCanonicalName()+"] which id is: "+id+" from Redis");
                    }
                    sessionFactory.getCache().putObject(cacheKey,object);
                }else{
                    if(log.isDebugEnabled()){
                        log.debug("fail getting ["+ clazz.getCanonicalName()+"] which id is: "+id+" from Redis, no such Object");
                    }
                }
                return (T)object;
            }
        }
        return null;
    }

    @Override
    public void delete(Object object) {

        Class type;
        if(configuration.isLazy()){
            type = object.getClass().getSuperclass();
        }else{
            type = object.getClass();
        }
        HashItem hashItem = configuration.getHashItemMap().get(type);
        if(hashItem==null){
            throw new UnSupportException(type.getCanonicalName());
        }

        hashItem.delete(transaction,object);
        sessionFactory.getCache().removeObject(hashItem.getCacheKey(object));
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
                hi.update(transaction,changes,object);
            }
        }
        if(log.isDebugEnabled()){
            log.debug("finish updating:"+ object.getClass().getCanonicalName());
        }
    }

    @Override
    public void exec() {
        transaction.exec();
        try {
            transaction.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jedisForTransaction.close();
    }

    @Override
    public void save(List list) {
        for(Object object:list){
            save(object);
        }
    }

}
