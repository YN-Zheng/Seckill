package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public class SeckillKey extends BasePrefix{


    // 永不过期
    public SeckillKey(String prefix){
        super(prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");



}
