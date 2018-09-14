package RedisORM.executor.op;

/**
 * 这个接口定义每种数据类型的处理接口，所有的数据经过这一层的加工处理交付给下一层。
 */

public interface OP {

    /**
     * @Description: 数据的操作
     * @Date 2018/9/12 15:49
     * @param object 使用的操作工具，可以是Transaction,也可以是Jedis
     * @param objects 操作数据相关信息的集合，包括key、field、value,因为每一种操作所需的数据量和类型都是不完全相同的
     * @return 返回数据操作后的结果
     */
    public Object op(Object object, Object... objects);

}
