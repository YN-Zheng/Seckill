package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public class SeckillKey extends BasePrefix{


    // 永不过期
    public SeckillKey(String prefix){
        super(prefix);
    }
    public SeckillKey(int expireSeconds, String prefix){
        super(expireSeconds,prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");
    public static SeckillKey getPath = new SeckillKey(60,"mp");
    public static SeckillKey getVerifyCode = new SeckillKey(300,"vc");


}
