package cn.yongnian.seckill.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AccessLimit注释。
 * 基于Redis，限制接口的访问频率
 * seconds: 刷新周期
 * maxCount: 周期内访问的最大次数
 * needLogin: 限定用户登陆才可否访问
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    int seconds();
    int maxCount();
    boolean needLogin() default true;
}
