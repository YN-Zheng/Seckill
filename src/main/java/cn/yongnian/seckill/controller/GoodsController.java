package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.GoodsKey;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.GoodsService;
import cn.yongnian.seckill.service.UserService;
import cn.yongnian.seckill.vo.GoodsDetailVo;
import cn.yongnian.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 主要负责:
 * 1. 商品列表(to_list)：缓存页面至Redis。60秒刷新一次
 * 2. 商品详情(to_detail/id)：前后端分离，静态页面，浏览器缓存。通过AJAX异步查询数据，减少流量
 *
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;


    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    // MODEL and User are both from ArgumentResolver
    public String toList(Model model, User user,
                         HttpServletResponse response,
                         HttpServletRequest request
    ) {
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goods", goodsVos);

        //手动渲染
        IContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", webContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodId}")
    @ResponseBody
    public Result<GoodsDetailVo> toGoodDetail(User user, @PathVariable("goodId") long goodsId) {


        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        long startTime = goodsVo.getStartDate().getTime();
        long endDateTime = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int status ;
        int remainSeconds;
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
        GoodsDetailVo goods = new GoodsDetailVo();
        goods.setGoods(goodsVo);
        goods.setRemainSeconds(remainSeconds);
        goods.setStatus(status);
        goods.setUser(user);
        return Result.success(goods);
    }

/*
    @RequestMapping(value = "/to_detail2/{goodId}", produces = "text/html")
    @ResponseBody
    public String toGoodDetail2(Model model, User user,
                                HttpServletResponse response,
                                HttpServletRequest request,
                                @PathVariable("goodId") long goodsId) {
//        snowflake算法
        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }


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


        //手动渲染
        IContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", webContext);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;

    }
*/

}
