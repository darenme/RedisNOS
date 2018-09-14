package RedisORM.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在类或字段上，表明这个类或者字段是否序列化保存
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RSerialize {

    // 如果是注解在类上，id表示这个映射的id
    // 如果是注解在字段上，id表示这个字段在进行映射时使用的是那个类的映射规则
    String id();
}
