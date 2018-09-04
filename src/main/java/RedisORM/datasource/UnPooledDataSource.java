package RedisORM.datasource;

import redis.clients.jedis.Jedis;

public class UnPooledDataSource implements DataSource{

    private String host;

    private int port;

    public UnPooledDataSource(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Jedis getJedis() {
        return new Jedis(host, port);
    }


}
