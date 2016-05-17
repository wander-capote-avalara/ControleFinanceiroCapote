package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbc.JDBCContaDAO;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Parcela;

public class ContaService {

	public void addConta(Conta conta) {
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

	public  List<Conta> getBills(int id, int userId) {
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Conta> billList = jdbcContadao.getBills(id, userId);
		conec.fecharConexao();

		return billList;
	}

	public List<Parcela> getParcelsById(int id) {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcContadao = new JDBCContaDAO(conexao);
		List<Parcela> parcelList = jdbcContadao.getParcelsById(id);
		conec.fecharConexao();

		return parcelList;
	}

}
