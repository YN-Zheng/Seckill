package cn.yongnian.seckill.vo;

import cn.yongnian.seckill.model.Order;
import lombok.Data;

/**
 * TODO
 */

@Data
public class OrderDetailVo {
    private GoodsVo good;
    private Order order;
}
