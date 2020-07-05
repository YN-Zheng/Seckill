package cn.yongnian.seckill.utils;

import java.util.UUID;

/**
 * TODO
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
