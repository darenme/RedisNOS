package RedisORM.JedisUseTest;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import java.io.IOException;

public class TransactionTest {

    /*
     在事务中，一个命令的提交是不能立即获得结果，需要等到所有的命令都提交
     也就是exec()函数执行完后才能获得结果。

     事务返回的是Response类型，在执行exec()函数后，可以通过它的get()函数取得返回值
    */

    @Test
    public void JedisDataException(){
        Jedis jedis = new Jedis("localhost", 6379);

        Transaction transaction = jedis.multi();

        // 错误的写法
//        String s = transaction.get("t1").get();//redis.clients.jedis.exceptions.JedisDataException: Please close pipeline or multi block before calling this method.

        Response s = transaction.get("t1");

        transaction.exec();

        System.out.println(s.get());

        try {
            transaction.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jedis.close();
    }

    @Test
    public void test(){

    }
}
