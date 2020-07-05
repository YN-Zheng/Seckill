package cn.yongnian.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 保存至数据库前,对用户输入的密码明文做两次MD5
 * 1. 用户端: PASS = MD5(  明文 + 固定 SALT)
 * 2. 服务端: PASS = MD5(  用户输入 + 随机 SALT)
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static String passwordToMd5(String password){
        String s = ""+salt.charAt(0) + salt.charAt(2) + password + salt.charAt(5) + salt.charAt(4);
        return md5(s);
    }
    public static String md5ToDBPassword(String password,String salt){
        String s = ""+salt.charAt(0) + salt.charAt(2) + password + salt.charAt(5) + salt.charAt(4);
        return md5(s);
    }

    public static String passwordToDBPassword(String input, String saltDB){
        String s = passwordToMd5(input);
        String dbPassword = md5ToDBPassword(s, saltDB);
        return dbPassword;
    }

    public static void main(String[] args) {
        System.out.println(passwordToMd5("123456"));   // d3b1294a61a07da9b49b6e22b2cbd7f9
    }
}
