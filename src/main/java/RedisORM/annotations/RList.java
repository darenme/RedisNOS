package RedisORM.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解在字段上，表示这个字段映射为list类型
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RList {

    // 映射在Redis中使用的key
    String key() default "";

    // 这个字段的实际类型
    Class<? extends List> javaType() default ArrayList.class;

    // 是否需要已存在
    boolean exist() default false;

}
