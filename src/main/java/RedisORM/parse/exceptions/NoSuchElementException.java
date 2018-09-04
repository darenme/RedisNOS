package RedisORM.parse.exceptions;

public class NoSuchElementException extends RuntimeException{

    public NoSuchElementException(String message) {
        super(message+" is not exist!");
    }
}
