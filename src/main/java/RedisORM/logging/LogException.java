package RedisORM.logging;

/**
 * 日志异常
 */
public class LogException extends RuntimeException{

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }
}
