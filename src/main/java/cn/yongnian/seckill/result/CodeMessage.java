package cn.yongnian.seckill.result;


/**
 * TODO
 */


public enum  CodeMessage {

    //通用
    SUCCESS(0,"success"),
    SERVER_ERROR(500100,"服务端异常"),
    BIND_ERROR(500101,"参数校验异常: %s"),
    MISSING_ARGUMENT(500102,"前端参数传输异常"),
    ACCESS_LIMIT_REACH(500103,"访问过于频繁"),
    //!!!不要直接用。用于填充参数用
    MODIFIED_ENUM(0,""),

    //登录模块 5002XX
    SESSION_ERROR(500210,"Session不存在或已经失效"),
    PASSWORD_EMPTY(500211,"密码不能为空"),
    MOBILE_EMPTY(500212,"手机号不能为空"),
    MOBILE_INVALID(500213,"手机号错误"),
    MOBILE_NOTFOUND(500214,"手机号不存在"),
    PASSWORD_ERROR(500215,"密码错误"),
    NOT_LOGIN(500516,"没有登陆"),

    //订单模块 5004XX
    ORDER_NOT_EXIST(500400,"订单不存在"),
    MULTI_SECKILL_ORDER(500401,"服务端错误: 多个秒杀订单"),

    //秒杀模块 5005XX
    NO_STOCK(500501,"该商品无库存,秒杀结束"),
    REPEAT_SECKILL(500502,"您已经成功秒杀该商品, 不能重复购买" ),
    WRONG_PATH(500503,"老实秒杀，不许开挂"),
    VERIFY_GENERATE_ERROR(500504,"生成验证码出错"),
    VERIFY_ERROR(500505,"验证码错误。不许开挂。")
    ;

    private int code;
    private String msg;
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    CodeMessage(int code,String msg){
        this.code = code;
        this.msg = msg;
    }


    public CodeMessage fillArgs(Object... args){
        int code = this.code;
        MODIFIED_ENUM.msg = String.format(this.msg, args);
        MODIFIED_ENUM.code = code;
        return MODIFIED_ENUM;
    }

}
