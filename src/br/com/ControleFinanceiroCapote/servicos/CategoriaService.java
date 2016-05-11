package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.jdbc.JDBCCategoriaDAO;
import br.com.ControleFinanceiroCapote.jdbc.JDBCContaDAO;
import br.com.ControleFinanceiroCapote.objetos.Categoria;

public class CategoriaService {
	
	public List<Categoria> getCategories(int id) {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCCategoriaDAO jdbcCategoria = new JDBCCategoriaDAO(conexao);
		List<Categoria> categories = jdbcCategoria.getCategories(id);
		conec.fecharConexao();

		return categories;
	}

	public void addCategory(Categoria categoria, int userId) {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCCategoriaDAO jdbcCategoria = new JDBCCategoriaDAO(conexao);
		jdbcCategoria.inserir(categoria, userId);
		conec.fecharConexao();
	}

	public void deleteCategory(int id) {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCCategoriaDAO jdbcCategoria = new JDBCCategoriaDAO(conexao);
		jdbcCategoria.deletaCategoria(id);
		conec.fecharConexao();		
	}

}
