package RedisORM.Demo;

import RedisORM.annotations.*;
import java.io.Serializable;
import java.util.List;


// 若以@RSerialize(id="teacher") 来注解表示这个类使用序列化的方式保存
// 以RHash来注解类，表明这个类在Redis中以一个hash类型保存
@RHash(id="teacher")
public class Teacher implements Serializable{

    // 这个字段注解为RKey,表示这个字段是主键，以id为key,以其值为value作为一个键值对保存到Redis的一个hash类型中
    @RKey
    private int id;

    // 这个字段注解为RField,表示这个字段映射为Redis中hash类型中的键值对
    @RField
    private String name;

    @RField
    private int age;

    // 这个字段注解为RList,表示这个字段在Redis中以list类型来保存
    @RList
    private List<String> research;

    public Teacher() {
    }

    public Teacher(int id, String name, int age, List<String> research) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.research = research;
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

    public List<String> getResearch() {
        return research;
    }

    public void setResearch(List<String> research) {
        this.research = research;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", research=" + research +
                '}';
    }
}
