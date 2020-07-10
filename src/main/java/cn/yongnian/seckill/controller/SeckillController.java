package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.Order;
import cn.yongnian.seckill.model.SeckillOrder;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.rabbitmq.MQSender;
import cn.yongnian.seckill.rabbitmq.SeckillMessage;
import cn.yongnian.seckill.redis.GoodsKey;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.GoodsService;
import cn.yongnian.seckill.service.OrderService;
import cn.yongnian.seckill.service.SeckillService;
import cn.yongnian.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    MQSender mqSender;


    private Map<Long,Boolean> localOverMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * GET POST 有什么区别?
     * GET: 幂等, 从服务端获取数据, 不会对服务端产生影响
     */
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId) {

        // 判断是否登陆
        if (user == null) {
            return Result.error(CodeMessage.NOT_LOGIN);
        }
        // 内存标记, 减少Redis访问
        Boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMessage.NO_STOCK);
        }
        // 预减库存
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMessage.NO_STOCK);
        }
        // 判断该用户是否已经秒杀过该商品
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodId(user.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMessage.REPEAT_SECKILL);
        }
        // 入队
        SeckillMessage sm = new SeckillMessage();
        sm.setUser(user);
        sm.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(sm);
        return Result.success(0); //排队中



        /*
        // TODO 数据库已经保证数据一致性, 去除以下判断和数据库查询,保留错误信息
        // 判断库存是否足够
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if(goodsVo.getSeckillStock()<=0){
            return Result.error(CodeMessage.NO_STOCK);
        }
        // 判断该用户是否已经秒杀过该商品
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodId(user.getId(),goodsId);
        if(order != null){
            return Result.error(CodeMessage.REPEAT_SECKILL);
        }
        // 减库存, 下订单, 写入秒杀订单
        Order newOrder = seckillService.seckill(user,goodsVo);
        log.info(user.getNickname()+"---"+goodsVo.getSeckillStock());
        return Result.success(newOrder);
        */

    }

    /**
     *
     * @param model
     * @param user
     * @param goodsId
     * @return orderId: 成功   1: 秒杀失败 0: 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, User user, @RequestParam("goodsId") long goodsId) {

        // 判断是否登陆
        if (user == null) {
            return Result.error(CodeMessage.NOT_LOGIN);
        }
        // 判断用户是否秒杀到商品
        long result = seckillService.getSeckillResult(user.getId(),goodsId);
        return Result.success(result);
    }


    /**
     * 系统初始化, 将商品的秒杀库存加载至redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), goods.getSeckillStock());
            localOverMap.put(goods.getId(),false);
        }
    }
}
