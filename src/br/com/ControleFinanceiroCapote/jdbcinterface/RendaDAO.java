package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public interface RendaDAO {

	public void inserir(Renda renda) throws ValidationException;
	public boolean deletaRenda(int id) throws ValidationException;
	public List<Renda> getIncomes(int id, int userId, RangeDTO range) throws ValidationException;
	public List<Renda> getAllFamilyIncomes(int idFamily) throws ValidationException;
	public List<Graph> getIncomesByCategory(int userId, RangeDTO  range) throws ValidationException;
	public double getTotalValueIncome(RangeDTO dates, int userId) throws ValidationException;
	public List<Graph> getFamilyIncomesTotalValue(int idFamily) throws ValidationException;
	public List<Parcela> getParcelsById(int id) throws ValidationException;
}
