package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbc.JDBCContaDAO;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;

public class ContaService {

	public void addConta(Conta conta) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		jdbcContadao.inserir(conta);
		conec.fecharConexao();
	}

	public void deletaConta(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		jdbcContadao.deletaConta(id);
		conec.fecharConexao();

	}

	public List<Conta> getBills(int id, int userId) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Conta> billList = jdbcContadao.getBills(id, userId, null);
		conec.fecharConexao();

		return billList;
	}

	public List<Conta> getBillsByDate(RangeDTO range, int userId) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Conta> billList = jdbcContadao.getBills(0, userId, range);
		conec.fecharConexao();

		return billList;
	}

	public List<Parcela> getParcelsById(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Parcela> parcelList = jdbcContadao.getParcelsById(id);
		conec.fecharConexao();

		return parcelList;
	}

	public double getBillsTotalValue(RangeDTO datas, int userId) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		double billsValue = jdbcContadao.getBillsTotalValue(datas, userId);
		conec.fecharConexao();

		return billsValue;
	}

	public List<Graph> getBillsByCategory(RangeDTO datas, int userId) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Graph> bills = jdbcContadao.getBillsByCategory(userId, datas);
		conec.fecharConexao();

		return bills;
	}

	public List<Graph> getFamilyBillsTotalValue(int familyId) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Graph> bills = jdbcContadao.getFamilyBillsTotalValue(familyId);
		conec.fecharConexao();

		return bills;
	}

	public void payParcel(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		jdbcContadao.payParcel(id);
		conec.fecharConexao();
	}

}
