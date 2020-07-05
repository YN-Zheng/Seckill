package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public abstract class BasePrefix implements KeyPrefix {


    private int expireSeconds;
    private String prefix;

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
    public BasePrefix(String prefix) {
        this.expireSeconds = 0;
        this.prefix = prefix;
    }

    //默认0代表永不过期
    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }
    //
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
