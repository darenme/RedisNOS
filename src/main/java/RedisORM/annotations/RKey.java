package RedisORM.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 类似与数据库的id,一次作为hash的key
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RKey {

}
