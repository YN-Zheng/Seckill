package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.UserKey;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.GoodsService;
import cn.yongnian.seckill.service.UserService;
import cn.yongnian.seckill.vo.GoodsVo;
import cn.yongnian.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 登陆 Controller
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    // MODEL and User are both from ArgumentResolver
    public String toList(Model model, User user) {
        if(user==null){
            return "login";
        }
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goods", goodsVos);
//        System.out.println(System.currentTimeMillis());
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodId}")
    public String toGoodDetail(Model model, User user,
                               @PathVariable("goodId") long goodsId) {
//        snowflake算法
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startTime = goodsVo.getStartDate().getTime();
        long endDateTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int status = 0;
        int remainSeconds = 0;
        if (now < startTime) {
            //还没到时间,倒计时
            status = 0;
            remainSeconds = (int) (startTime - now) / 1000;
        } else if (now > endDateTime) {
            status = 2;
            //时间已过
            remainSeconds = -1;
        } else {
            status = 1;
            remainSeconds = 0;
            //秒杀进行中
        }
        model.addAttribute("goods", goodsVo);
        model.addAttribute("status", status);
        model.addAttribute("remainSeconds", remainSeconds);


        return "goods_detail";
    }


}
