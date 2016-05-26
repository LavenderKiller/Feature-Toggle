package com.toggle.exception;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class RequestValidationException extends MethodArgumentNotValidException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2333307111826035486L;

	public RequestValidationException(BindingResult bindingResult) {
		super(null, bindingResult);
	}

}
