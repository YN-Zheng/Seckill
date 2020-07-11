package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public class AccessKey extends BasePrefix{

    public AccessKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }
    public static AccessKey access = new AccessKey(5,"access");
    public static AccessKey accessWithExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }

}
