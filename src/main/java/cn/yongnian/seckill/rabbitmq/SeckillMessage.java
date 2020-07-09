package cn.yongnian.seckill.rabbitmq;

import cn.yongnian.seckill.model.User;
import lombok.Data;

/**
 * TODO
 */
@Data
public class SeckillMessage {
    private User user;
    private long goodsId;

}
