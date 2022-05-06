package com.divergent.mahavikreta.exception.handler;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.divergent.mahavikreta.exception.BadRequestException;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.exception.UserRequestException;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class is Global Exception Handler <br>
 * response formatting for Exceptions
 * 
 * @author Aakash
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final String EXCEPTION_OCCURED = "Exception Occured:: {}";
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * This method Handle {@link GenricException}.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param ex      {@link GenricException}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
	@ExceptionHandler(GenricException.class)
	public ResponseMessage<String> handleGenricException(HttpServletRequest request, GenricException ex) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link BadRequestException}
	 * 
	 * @param ex      {@link BadRequestException}
	 * @param request {@link HttpServletRequest}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseMessage<String> handleValidationException(BadRequestException ex, HttpServletRequest request) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link MethodArgumentNotValidException}
	 * 
	 * @param ex      {@link MethodArgumentNotValidException}
	 * @param request {@link HttpServletRequest}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseMessage<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		BindingResult bindingResult = ex.getBindingResult();
		Set<String> errors = new HashSet<>();
		for (ObjectError objectError : bindingResult.getAllErrors()) {
			if (!errors.contains(objectError.getDefaultMessage())) {
				errors.add(objectError.getDefaultMessage());
			}
		}
		return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), errors.toString());
	}

	/**
	 * This method Handle {@link HttpRequestMethodNotSupportedException}
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param ex      {@link HttpRequestMethodNotSupportedException}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseMessage<String> handleMethodNotAllowedException(HttpServletRequest request,
			HttpRequestMethodNotSupportedException ex) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link HttpRequestMethodNotSupportedException}
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param ex      {@link HttpRequestMethodNotSupportedException}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserRequestException.class)
	public ResponseMessage<String> handleUserRequestException(HttpServletRequest request,
			HttpRequestMethodNotSupportedException ex) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link HttpMessageNotReadableException}
	 * 
	 * @param ex      {@link HttpMessageNotReadableException}
	 * @param request {@link HttpServletRequest}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseMessage<String> httpMessageNotReadableException(HttpMessageNotReadableException ex,
			HttpServletRequest request) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link AccessDeniedException}
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param ex      {@link AccessDeniedException}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ResponseMessage<String> handleMethodNotAllowedException(HttpServletRequest request,
			AccessDeniedException ex) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.FORBIDDEN.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link ValidationException}
	 * 
	 * @param ex      {@link ValidationException}
	 * @param request {@link HttpServletRequest}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseMessage<String> handleValidationException(ValidationException ex, HttpServletRequest request) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
	}

	/**
	 * This method Handle {@link Exception}
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param ex {@link Exception}
	 * @return {@link ResponseMessage}&lt;{@link String}&gt;
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseMessage<String> handleMethodNotAllowedException(HttpServletRequest request, Exception ex) {
		LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
		return new ResponseMessage<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
	}

}
