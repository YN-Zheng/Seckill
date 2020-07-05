package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPIRE = 3600*24*2;
    // 0 代表永不过期
    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE,"tk");



}
