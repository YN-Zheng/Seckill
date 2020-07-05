package cn.yongnian.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * TODO
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.yongnian.seckill.mapper")
public class SeckillSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillSystemApplication.class,args);
    }

}
