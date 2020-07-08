package cn.yongnian.seckill.mapper;

import cn.yongnian.seckill.model.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * TODO
 */
@Mapper
public interface SeckillGoodsExtMapper {

    @Update("update seckill_goods set seckill_stock = seckill_stock-1 where goods_id = #{goodsId} and seckill_stock > 0")
    public int reduceStock(SeckillGoods g);
}
