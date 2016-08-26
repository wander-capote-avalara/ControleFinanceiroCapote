package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbc.JDBCRendaDAO;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public class RendaService {
	
	public RendaService() {
		// TODO Auto-generated constructor stub
	}
	public void addRenda(Renda renda) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		jdbcRendadao.inserir(renda);
		conec.fecharConexao();
	}
	public List<Renda> getIncomes(int id, int userId) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		List<Renda> incomeList = jdbcRendadao.getIncomes(id, userId, null);
		conec.fecharConexao();

		return incomeList;
	}
	
	public void deletaRenda(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		jdbcRendadao.deletaRenda(id);
		conec.fecharConexao();
	}
	public List<Parcela> getParcelsById(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		List<Parcela> parcelList = jdbcRendadao.getParcelsById(id);
		conec.fecharConexao();

		return parcelList;
	}
	public int getTotalValueIncome(RangeDTO datas, int userId) throws ValidationException {
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		int incomesValue = jdbcRendadao.getTotalValueIncome(datas, userId);
		conec.fecharConexao();
		
		return incomesValue;
	}
	public List<Renda> getIncomesDate(RangeDTO range, int userId) throws ValidationException {
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		List<Renda> incomeList = jdbcRendadao.getIncomes(0, userId, range);
		conec.fecharConexao();

		return incomeList;
	}
		
	public List<Graph> getIncomesByCategory(RangeDTO datas, int userId) throws ValidationException {
			
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		List<Graph> incomes = jdbcRendadao.getIncomesByCategory(userId, datas);
		conec.fecharConexao();
		
		return incomes;
	}
	public List<Graph> getFamilyIncomesTotalValue(int familyId) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRendadao = new JDBCRendaDAO(conexao);
		List<Graph> Incomes = jdbcRendadao.getFamilyIncomesTotalValue(familyId);
		conec.fecharConexao();

		return Incomes;
	}
}
