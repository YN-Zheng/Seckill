package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.rabbitmq.MQReceiver;
import cn.yongnian.seckill.rabbitmq.MQSender;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.UserKey;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static cn.yongnian.seckill.result.Result.success;



@RequestMapping("/demo")
@Controller
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    @Autowired
    MQReceiver mqReceiver;


    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header(){
        mqSender.sendHeader("hello, immoc");
        return Result.success(null);
    }

    //TODO 接口测试: swagger
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        mqSender.sendFanout("hello, immoc");
        return Result.success(null);
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){
        mqSender.sendTopic("hello, immoc");
        return Result.success(null);
    }


    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello, immoc");
        return Result.success(null);
    }




    // 返回界面
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setNickname("小明");
        user.setId(15871476227L);

        boolean result = redisService.set(UserKey.getById,""+1, user);
        return Result.success(result);
    }


}
