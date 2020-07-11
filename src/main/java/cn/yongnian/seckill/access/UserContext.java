package cn.yongnian.seckill.access;

import cn.yongnian.seckill.model.User;

/**
 * 使用ThreadLocal来保存User
 * 每个线程的本地变量,线程安全
 */
public class UserContext {
    //TODO 深入了解ThreadLocal
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }

}
