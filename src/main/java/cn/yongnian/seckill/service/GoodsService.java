package cn.yongnian.seckill.service;

import cn.yongnian.seckill.dao.GoodsDao;
import cn.yongnian.seckill.exception.GlobalException;
import cn.yongnian.seckill.mapper.GoodsExtMapper;
import cn.yongnian.seckill.mapper.GoodsMapper;
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
    GoodsMapper goodsMapper;

    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    public List<GoodsVo> listGoodsVo(){
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
            //多个秒杀
        } else {
            //正常
            SeckillGoods seckillGoods1 = seckillGoods.get(0);
            // goodsVo 的 ID 是goods ID
            BeanUtils.copyProperties(seckillGoods1, goodsVo,"id");
        }
        return goodsVo;
    }

    public synchronized void reduceStock(GoodsVo goodsVo) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setSeckillStock(goodsVo.getSeckillStock()-1);
        SeckillGoodsExample example = new SeckillGoodsExample();
        example.createCriteria().andGoodsIdEqualTo(goodsVo.getId());

        seckillGoodsMapper.updateByExampleSelective(seckillGoods,example);
    }
}
