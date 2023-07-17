package br.com.banco.exception;

public class RegistroNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RegistroNotFoundException(String id) {
		super("Registro " + id + " não encontrado!");
	}
	
}
