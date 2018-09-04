package RedisORM.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RString {
    // 默认为格式为 key.RString
    // 如果设置了，则前面不会加上key
    String key() default "";

    boolean exist() default false;
}
