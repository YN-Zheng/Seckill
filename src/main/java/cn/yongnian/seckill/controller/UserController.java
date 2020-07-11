package cn.yongnian.seckill.controller;

import cn.yongnian.seckill.model.User;
import cn.yongnian.seckill.redis.RedisService;
import cn.yongnian.seckill.redis.UserKey;
import cn.yongnian.seckill.result.Result;
import cn.yongnian.seckill.service.UserService;
import cn.yongnian.seckill.utils.MD5Util;
import cn.yongnian.seckill.utils.UUIDUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User Controller
 * 辅助测试用，包括：
 * 1.生成5000个用户，密码为123456
 * 2. 更新这些用户的token
 * ！！！！！！不要将该接口暴露！！！！！！！！！！！
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/info")
    @ResponseBody
    public Result<User> info(User user) {
        return Result.success(user);
    }

/*
    @RequestMapping("/produce_user")
    @ResponseBody
    public Result<List<User>> produce(Model model) {
        int produceNum = 5000;
        List<User> users = new ArrayList<>(produceNum);
        for (int i = 0; i < produceNum; i++) {
            User user = new User();
            user.setId(10000000000L + i);
            user.setGmtModified(new Date());
            user.setGmtCreate(new Date());
            user.setNickname("小明" + i + "号");
            String password = "123456";
            String dbPassword;
            String dbSalt = RandomStringUtils.randomAlphanumeric(8);
            String md5 = MD5Util.passwordToMd5(password);
            dbPassword = MD5Util.md5ToDBPassword(md5, dbSalt);
            user.setPassword(dbPassword);
            user.setSalt(dbSalt);
            userService.insertProducedUser(user);
            users.add(user);
            log.info("produce:"+user.getNickname());
            String token = UUIDUtil.uuid();
            redisService.set(UserKey.token,token,user);
        }
        return Result.success(users);
    }
*/


    /**
     * 生成token,用于Jmeter测试
     * @return
     */
/*    @RequestMapping("/update_token")
    @ResponseBody
    public Result<List<User>> update() {
        int produceNum = 5000;
        List<User> users = new ArrayList<>(produceNum);

        // 写入Txt文件
        File writename = new File("./token.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
        BufferedWriter out;
        try {
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));

            for (int i = 0; i < produceNum; i++) {
                Long id = 10000000000L + i;

                String token = UUIDUtil.uuid();

                User user = userService.selectUserById(id);
                users.add(user);
                log.info("update:" + user.getNickname() + "---" + token);
                redisService.set(UserKey.token, token, user);
                out.write(id + "," + token + "\r\n"); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
            out.close(); // 最后记得关闭文件

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success(users);
    }
*/
}
