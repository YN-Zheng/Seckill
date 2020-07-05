package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.Order;
import cn.yongnian.seckill.model.SeckillOrder;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.service.GoodsService;
import cn.yongnian.seckill.service.OrderService;
import cn.yongnian.seckill.service.SeckillService;
import cn.yongnian.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/do_seckill")
    public synchronized String doSeckill(Model model, User user, @RequestParam("goodsId")long goodsId){

        // 判断是否登陆
        model.addAttribute("user",user);
        if(user==null){
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!no user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!no user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!no user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!no user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!no user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!no user!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            return "login";
        }

        // 判断库存是否足够
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if(goodsVo.getSeckillStock()<=0){
            model.addAttribute("msg",CodeMessage.NO_STOCK.getMsg());
            return "seckill_fail";
        }
        // 判断该用户是否已经秒杀过该商品
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodId(user.getId(),goodsId);
        if(order != null){
            model.addAttribute("msg",CodeMessage.REPEAT_SECKILL.getMsg());
            return "seckill_fail";
        }
        // 减库存, 下订单, 写入秒杀订单
        Order newOrder = seckillService.seckill(user,goodsVo);
        model.addAttribute("order",newOrder);
        model.addAttribute("goods",goodsVo);
        log.info(user.getNickname()+"---"+goodsVo.getSeckillStock());
        return "seckill_order_detail";

    }
}
