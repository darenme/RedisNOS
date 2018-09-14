package RedisORM.cache;

/**
 * 缓存接口，声明缓存类所要实现的方法。
 *
 * 缓存使用了装饰器设计模式，将缓存的每种功能分离来实现，根据用户的配置来选择缓存的功能。
 */

public interface Cache {

  // 获取这个缓存的id
  String getId();

  // 存放Cache
  void putObject(Object key, Object value);

  // 获取Cache
  Object getObject(Object key);

  // 移除Cache
  Object removeObject(Object key);

  // 清除所有Cache
  void clear();

  // 获取Cache的大小
  int getSize();


}