package cn.yongnian.seckill.access;

import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.AccessKey;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 拦截器, 用于AccessLimit注解
 *
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){

            User user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return true;
            }

            // 并发限制参数
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if(needLogin){
                if(user == null){
                    render(response,CodeMessage.SESSION_ERROR);
                    return false;
                }
                key +="_"+user.getId();
            }
            //TODO martine flower, 重构-改善既有代码的设计
            Integer count = redisService.get(AccessKey.access, key, Integer.class);
            AccessKey ak = AccessKey.accessWithExpire(seconds);
            if(count==null){
                redisService.set(ak,key,1);
            }else if(count < maxCount){
                redisService.incr(ak,key);
            }else{
                render(response,CodeMessage.ACCESS_LIMIT_REACH);
                return false;
            }

        }
        return super.preHandle(request, response, handler);
    }

    private void render(HttpServletResponse response,CodeMessage cm) throws Exception{
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String s = JSON.toJSONString(Result.error(cm));
        out.write(s.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response){

        // paramToken：从request的parameter里拿,或者从request的cookies里拿
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        // 若没有用户额cookie,跳转至登录页面
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        // 优先选择paramToken
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        // 通过token获取用户信息, 并更新cookie
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length==0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieNameToken)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
