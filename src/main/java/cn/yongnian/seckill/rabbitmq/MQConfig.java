package cn.yongnian.seckill.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Configuration
public class MQConfig {

    public static final String QUEUE="queue";

    @Bean
    public Queue queue(){
        return new Queue(QUEUE,true);
    }
}
