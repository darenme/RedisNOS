
package RedisORM.logging.log4j;

import RedisORM.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * log4j的适配器
 */
public class Log4jImpl implements Log {
  
  // 这个类的名字
  private static final String FQCN = Log4jImpl.class.getName();

  // 实际使用的log
  private final Logger log;
  // 初始化
  public Log4jImpl(String clazz) {
    log = Logger.getLogger(clazz);
  }

// 下面是通过log4j对接口方法的实现
  @Override
  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  @Override
  public boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  @Override
  public void error(String s, Throwable e) {
    log.log(FQCN, Level.ERROR, s, e);
  }

  @Override
  public void error(String s) {
    log.log(FQCN, Level.ERROR, s, null);
  }

  @Override
  public void debug(String s) {
    log.log(FQCN, Level.DEBUG, s, null);
  }

  @Override
  public void trace(String s) {
    log.log(FQCN, Level.TRACE, s, null);
  }

  @Override
  public void warn(String s) {
    log.log(FQCN, Level.WARN, s, null);
  }

}
