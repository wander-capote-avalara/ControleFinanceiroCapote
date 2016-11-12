package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;

public interface ContaDAO {
	
	public void inserir(Conta conta) throws ValidationException;
	public boolean deletaConta(int id) throws ValidationException;
	public List<Conta> getBills(int id, int userId, RangeDTO range)throws ValidationException;
	public List<Conta> getAllFamilyBills(int idFamily)throws ValidationException;
	public List<Graph> getBillsByCategory(int userId, RangeDTO range)throws ValidationException;
	public double getBillsTotalValue(RangeDTO dates, int userId)throws ValidationException;  
	public List<Graph> getFamilyBillsTotalValue(int idFamily)throws ValidationException;
	public List<Parcela> getParcelsById(int id)throws ValidationException;
} 