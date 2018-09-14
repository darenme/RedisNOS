package RedisORM.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.util.HashSet;
import java.util.Set;

/**
 * 注解在字段上，表示这个字段以set类型保存在Redis中
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RSet {

    // 保存时使用的key
    String key() default "";

    // 是否是由于的set
    boolean sorted() default false;

    // 字段的实际类型
    Class<? extends Set> javaType() default HashSet.class;

    // 是否需要存在
    boolean exist() default false;
}
