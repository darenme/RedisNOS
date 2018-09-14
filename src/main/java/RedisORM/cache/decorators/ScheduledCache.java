
package RedisORM.cache.decorators;

import RedisORM.cache.Cache;

/**
 * 定时调度缓存
 * 目的是每隔一段时间清空一下缓存
 *
 */
public class ScheduledCache implements Cache {

  private Cache delegate;
  // 清空缓存的周期
  protected long clearInterval;
  // 上一次清空的时间
  protected long lastClear;

  public ScheduledCache(Cache delegate,Long time) {
    this.delegate = delegate;
    this.clearInterval = time==null?(60*60*1000):time;
    this.lastClear = System.currentTimeMillis();
  }


  @Override
  public String getId() {
    return delegate.getId();
  }

  @Override
  public int getSize() {
    clearWhenStale();
    return delegate.getSize();
  }

  @Override
  public void putObject(Object key, Object object) {
    clearWhenStale();
    delegate.putObject(key, object);
  }

  @Override
  public Object getObject(Object key) {
    return clearWhenStale() ? null : delegate.getObject(key);
  }

  @Override
  public Object removeObject(Object key) {
    clearWhenStale();
    return delegate.removeObject(key);
  }

  @Override
  public void clear() {
    lastClear = System.currentTimeMillis();
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

  /**
   * @Description: 如果到时间了，清空一下缓存
   * @Date 2018/9/12 13:22
   * @param
   * @return boolean
   */
  private boolean clearWhenStale() {
    if (System.currentTimeMillis() - lastClear > clearInterval) {
      clear();
      return true;
    }
    return false;
  }

}
