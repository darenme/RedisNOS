package RedisORM.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 这个用于hash类型，用于字段上，默认field为字段的名称.
 */


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RField {

    // 实际在Redis中的hash中的field名
    String field() default "";

    // 是否需要已存在
    boolean exist() default false;

}
