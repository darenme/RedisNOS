package RedisORM.logging;

public class LogException extends RuntimeException{

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }
}
