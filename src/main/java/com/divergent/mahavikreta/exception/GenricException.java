package com.divergent.mahavikreta.exception;

/**
 *  This class is Exception for Generic Exception <br>
 * <b>Error Code</b>:- {@code 417}
 * 
 * @author Aakash
 *
 */
public class GenricException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final String message;

	public GenricException(String message) {
		super(message);
		this.message = message;
	}

	public GenricException(String message, Throwable t) {
		super(message, t);
		this.message = message;
	}

}
