package cn.yongnian.seckill.exception;

import cn.yongnian.seckill.result.CodeMessage;

/**
 * TODO
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
