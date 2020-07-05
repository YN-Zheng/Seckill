/*
package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.User;
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

*/
/**
 * TODO
 *//*

@RequestMapping("/demo")
@Controller
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;
    // 返回界面
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Joshua");
        return "hello";
    }

*/
/*
    // 返回对象消息
    @RequestMapping("/hello/success")
    @ResponseBody
    public Result<String> hello(){
        return Result.error(CodeMessage.SAMPLE_SUCCESS);
    }


    @RequestMapping("/hello/error")
    @ResponseBody
    public Result<String> error(){
        return Result.error(CodeMessage.SAMPLE_ERROR);
    }
*//*


    //测试Mybatis
    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user1 = userService.getById(18672394517L);
        return success(user1);
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
*/
