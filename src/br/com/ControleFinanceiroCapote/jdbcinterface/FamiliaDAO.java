package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Familia;

public interface FamiliaDAO {

	public void inserir(Familia family) throws ValidationException;
	public List<Familia> getFamily();
}
