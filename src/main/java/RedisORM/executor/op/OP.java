package RedisORM.executor.op;

import redis.clients.jedis.Jedis;

public interface OP {

    public Object op(Object object, Object... objects);


}
