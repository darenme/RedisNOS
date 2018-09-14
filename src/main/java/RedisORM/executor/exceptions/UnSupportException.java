package RedisORM.executor.exceptions;

/**
 * 异常：不支持的类型
 */
public class UnSupportException extends RuntimeException{

    public UnSupportException(String message) {
        super("Don't support this type : "+message);
    }
}
