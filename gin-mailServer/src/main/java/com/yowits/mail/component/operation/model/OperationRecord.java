package com.yowits.mail.component.operation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yowits.mail.constan.enums.SendStatus;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 操作记录
 * @author jian.liu
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class OperationRecord {

	//id
	private String id;
	
	//收件人
	@Indexed
	private String to; 
	
	//发件人
	@Indexed
	private String from;
	
	//发送时间
	private String createDate;
	
	//邮件类型
	@Indexed
	private String type;
	
	//内容
	private String content;
	
	//主题
	private String subject;
	
	//完成时间
	private String finishDate;
	
	//创建时间戳
	private Long currentTimeMillis;
	
	//发送状态
	private SendStatus status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getCreateDate() {
		return createDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public SendStatus getStatus() {
		return status;
	}

	public void setStatus(SendStatus status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getCurrentTimeMillis() {
		return currentTimeMillis;
	}

	public void setCurrentTimeMillis(Long currentTimeMillis) {
		this.currentTimeMillis = currentTimeMillis;
	}
}
