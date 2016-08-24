package br.com.ControleFinanceiroCapote.validacao;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class ValidaUsuario {
	
	public ValidaUsuario() {

	}

	public void insertValidation(Usuario user) throws ValidationException {

		String msg = "";
		if (user.getUsuario().isEmpty()) {
			msg += "Campo Usuário não pode ser vazio!";
		} else if (user.getEmail().isEmpty()) {
			msg += "Campo E-Mail não pode ser vazio!";
		} else if (user.getSenha().isEmpty()) {
			msg += "Campo Senha não pode ser vazio!";
		} else if (user.getConfirmaSenha().isEmpty()) {
			msg += "Campo Confirma Senha não pode ser vázio!";
		} else if (user.getConfirmaSenha().equals(user.getSenha())) {
			msg += "Campo Senha e Confirma senha devem ser iguais!";
		}

		if (!msg.equals("")) {
			throw new ValidationException(msg);
		}
	}

	public void userValidation(int id) throws ValidationException {
		if (id == 0) {
			throw new ValidationException("Usuário não exitente!");
		}
	}

	public void updateValidation(Usuario user) throws ValidationException {
		insertValidation(user);
		userValidation(user.getId());		
	}

}
