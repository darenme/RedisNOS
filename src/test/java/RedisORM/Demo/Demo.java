package RedisORM.Demo;

import RedisORM.Configuration;
import RedisORM.session.DefaultSessionFactory;
import RedisORM.session.RedisSession;
import RedisORM.session.SessionFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yuanzijue1992
 * @Date: Created in 21:17 2018/9/13
 */
public class Demo {

    public static void main(String[] args) {

        // 创建配置类
        Configuration configuration = new Configuration("Demo.xml");

        // 创建会话工厂
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        // 创建会话
        RedisSession session = sessionFactory.opSession();

//        // 创建teacher1对象
//        List<String> research1 = new ArrayList<>();
//        research1.add("Theorem proving");
//        research1.add("fomal methods");
//        research1.add("plc");
//        Teacher teacher1 = new Teacher(1001,"chen",60,research1);
//
//        // 创建teacher2对象
//        List<String> research2 = new ArrayList<>();
//        research2.add("ai");
//        research2.add("big data");
//        Teacher teacher2 = new Teacher(1002,"wei",40,research2);
//
//        // 创建student对象
//        Student student = new Student(1616002,"mzw",26,teacher1,teacher2);
//
//        // 执行保存操作
//        session.save(student);
//
//        // 提交事务
//        session.exec();

        // 获取对象
        Student student = session.get(Student.class,1616002);

        // 打印对象，查看数据
        System.out.println(student);

        session.exec();

    }

}
