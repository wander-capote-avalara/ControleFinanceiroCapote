package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public interface RendaDAO {

	public void inserir(Renda renda);
	public boolean deletaRenda(int id) throws ValidationException;
	public List<Renda> getIncomes(int id, int userId, RangeDTO range);

}
