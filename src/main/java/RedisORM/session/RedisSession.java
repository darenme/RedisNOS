package RedisORM.session;

import java.util.List;

public interface RedisSession {

    public void save(Object object);

    public <T> T get(Class<T> clazz,Object id);

    public void delete(Object object);

    public void update(Object object);

    public void exec();

    public void save(List list);

}
