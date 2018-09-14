package RedisORM.useTest;

import RedisORM.annotations.RField;
import RedisORM.annotations.RHash;
import RedisORM.annotations.RKey;

@RHash(id="student")
public class Student {

    @RKey
    private  int id;

    @RField
    private String name;

    @RField
    private int age;

    @RHash(id="teacher")
    private Teacher teacher;

    public Student() {
    }

    public Student(int id, String name, int age, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.teacher = teacher;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", teacher=" + teacher +
                '}';
    }
}

