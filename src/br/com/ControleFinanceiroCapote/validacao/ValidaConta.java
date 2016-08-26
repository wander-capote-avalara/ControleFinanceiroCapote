package br.com.ControleFinanceiroCapote.validacao;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Conta;

public class ValidaConta {
	public ValidaConta(){}
	
	public void insertValidation(Conta conta) throws ValidationException {

		String msg = "";
		if (conta.getCategoria() == 0) {
			msg += "Campo categoria não pode ser vazio!";
		}else if (conta.getUserId() == 0) {
			msg += "Toda conta deve ter um usuário!";
		}else if (conta.getDescription().isEmpty()) {
			msg += "Campo descrição não pode ser vazio!";
		}else if (conta.getTotalValue() == 0) {
			msg += "Campo valor total não pode ser vazio!";
		}

		if (!msg.equals("")) {
			throw new ValidationException(msg);
		}
	}

	public void idValidation(int id) throws ValidationException {
		if (id == 0) {
			throw new ValidationException("Conta não exitente!");
		}
	}

	public void updateValidation(Conta conta) throws ValidationException {
		insertValidation(conta);
		idValidation(conta.getId());		
	}
}
