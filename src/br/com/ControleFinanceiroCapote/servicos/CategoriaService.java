package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.jdbc.JDBCCategoriaDAO;
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

}
