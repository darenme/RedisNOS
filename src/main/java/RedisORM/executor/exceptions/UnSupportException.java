package RedisORM.executor.exceptions;

public class UnSupportException extends RuntimeException{

    public UnSupportException(String message) {
        super("Don't support this type : "+message);
    }
}
