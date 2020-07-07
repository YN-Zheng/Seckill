package cn.yongnian.seckill.service;

import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.mapper.OrderMapper;
import cn.yongnian.seckill.mapper.SeckillOrderMapper;
import cn.yongnian.seckill.model.*;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * TODO
 */
@Service
public class OrderService {

    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    @Autowired
    OrderMapper orderMapper;



    public SeckillOrder getSeckillOrderByUserIdAndGoodId(Long userId, long goodsId) {
        SeckillOrderExample orderExample = new SeckillOrderExample();
        orderExample.createCriteria().andUserIdEqualTo(userId).andGoodsIdEqualTo(goodsId);
        List<SeckillOrder> seckillOrders = seckillOrderMapper.selectByExample(orderExample);
        if(seckillOrders==null || seckillOrders.isEmpty()){
            return null;
        }
        return seckillOrders.get(0);
    }



//    @Transactional
    public Order createOrder(User user, GoodsVo goodsVo) {
        Order order = new Order();
        order.setDeliveryAddressId(0L);
        order.setGmtCreate(new Date());
        order.setGmtPay(new Date());
        order.setGoodsCount(1);
        order.setGoodsId(goodsVo.getId());
        order.setGoodsName(goodsVo.getName());
        order.setGoodsPrice(goodsVo.getSeckillPrice());
        order.setOrderChannel((byte) 0);
        order.setStatus((byte) 0); // 新建 未支付
        order.setUserId(user.getId());

        int num = orderMapper.insertSelective(order);
        Long id = order.getId();

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(id);
        seckillOrder.setUserId(user.getId());

        seckillOrderMapper.insertSelective(seckillOrder);
        return order;
    }

    public Order getById(long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }
}
