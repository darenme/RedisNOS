package RedisORM.Demo;

import RedisORM.annotations.RField;
import RedisORM.annotations.RHash;
import RedisORM.annotations.RKey;
import RedisORM.annotations.RSerialize;
import java.io.Serializable;

// 以RHash来注解类，表明这个类在Redis中以一个hash类型保存
@RHash(id="student")
public class Student implements Serializable{
    // 这个字段注解为RKey,表示这个字段是主键，以id为key,以其值为value作为一个键值对保存到Redis的一个hash类型中
    @RKey
    private  int id;

    // 这个字段注解为RField,表示这个字段映射为Redis中hash类型中的键值对
    @RField
    private String name;

    @RField
    private int age;

    // 这个字段注解为RHash,表明这个字段需要使用一个hash类型来保存
    @RHash(id="teacher")
    private Teacher teacher1;

    // 这个字段注解为RSerialize,表明这个字段使用序列化的方式来保存，以键值对的方式保存在hash类型中
    @RSerialize(id="teacher")
    private Teacher teacher2;

    public Student() {
    }

    public Student(int id, String name, int age, Teacher teacher1, Teacher teacher2) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
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
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", teacher1=" + teacher1 +
                ", teacher2=" + teacher2 +
                '}';
    }
}

