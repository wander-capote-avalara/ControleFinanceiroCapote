package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.ControleFinanceiroCapote.jdbcinterface.CategoriaDAO;
import br.com.ControleFinanceiroCapote.objetos.Categoria;

public class JDBCCategoriaDAO implements CategoriaDAO {


	private Connection conexao;

	public JDBCCategoriaDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	@Override
	public List<Categoria> getCategories(int id) {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT c.Id_Categorias as idCategoria, c.Descricao as nomeCategoria ");
		comando.append("FROM categorias c ");
		comando.append("WHERE 1=1");
		if (id != 0) {
			comando.append(" AND ");
			comando.append("c.Id_Categorias = " + id);
		}

		List<Categoria> listCategory = new ArrayList<Categoria>();
		Categoria category = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				category = new Categoria();
				int idCategory = rs.getInt("idCategoria");
				String nameCategory = rs.getString("nomeCategoria");

				category.setId(idCategory);
				category.setName(nameCategory);
				listCategory.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listCategory;
	}

}
