package br.com.ControleFinanceiroCapote.validacao;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Categoria;

public class ValidaCategoria {
	
	public void insertValidation(Categoria categ) throws ValidationException {

		String msg = "";
		if (categ.getName().isEmpty())
			msg += "Campo nome da categoria não pode ser vazio!";

		if (!msg.equals(""))
			throw new ValidationException(msg);
	}

	public void idValidation(int id) throws ValidationException {
		if (id == 0)
			throw new ValidationException("Categoria não exitente!");
	}

	public void updateValidation(Categoria categ) throws ValidationException {
		insertValidation(categ);
		idValidation(categ.getId());
	}
}
