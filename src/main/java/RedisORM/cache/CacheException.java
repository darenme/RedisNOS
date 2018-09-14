
package RedisORM.cache;


/**
 * 配置Cache是发生的异常
 */
public class CacheException extends RuntimeException {

  public CacheException(String message) {
    super(message);
  }

  public CacheException(String message, Throwable cause) {
    super(message, cause);
  }

}
