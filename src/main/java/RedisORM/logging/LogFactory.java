
package RedisORM.logging;

import RedisORM.logging.log4j.Log4jImpl;
import RedisORM.logging.log4j2.Log4j2Impl;
import RedisORM.logging.nologging.NoLoggingImpl;
import RedisORM.logging.slf4j.Slf4jImpl;
import java.lang.reflect.Constructor;

/**
 * 用于创建指定的日志适配器类
 */

public final class LogFactory {


  public static final String MARKER = "MYBATIS";

  //用来记录当前使用的第三方日志组件所对应的适配器的构造方法
  private static Constructor<? extends Log> logConstructor ;

  static {
    // 尝试按顺序加载日志组件
    //    tryImplementation(LogFactory::useSlf4jLogging);
    //    tryImplementation(LogFactory::useLog4J2Logging);
    //    tryImplementation(LogFactory::useLog4JLogging);
    //    tryImplementation(LogFactory::useNoLogging);
    try {
      logConstructor = Log4jImpl.class.getConstructor(String.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  private LogFactory() {
    // disable construction
    try {
      logConstructor = Log4jImpl.class.getConstructor(String.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  public static Log getLog(Class<?> aClass) {
    return getLog(aClass.getName());
  }

  public static Log getLog(String logger) {
    try {
      return logConstructor.newInstance(logger);
    } catch (Throwable t) {
      throw new LogException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
    }
  }

  public static synchronized void useCustomLogging(Class<? extends Log> clazz) {
    setImplementation(clazz);
  }

  public static synchronized void useSlf4jLogging() {
    setImplementation(Slf4jImpl.class);
  }

  public static synchronized void useLog4JLogging() {
    setImplementation(Log4jImpl.class);
  }

  public static synchronized void useLog4J2Logging() {
    setImplementation(Log4j2Impl.class);
  }

  public static synchronized void useNoLogging() {
    setImplementation(NoLoggingImpl.class);
  }

  private static void tryImplementation(Runnable runnable) {
    if (logConstructor == null) {
      try {
        runnable.run();
      } catch (Throwable t) {
      }
    }
  }

  //  设置log构造函数
  private static void setImplementation(Class<? extends Log> implClass) {
    try {
      Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
      Log log = candidate.newInstance(LogFactory.class.getName());
      if (log.isDebugEnabled()) {
        log.debug("Logging initialized using '" + implClass + "' adapter.");
      }
      logConstructor = candidate;
    } catch (Throwable t) {
    }
  }

}
