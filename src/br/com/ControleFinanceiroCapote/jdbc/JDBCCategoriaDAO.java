package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.CategoriaDAO;
import br.com.ControleFinanceiroCapote.objetos.Categoria;
import br.com.ControleFinanceiroCapote.validacao.ValidaCategoria;
import br.com.ControleFinanceiroCapote.validacao.ValidaFamilia;
import br.com.ControleFinanceiroCapote.validacao.ValidaUsuario;

public class JDBCCategoriaDAO implements CategoriaDAO {

	ValidaCategoria validac =  new ValidaCategoria();
	ValidaFamilia validaf = new ValidaFamilia();
	ValidaUsuario valida = new ValidaUsuario();
	
	private Connection conexao;

	public JDBCCategoriaDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	@Override
	public List<Categoria> getCategories(int id, int userId) throws ValidationException {
		
		valida.userValidation(userId);
		
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT c.Id_Categorias as idCategoria, c.Descricao as nomeCategoria ");
		comando.append("FROM categorias c ");
		comando.append("WHERE Status = 1");
		comando.append(" AND ");
		comando.append(" Id_Usuarios = " + userId);
		if (id != 0) {
			validac.idValidation(id);
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
			throw new ValidationException(e);
		}
		return listCategory;
	}

	public void inserir(Categoria categoria, int userId) throws ValidationException {
		valida.userValidation(userId);
		if (categoria.getId() == 0) {
			validac.insertValidation(categoria);
			StringBuilder comando = new StringBuilder();
			comando.append("INSERT INTO categorias ");
			comando.append("(Descricao, Status, Id_Usuarios) ");
			comando.append("VALUES (?,?,?)");

			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando.toString());
				p.setString(1, categoria.getName());
				p.setInt(2, 1);
				p.setInt(3, userId);
				p.execute();
			} catch (Exception e) {
				throw new ValidationException(e);
			}
		} else {
			validac.updateValidation(categoria);
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE categorias ");
			comando.append("SET Descricao = ? ");
			comando.append("WHERE Id_Categorias = ?");

			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando.toString());
				p.setString(1, categoria.getName());
				p.setInt(2, categoria.getId());
				p.execute();
			} catch (Exception e) {
				throw new ValidationException(e);
			}
		}
	}

	public void deletaCategoria(int id) throws ValidationException {
		validac.idValidation(id);
		StringBuilder comando = new StringBuilder();
		comando.append("UPDATE categorias ");
		comando.append("SET Status = 0 ");
		comando.append("WHERE Id_Categorias = ?");
		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();

		} catch (SQLException e) {
			throw new ValidationException(e);
		}
	}

}
