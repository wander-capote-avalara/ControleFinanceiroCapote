package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;

public interface ContaDAO {
	
	public void inserir(Conta conta);
	public boolean deletaConta(int id) throws ValidationException;
	public List<Conta> getBills(int id, int userId, RangeDTO range);
	public List<Conta> getAllFamilyBills(int idFamily);
	public List<Graph> getBillsByCategory(int userId, RangeDTO range); 
	public int getBillsTotalValue(RangeDTO dates, int userId);  
	public List<Graph> getFamilyBillsTotalValue(int idFamily); 
	public List<Parcela> getParcelsById(int id); 
} 