package com.cimctht.servicestation.common.exception;

public class UnimaxException extends RuntimeException{
	
	public UnimaxException() {
		super();
	}
	public UnimaxException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public UnimaxException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnimaxException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnimaxException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
