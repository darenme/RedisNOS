package RedisORM.useTest;

import RedisORM.annotations.RField;
import RedisORM.annotations.RHash;
import RedisORM.annotations.RKey;

@RHash(id="teacher")
public class Teacher {

    @RKey
    private int id;

    @RField
    private String name;

    @RField
    private int age;

    public Teacher() {
    }

    public Teacher(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
