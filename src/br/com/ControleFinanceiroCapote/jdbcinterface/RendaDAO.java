package br.com.ControleFinanceiroCapote.jdbcinterface;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public interface RendaDAO {

	public void inserir(Renda renda);
	public boolean deletaRenda(int id) throws ValidationException;

}
