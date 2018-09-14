package RedisORM.session;

import java.util.List;

/**
 * 会话接口
 */
public interface RedisSession {

    /**
     * @Description: 保存对象
     * @Date 2018/9/12 22:52
     * @param object 需要被保存的对象
     * @return
     */
    public void save(Object object);

    /**
     * @Description: 获取数据
     * @Date 2018/9/12 22:53
     * @param clazz 获取的类型
     * @param id 数据的id
     * @return
     */
    public <T> T get(Class<T> clazz,Object id);

    /**
     * @Description: 删除数据
     * @Date 2018/9/12 22:53
     * @param object 要删除的对象
     * @return
     */
    public void delete(Object object);

    /**
     * @Description: 更新数据
     * @Date 2018/9/12 22:54
     * @param object 需要更新的对象
     * @return
     */
    public void update(Object object);

    /**
     * @Description: 提交事务
     * @Date 2018/9/12 22:55
     * @return
     */
    public void exec();

    /**
     * @Description: 保存多个对象
     * @Date 2018/9/12 22:55
     * @param list 需要保存对象的列表
     * @return
     */
    public void save(List list);

}
