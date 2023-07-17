package br.com.banco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.banco.exception.RegistroNotFoundException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(RegistroNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleRegistroNotFoundException(RegistroNotFoundException e) {
		return e.getMessage();
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleIllegalArgumentException(IllegalArgumentException e) {
		return "Valor inválido";
	}

}
