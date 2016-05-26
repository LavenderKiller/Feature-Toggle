package com.toggle.exception;

import java.util.Locale;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalBaseExceptionHandler {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;

	/**
	 * Handles JPA NoResultExceptions thrown from web service controller
	 * methods. Creates a response with an empty body and HTTP status code 404,
	 * not found.
	 * 
	 * @param nre
	 *            A NoResultException instance.
	 * @return A ResponseEntity with an empty response body and HTTP status code
	 *         404.
	 */
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<Exception> handleNoResultException(NoResultException nre) {
		logger.error("> handleNoResultException");
		logger.error("- NoResultException: ", nre);
		logger.error("< handleNoResultException");
		return new ResponseEntity<Exception>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles all Exceptions not addressed by more specific
	 * <code>@ExceptionHandler</code> methods. Creates a response with the
	 * Exception detail in the response body as JSON and a HTTP status code of
	 * 500, internal server error.
	 * 
	 * @param e
	 *            An Exception instance.
	 * @return A ResponseEntity containing a the Exception attributes in the
	 *         response body and a HTTP status code 500.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Exception> handleException(Exception e) {
		logger.error("> handleException");
		logger.error("- Exception: ", e);
		logger.error("< handleException");
		return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ModelMap handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
		ModelMap map = new ModelMap();
		ModelMap errorMap = new ModelMap();
		map.addAttribute("hasErrors", true);
		for (FieldError fieldError : error.getBindingResult().getFieldErrors()) {
			String message = messageSource.getMessage(fieldError.getCode(), null, fieldError.getDefaultMessage(),
					Locale.US);
			errorMap.addAttribute(fieldError.getField(), message);
		}
		map.addAttribute("bindingErrors", errorMap);
		return map;
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ModelMap handleMissingServletParameterException(MissingServletRequestParameterException error) {
		ModelMap map = new ModelMap();
		ModelMap errorMap = new ModelMap();
		map.addAttribute("hasErrors", true);
		errorMap.addAttribute(error.getParameterName(), error.getMessage());
		map.addAttribute("bindingErrors", errorMap);
		return map;
	}

}
