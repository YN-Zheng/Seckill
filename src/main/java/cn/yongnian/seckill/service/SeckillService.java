package cn.yongnian.seckill.service;

import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.model.Goods;
import cn.yongnian.seckill.model.Order;
import cn.yongnian.seckill.model.SeckillOrder;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.SeckillKey;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * TODO
 */
@Service
public class SeckillService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;


    @Transactional
    public Order seckill(User user, GoodsVo goodsVo) {
        // 减库存 下订单
        boolean success = goodsService.reduceStock(goodsVo);
        // 写入秒杀订单
        if(success) {
            return orderService.createOrder(user, goodsVo);
        }else{
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }



    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodId(userId, goodsId);
        if(order != null){
            //秒杀成功
            return order.getOrderId();
        }else if(getGoodsOver(goodsId)){
            // 卖完了 没抢到
            return -1;
        }
        return 0;
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver,""+goodsId,true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exist(SeckillKey.isGoodsOver,""+goodsId);
    }
}
