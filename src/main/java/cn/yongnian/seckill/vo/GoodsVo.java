package cn.yongnian.seckill.vo;

import cn.yongnian.seckill.model.Goods;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO
 */
public class GoodsVo extends Goods {
    private Integer seckillStock;
    private BigDecimal seckillPrice;
    private Date startDate;
    private Date endDate;

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public BigDecimal getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
