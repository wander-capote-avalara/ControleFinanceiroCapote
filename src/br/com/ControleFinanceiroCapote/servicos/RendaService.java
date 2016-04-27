package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.jdbc.JDBCRendaDAO;
import br.com.ControleFinanceiroCapote.jdbcinterface.RendaDAO;
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
}
