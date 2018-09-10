package RedisORM.useTest;

import RedisORM.annotations.RField;
import RedisORM.annotations.RHash;
import RedisORM.annotations.RKey;
import RedisORM.annotations.RSerialize;

import java.io.Serializable;

@RSerialize(id="student2")
public class Student2 implements Serializable{
    @RKey
    private  int id;

    @RField
    private String name;

    @RField
    private int age;

    @RSerialize(id="teacher")
    private Teacher teacher;

    public Student2(int id, String name, int age, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.teacher = teacher;
    }

    public Student2() {
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
        return "Student2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", teacher=" + teacher +
                '}';
    }
}
