package RedisORM.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.util.HashSet;
import java.util.Set;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RSet {

    String key() default "";

    boolean sorted() default false;

    Class<? extends Set> javaType() default HashSet.class;

    boolean exist() default false;
}
