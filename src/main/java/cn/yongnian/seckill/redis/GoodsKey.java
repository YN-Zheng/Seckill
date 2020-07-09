package cn.yongnian.seckill.redis;

/**
 * TODO
 */
public class GoodsKey extends BasePrefix {

    // 页面缓存时间：60s
    public static final int EXPIRE = 60;

    // 0 代表永不过期
    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(EXPIRE, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(EXPIRE, "gd");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(EXPIRE, "gs");


}
