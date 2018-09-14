package RedisORM.proxy;

import net.sf.cglib.proxy.MethodInterceptor;

public interface LazyProxy extends MethodInterceptor {

    public Object getProxyInstance();

    public Object getObject();
}
