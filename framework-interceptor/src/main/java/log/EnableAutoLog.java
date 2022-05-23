package log;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author tao.xiong
 * @Description 自动日志注解
 * @Date 2022/5/13 15:12
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({LogConfigurer.class})
public @interface EnableAutoLog {
    //nothing
}
