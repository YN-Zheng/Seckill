package cn.yongnian.seckill.service;

import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.model.Goods;
import cn.yongnian.seckill.model.Order;
import cn.yongnian.seckill.model.SeckillOrder;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.SeckillKey;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.utils.MD5Util;
import cn.yongnian.seckill.utils.UUIDUtil;
import cn.yongnian.seckill.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

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
        }else if(goodsService.getGoodsVoByGoodsId(goodsId).getSeckillStock()==0){
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

    public String createSeckillPath(Long userId, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SeckillKey.getPath, "" + userId + "_" + goodsId, str);
        return str;
    }
    public boolean checkPath(Long userId, long goodsId, String path) {
        if(path==null){
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getPath, "" + userId + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    public BufferedImage createSeckillVerifyCode(User user, long goodsId) {
        if(user==null || goodsId <=0){
            return null;
        }
        int width = 80;
        int height = 32;

        // 创建背景
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 设置背景色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width,height);
        // 设置轮廓
        g.setColor(Color.BLACK);
        g.drawRect(0,0,width-1,height-1);
        // 随机数
        Random rdm  = new Random();
        // 干扰点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x,y,0,0);
        }
        // 产生随机code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0,100,0));
        g.setFont(new Font("Candara",Font.BOLD,24));
        g.drawString(verifyCode,8,24);
        g.dispose();

        // 验证码结果存入Redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getVerifyCode,user.getId()+","+goodsId,rnd);

        //输出图片
        return image;

    }

    private int calc(String exp) {
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[]{'+','-','*'};
    /**
     * 生成随机算式
     * + - *
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 秒杀验证
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(User user, long goodsId, Integer verifyCode) {
        if(user==null || goodsId <=0 || verifyCode == null){
            return false;
        }
        Integer oldVerifyCode = redisService.get(SeckillKey.getVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if(oldVerifyCode == null || oldVerifyCode - verifyCode !=0){
            return false;
        }
        // 验证成功, 在redis中清掉缓存
        redisService.delete(SeckillKey.getVerifyCode, user.getId() + "," + goodsId);
        return true;
    }
}
