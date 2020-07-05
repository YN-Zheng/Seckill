package cn.yongnian.seckill.result;


/**
 * TODO
 */


public enum  CodeMessage {

    //通用
    SUCCESS(0,"success"),
    SERVER_ERROR(500100,"服务端异常"),
    BIND_ERROR(500101,"参数校验异常: %s"),
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

    //秒杀模块 5005XX
    NO_STOCK(500501,"该商品无库存,秒杀结束"),
    REPEAT_SECKILL(500502,"您已经成功秒杀该商品, 不能重复购买" )
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
