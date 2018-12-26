package com.yowits.mail.constan.dto;

/**
 * 自定义响应状态码
 */
public enum GlobalErrorCode {
    CODE_FU_1(-1,"系统繁忙，请稍后再试"),
    CODE_200(200,"请求成功"),
    
    CODE_40000(40000,"失败"),
    CODE_40001(40001,"不合法的凭证类型"),
    CODE_40002(40002,"邮件地址格式错误"),
    
    CODE_50550(50550,"改服务器发信IP超过邮箱限制"),
    CODE_50535(50535,"发件人账号或密码验证失败"),
    CODE_50536(50536,"发送邮件失败，附件不存在"),
    CODE_50537(50537,"文件上传失败，文件不存在"),
    CODE_50538(50538,"上传文件大小超出限制，上传失败"),
    	
    
    CODE_61450(61450,"系统错误(system error)"),
    CODE_61602(61502,"必填参数为空"),
    CODE_61503(61503,"不允许重复提交");
    private int code;
    private String desc;

    GlobalErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GlobalErrorCode getDescByCode(int code){
        for (GlobalErrorCode errorCodeEnum:GlobalErrorCode.values()){
            if (errorCodeEnum.getCode()== code){
                return errorCodeEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
