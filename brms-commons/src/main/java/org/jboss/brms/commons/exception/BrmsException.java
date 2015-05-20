package org.jboss.brms.commons.exception;

public class BrmsException extends Exception {

	private static final long serialVersionUID = 1L;

	public BrmsException() {
		super();
	}

	public BrmsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BrmsException(String message, Throwable cause) {
		super(message, cause);
	}

	public BrmsException(String message) {
		super(message);
	}

	public BrmsException(Throwable cause) {
		super(cause);
	}

}
