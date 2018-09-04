package RedisORM.session;

public interface Session{

    public void save(Object object);

    public <T> T get(Class<T> clazz,String id);

    public void delete(Object object);

    public void update(Object object);

}
