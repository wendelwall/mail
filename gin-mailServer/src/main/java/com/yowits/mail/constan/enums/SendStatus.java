package com.yowits.mail.constan.enums;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public enum SendStatus {

	wait("等待"),
	
	finish("完成"),
	
	exception("异常");
	

	private String value;
	private String name;

    private SendStatus(String name) {
    	this.value= this.toString();
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
	public String getValue() {
		return value;
	}
 
    
    public static SendStatus formatEnum(String key){
    	
    	if(StringUtils.isBlank(key)){
    		return null;
    	}
    	try {
			return SendStatus.valueOf(key);
		} catch (Exception e) {
			return exception;
		}    	
    }
	
}