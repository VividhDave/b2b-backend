package com.divergent.mahavikreta.exception;

/**
 * This class is Exception for User Request Error<br>
 * <b>Error Code</b>:- {@code 400}
 * 
 * @author Aakash
 *
 */
public class UserRequestException extends GenricException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct new Generic Exception with user message.
	 * 
	 * @param exception user message
	 */
	public UserRequestException(String message) {
		super(message);
	}

	/**
	 * Construct new Generic Exception with user message.
	 * 
	 * @param exception user message
	 * @param throwable {@link Throwable}
	 */
	public UserRequestException(String message, Throwable e) {
		super(message, e);
	}

}
