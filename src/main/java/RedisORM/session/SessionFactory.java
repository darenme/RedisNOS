package RedisORM.session;

import RedisORM.cache.Cache;

/**
 * 会话工厂接口
 */
public interface SessionFactory {

    /**
     * @Description: 获取一个会话
     * @Date 2018/9/12 22:56
     * @return 返回一个会话
     */
    public RedisSession opSession();

    /**
     * @Description: 获取是否缓存是只读
     * @Date 2018/9/12 22:56
     * @return
     */
    public boolean isReadonly();

    /**
     * @Description: 获取缓存
     * @Date 2018/9/12 22:56
     * @return 返回缓存
     */
    public Cache getCache();

}
