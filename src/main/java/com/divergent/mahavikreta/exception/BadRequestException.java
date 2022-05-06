package com.divergent.mahavikreta.exception;

/**
 * This class is Exception for Bad Request Error<br>
 * <b>Error Code</b>:- {@code 400}
 * 
 * @author Aakash
 *
 */
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public final String message;

	/**
	 * Construct new Bad Request Exception with message.
	 * 
	 * @param message {@link String}
	 */
	public BadRequestException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * Construct new Bad Exception with message.
	 * 
	 * @param message {@link String}
	 * @param t
	 */
	public BadRequestException(String message, Throwable t) {
		super(message, t);
		this.message = message;
	}

}
