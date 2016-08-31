package br.com.ControleFinanceiroCapote.validacao;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public class ValidaRenda {
	public ValidaRenda(){}
	
	public void insertValidation(Renda renda) throws ValidationException {

		String msg = "";
		if (renda.getCategoria() == 0) {
			msg += "Campo categoria não pode ser vazio!";
		}else if (renda.getUserId() == 0) {
			msg += "Toda renda deve ter um usuário!";
		}else if (renda.getDescription().isEmpty()) {
			msg += "Campo descrição não pode ser vazio!";
		}else if (renda.getTotalValue() == 0) {
			msg += "Campo valor total não pode ser vazio!";
		}

		if (!msg.equals("")) {
			throw new ValidationException(msg);
		}
	}

	public void idValidation(int id) throws ValidationException {
		if (id == 0) {
			throw new ValidationException("Renda não exitente!");
		}
	}

	public void updateValidation(Renda renda) throws ValidationException {
		insertValidation(renda);
		idValidation(renda.getId());		
	}
}
