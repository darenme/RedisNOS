package RedisORM.useTest;


import RedisORM.Configuration;
import RedisORM.session.DefaultRedisSession;
import RedisORM.session.DefaultSessionFactory;
import RedisORM.session.RedisSession;
import RedisORM.session.SessionFactory;
import org.junit.Test;

import java.util.*;

public class ItemTest {

    @Test
    public void testCreate(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
    }

    @Test
    public void testSave1(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Teacher teacher = new Teacher(1001,"chen",60);
        Student student = new Student(1616002,"mzw",18,teacher);
        session.save(student);
    }

    @Test
    public void testGet1(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Student student = session.get(Student.class,1616002);
        System.out.println(student.getAge());
    }

    @Test
    public void testSave2(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Teacher teacher = new Teacher(1001,"chen",60);
        Student2 student = new Student2(1616002,"mzw",18,teacher);
        session.save(student);
    }

    @Test
    public void testGet2(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Student2 student = session.get(Student2.class,1616002);
        System.out.println(student);
    }

    @Test
    public void testSave3(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Teacher teacher1 = new Teacher(1001,"chen",60);
        Teacher teacher2 = new Teacher(1001,"yang",40);
        Student3 student = new Student3(1616002,"mzw",18,teacher1,teacher2);
        session.save(student);
    }

    @Test
    public void testGet3(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Student3 student = session.get(Student3.class,1616002);
        System.out.println(student.getAge());
        System.out.println(student.getName());
    }

    @Test
    public void testSave4(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Teacher teacher1 = new Teacher(1001,"chen",60);
        Teacher teacher2 = new Teacher(1002,"yang",40);
        Student4 student = new Student4(1616002,"mzw",18,teacher1,teacher2);
        session.save(student);
        session.exec();
    }

    @Test
    public void testGet4(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Student4 student = session.get(Student4.class,1616002);
        System.out.println(student.getAge());
        System.out.println(student.getName());
        System.out.println(student.getTeacher1());
    }

    @Test
    public void testCache(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Student4 student1 = session.get(Student4.class,1616002);
        Student4 student2 = session.get(Student4.class,1616002);
    }

    @Test
    public void testSaveList(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
        Teacher teacher1 = new Teacher(1001,"chen",60);
        Teacher teacher2 = new Teacher(1002,"yang",40);
        Teacher teacher3 = new Teacher(1003,"shi",30);

        List list = new ArrayList();
        list.add(teacher1);
        list.add(teacher2);
        list.add(teacher3);

        session.save(list);
        session.exec();
    }

    @Test
    public void deleteTest(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();
//        Student4 student4 = session.get(Student4.class,1616002);
//        session.delete(student4);
        OtherType ot = session.get(OtherType.class,111);
        session.delete(ot);
        session.exec();
    }

    @Test
    public void listTest(){
        Configuration configuration = new Configuration("Conf.xml");
        SessionFactory sessionFactory = new DefaultSessionFactory(configuration);
        RedisSession session = sessionFactory.opSession();

        OtherType ot = new OtherType(111);
        ot.setSignatute("hello world");

        List<String> language = new ArrayList<>();
        language.add("english");
        language.add("chinese");
        ot.setLanguage(language);

        Set<String> phones = new HashSet<>();
        phones.add("189");
        phones.add("136");
        ot.setPhones(phones);


        Set<String> edu = new LinkedHashSet<>();
        edu.add("jincheng");
        edu.add("nanhang");
        ot.setEducation(edu);

        session.save(ot);

        session.exec();
    }


}
