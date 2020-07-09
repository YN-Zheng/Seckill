package cn.yongnian.seckill.service;

import cn.yongnian.seckill.dao.GoodsDao;
import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.mapper.GoodsExtMapper;
import cn.yongnian.seckill.mapper.GoodsMapper;
import cn.yongnian.seckill.mapper.SeckillGoodsExtMapper;
import cn.yongnian.seckill.mapper.SeckillGoodsMapper;
import cn.yongnian.seckill.model.Goods;
import cn.yongnian.seckill.model.GoodsExample;
import cn.yongnian.seckill.model.SeckillGoods;
import cn.yongnian.seckill.model.SeckillGoodsExample;
import cn.yongnian.seckill.result.CodeMessage;
import cn.yongnian.seckill.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 */
@Service
public class GoodsService {
    @Autowired
    GoodsExtMapper goodsExtMapper;

    @Autowired
    SeckillGoodsExtMapper seckillGoodsExtMapper;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    public List<GoodsVo> listGoodsVo() {
        return goodsExtMapper.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        Goods good = goodsMapper.selectByPrimaryKey(goodsId);
        if (good == null) {
            throw new GlobalException(CodeMessage.SERVER_ERROR);
        }
        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(good, goodsVo);

        SeckillGoodsExample seckillGoodsExample = new SeckillGoodsExample();
        seckillGoodsExample.createCriteria().andGoodsIdEqualTo(goodsId);
        List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(seckillGoodsExample);
        if (seckillGoods.isEmpty()) {
            // 没有秒杀信息
        } else if (seckillGoods.size() != 1) {
            throw new GlobalException(CodeMessage.MULTI_SECKILL_ORDER);
            //多个秒杀
        } else {
            //正常
            SeckillGoods seckillGoods1 = seckillGoods.get(0);
            // goodsVo 的 ID 是goods ID
            BeanUtils.copyProperties(seckillGoods1, goodsVo, "id");
        }
        return goodsVo;
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goodsVo.getId());
        int ret = seckillGoodsExtMapper.reduceStock(g);
        return ret > 0;
    }
}
