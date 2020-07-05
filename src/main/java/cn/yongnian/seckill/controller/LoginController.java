package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.UserKey;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.UserService;
import cn.yongnian.seckill.utils.ValidatorUtil;
import cn.yongnian.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static cn.yongnian.seckill.result.Result.success;

/**
 * 登陆 Controller
 * TODO @Valid?
 */

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        // loginVo 参数校验
        // 通过注解绑定的有: 非空, 是否为标准手机号
        boolean login = userService.login(response, loginVo);
        return Result.success(login); // true

    }


}
