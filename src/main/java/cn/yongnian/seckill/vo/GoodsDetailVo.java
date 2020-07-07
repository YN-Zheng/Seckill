package cn.yongnian.seckill.vo;

import cn.yongnian.seckill.model.User;
import lombok.Data;

/**
 * TODO
 */
@Data
public class GoodsDetailVo {
    private int status = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private User user;
}
