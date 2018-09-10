package RedisORM.useTest;

import RedisORM.annotations.RField;
import RedisORM.annotations.RHash;
import RedisORM.annotations.RKey;
import RedisORM.annotations.RSerialize;

import java.io.Serializable;

@RHash(id="student3")
public class Student3 implements Serializable{
    @RKey
    private  int id;

    @RField
    private String name;

    @RField
    private int age;

    @RSerialize(id="teacher")
    private Teacher teacher1;

    @RHash(id="teacher")
    private Teacher teacher2;

    public Student3(int id, String name, int age, Teacher teacher1, Teacher teacher2) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
    }

    public Student3() {
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


    @Override
    public String toString() {
        return "Student3{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", teacher1=" + teacher1 +
                ", teacher2=" + teacher2 +
                '}';
    }
}
