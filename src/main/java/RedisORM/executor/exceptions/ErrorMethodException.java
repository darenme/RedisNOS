package RedisORM.executor.exceptions;

/**
 * 构造执行器时可能发生的异常：错误的方法
 */
public class ErrorMethodException extends RuntimeException{

    public ErrorMethodException(String message) {
        super(message);
    }
}
