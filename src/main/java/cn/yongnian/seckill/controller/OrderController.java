package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.Order;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.GoodsService;
import cn.yongnian.seckill.service.OrderService;
import cn.yongnian.seckill.vo.GoodsVo;
import cn.yongnian.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 */
@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, User user,
                                      @RequestParam("orderId") long orderId) {
        if(user==null){
            return Result.error(CodeMessage.NOT_LOGIN);
        }
        // TODO check whether the order belongs to the user
        Order order = orderService.getById(orderId);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo info = new OrderDetailVo();
        info.setGood(goods);
        info.setOrder(order);
        return Result.success(info);
    }
}
