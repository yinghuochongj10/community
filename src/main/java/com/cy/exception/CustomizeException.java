package com.cy.exception;

public class CustomizeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;
	private Integer code;
	public CustomizeException(ICustomizeErroCode erroCode) {
		this.code=erroCode.getCode();
		this.message=erroCode.getMessage();
	}

	@Override
	public String getMessage() {
		return message;
	}

	public Integer getCode() {
		return code;
	}

	
}
