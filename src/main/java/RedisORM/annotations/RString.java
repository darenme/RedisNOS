package RedisORM.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解在字段上，表明这个字段是共有的，在保存在Redis中时以String类型保存，而不是hash类型中的一个field
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RString {
    // 默认为格式为 key.RString
    // 如果设置了，则前面不会加上key
    String key() default "";

    // 是否需要已存在
    boolean exist() default false;
}
