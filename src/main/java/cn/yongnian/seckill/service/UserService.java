package cn.yongnian.seckill.service;

import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.mapper.UserMapper;
import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.model.UserExample;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.UserKey;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.utils.MD5Util;
import cn.yongnian.seckill.utils.UUIDUtil;
import cn.yongnian.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * TODO
 */
@Service
public class UserService {
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public User getById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }


    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        // 将错误信息直接抛出。在GlobalExceptionHandler中处理并返回错误信息到前端
        if (loginVo == null) {
            throw new GlobalException(CodeMessage.SERVER_ERROR);
        }

        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
         // 登陆
        User user = userMapper.selectByPrimaryKey(Long.valueOf(mobile));
        if (user == null) {
            throw new GlobalException(CodeMessage.MOBILE_NOTFOUND);
        }
        // 验证密码
        String dbPassword = user.getPassword();
        String dbSalt = user.getSalt();
        // MD5
        String expectDBPassword = MD5Util.md5ToDBPassword(password, dbSalt);
        if (!expectDBPassword.equals(dbPassword)) {
            throw new GlobalException(CodeMessage.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response, user, token);

        return true;
    }

    private void addCookie(HttpServletResponse response, User user,String token) {
        // 随机生成token作为cookie的value
        // 生成cookie, 并保存至redis中
        redisService.set(UserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.getExpireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public User getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        //延长有效期
        User user = redisService.get(UserKey.token, token, User.class);
        if(user!=null){
            addCookie(response,user,token);
        }
        return user;
    }


    public void insertProducedUser(User user) {
        int i = userMapper.insertSelective(user);
    }

    public User selectUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
