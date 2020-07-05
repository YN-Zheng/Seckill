package cn.yongnian.seckill.mapper;

import cn.yongnian.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TODO
 */
public interface GoodsExtMapper {
    public List<GoodsVo> listGoodsVo();
}
