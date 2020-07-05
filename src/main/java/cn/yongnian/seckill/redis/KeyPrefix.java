package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public interface KeyPrefix {
    public int getExpireSeconds();
    public String getPrefix();
}
