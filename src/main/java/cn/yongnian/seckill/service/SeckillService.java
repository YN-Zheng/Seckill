package cn.yongnian.seckill.service;

import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.model.Goods;
import cn.yongnian.seckill.model.Order;
import cn.yongnian.seckill.model.SeckillOrder;
import cn.yongnian.seckill.model.User;
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
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;


    @Transactional
    public Order seckill(User user, GoodsVo goodsVo) {
        // 减库存 下订单
        goodsService.reduceStock();
        // 写入秒杀订单
        return orderService.createOrder(user,goodsVo);
    }
}
