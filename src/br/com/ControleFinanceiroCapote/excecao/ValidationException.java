package br.com.ControleFinanceiroCapote.excecao;

public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static String mensagem = "Erro ao fazer a operação, favor entrar em contato com o admin!";
	
	public ValidationException(){
		super(mensagem);
	}
	
	public ValidationException(String msg, Throwable t){
		super(msg, t);
	}
	
	public ValidationException(String msg){
		super(msg);
	}
	
	public ValidationException(Throwable t){
		super(mensagem, t);
	}
}
