package br.com.ControleFinanceiroCapote.validacao;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Familia;

public class ValidaFamilia {
	
	public void insertValidation(Familia family) throws ValidationException {

		String msg = "";
		if (family.getName().isEmpty()) 
			msg += "Campo nome da família não pode ser vazio!";
		 else if (family.getUsers().isEmpty())
			msg += "Erro!"; 

		if (!msg.equals(""))
			throw new ValidationException(msg);
	}

	public void familyValidation(int id) throws ValidationException {
		if (id == 0) 
			throw new ValidationException("Família não exitente!");
	}
	
	public void familyNameValidation(String name) throws ValidationException{
		if(name.isEmpty())
			throw new ValidationException("Nome da familia não pode ser vazio");
	}

	public void updateValidation(Familia family) throws ValidationException {
		insertValidation(family);
		familyValidation(family.getId());		
	}

}
