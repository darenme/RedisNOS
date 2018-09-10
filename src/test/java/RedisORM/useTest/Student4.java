package RedisORM.useTest;


import java.io.Serializable;

public class Student4 implements Serializable{

    private  int id;

    private String name;

    private int age;

    private Teacher teacher1;

    private Teacher teacher2;

    public Student4(int id, String name, int age, Teacher teacher1, Teacher teacher2) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
    }

    public Student4() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Teacher getTeacher1() {
        return teacher1;
    }

    public void setTeacher1(Teacher teacher1) {
        this.teacher1 = teacher1;
    }

    public Teacher getTeacher2() {
        return teacher2;
    }

    public void setTeacher2(Teacher teacher2) {
        this.teacher2 = teacher2;
    }

}
