package cn.yongnian.seckill.rabbitmq;

import cn.yongnian.seckill.model.SeckillOrder;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.service.GoodsService;
import cn.yongnian.seckill.service.OrderService;
import cn.yongnian.seckill.service.SeckillService;
import cn.yongnian.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Service
public class MQReceiver {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillService seckillService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSeckill(String message) {
        log.info("receive message:" + message);
        SeckillMessage sm = RedisService.StringToBean(message, SeckillMessage.class);
        User user = sm.getUser();
        long goodsId = sm.getGoodsId();

        // 减库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getSeckillStock();
        if(stock<=0){
            return;
        }
        // 判断是否已经秒杀到了 TODO: 改成Redis
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodId(user.getId(), goodsId);
        if(order!=null){
            return;
        }
        seckillService.seckill(user,goods);


    }


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String message) {
        log.info("topic queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String message) {
        log.info("topic queue2 message:" + message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeader(byte[] message) {
        log.info("header queue message:" + new String(message));
    }
}
