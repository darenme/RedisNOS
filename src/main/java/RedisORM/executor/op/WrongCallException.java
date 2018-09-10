package RedisORM.executor.op;

public class WrongCallException extends RuntimeException{

    public WrongCallException(String message) {
        super(message);
    }
}
