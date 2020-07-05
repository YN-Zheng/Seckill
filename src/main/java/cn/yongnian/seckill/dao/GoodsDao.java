package cn.yongnian.seckill.dao;

import cn.yongnian.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TODO
 */
@Mapper
public interface GoodsDao {

    @Select("select g.* sg.stock, sg.start_date, mg.end_date" +
            "from seckill_goods sg left join goods g on sg.goods_id = g.id ")
    public List<GoodsVo> listGoodsVo();
}
