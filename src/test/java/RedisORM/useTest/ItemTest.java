package RedisORM.useTest;


import RedisORM.Configuration;
import RedisORM.session.RedisSession;
import org.junit.Test;

public class ItemTest {

    @Test
    public void testGet(){
        Configuration configuration = new Configuration("Conf.xml");
        RedisSession session = new RedisSession(configuration);
        Student student = session.get(Student.class,"1");
//        System.out.println(student);
//        System.out.println(student.getName());
//        System.out.println(student.getAge());
//        System.out.println(student.getTeacher());
//        System.out.println(student);
//        student.setAge(22);
        System.out.println(student);
    }

    @Test
    public void testSave(){
        Configuration configuration = new Configuration("Conf.xml");
        RedisSession session = new RedisSession(configuration);
        Teacher teacher = new Teacher(1001,"chen",60);
        Student student = new Student(1616002,"mzw",18,teacher);
        session.save(student);
    }

}
