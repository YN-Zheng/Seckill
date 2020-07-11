package cn.yongnian.seckill.exception;

import cn.yongnian.seckill.result.CodeMessage;

/**
 * 全局异常,含有codeMessage, 方便返回错误信息给客户端
 */
public class GlobalException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    private CodeMessage codeMessage;

    public CodeMessage getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(CodeMessage codeMessage) {
        this.codeMessage = codeMessage;
    }

    public GlobalException(CodeMessage codeMessage){
        super(codeMessage.toString());
        this.codeMessage = codeMessage;
    }
}
