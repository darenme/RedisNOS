package RedisORM.executor.op;

import redis.clients.jedis.Jedis;

public interface OP {

    public Object op(Jedis jedis, Object... objects);
}
