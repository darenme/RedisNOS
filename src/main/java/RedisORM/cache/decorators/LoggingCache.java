
package RedisORM.cache.decorators;

import RedisORM.cache.Cache;
import RedisORM.logging.Log;
import RedisORM.logging.LogFactory;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 日志缓存
 * 添加功能：取缓存时打印命中率、存放和移除时打印信息
 */
public class LoggingCache implements Cache {


  private Log log;
  private Cache delegate;
  protected int requests = 0;
  protected int hits = 0;

  public LoggingCache(Cache delegate) {
    this.delegate = delegate;
    this.log = LogFactory.getLog(LoggingCache.class);
  }

  @Override
  public int getSize() {
    return delegate.getSize();
  }

  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public void putObject(Object key, Object object) {
    if(log.isDebugEnabled()){
      log.debug("----Put "+key.toString()+" to Cache");
    }
    delegate.putObject(key, object);
  }

  //目的就是getObject时，打印命中率
  @Override
  public Object getObject(Object key) {
    //访问一次requests加一
    requests++;
    final Object value = delegate.getObject(key);
    //命中了则hits加一
    if (value != null) {
      hits++;
    }
    if (log.isDebugEnabled()) {
      //就是打印命中率 hits/requests
      log.debug("Cache Hit Ratio: " + getHitRatio()+" and get: "+key.toString());
    }
    return value;
  }

  @Override
  public Object removeObject(Object key) {
    if(log.isDebugEnabled()){
      log.debug("Remove "+key.toString()+" from Cache");
    }
    return delegate.removeObject(key);
  }

  @Override
  public void clear() {
    if(log.isDebugEnabled()){
      log.debug(" Cache has been cleared");
    }
    delegate.clear();
  }

  @Override
  public int hashCode() {
    return delegate.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return delegate.equals(obj);
  }

  private double getHitRatio() {
    return (double) hits / (double) requests;
  }

}
