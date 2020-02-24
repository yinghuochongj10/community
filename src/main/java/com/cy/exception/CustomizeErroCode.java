package com.cy.exception;

public enum CustomizeErroCode implements ICustomizeErroCode{
	QUESTION_NOT_FOUND(2001,"你找的问题不存在了，要不换个试试？"),
	TAGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
	NO_LOGIN(2003,"当前操作需要登录，请登陆后重试"),
	SYS_ERROR(2004,"服务器冒烟了，要不等等？"),
	TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
	COMMENT_NOT_FOUND(2006,"你找的评论不在了，换个试试？"),
	COMMENT_IS_EMPTY(2007,"输入内容不能为空"),
	READ_NOTIFICATION_FALL(2008,"兄弟你这是读别人的信息呢？"),
	NOTIFICATION_NOT_FOUND(2009,"消息莫非不翼而飞了？")
	;
	
	@Override
	public String getMessage() {
		return message;
	}
	@Override
	public Integer getCode() {
		return code;
	}
	private String message;
	private Integer code;

	 CustomizeErroCode(Integer code,String message) {
	this.message=message;
	this.code=code;
	}




	
}
