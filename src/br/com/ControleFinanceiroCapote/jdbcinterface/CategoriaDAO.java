package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.objetos.Categoria;

public interface CategoriaDAO {

	public List<Categoria> getCategories(int id, int userId);
}
