package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.helpers.Helper;
import br.com.ControleFinanceiroCapote.jdbc.JDBCFamiliaDAO;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class FamiliaService {

	public FamiliaService() {
		// TODO Auto-generated constructor stub
	}
	
	public void addfamily(Familia family) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.inserir(family);
		conec.fecharConexao();
	}
	
	public List<Familia> getFamilies() {
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		List<Familia> families = jdbcFamilia.getFamily();
		conec.fecharConexao();
		
		return families;	
	}

}
