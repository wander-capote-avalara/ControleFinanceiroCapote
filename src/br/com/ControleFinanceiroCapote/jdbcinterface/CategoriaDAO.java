package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Categoria;

public interface CategoriaDAO {

	public List<Categoria> getCategories(int id, int userId) throws ValidationException;
	public void inserir(Categoria categoria, int userId) throws ValidationException;
	public void deletaCategoria(int id) throws ValidationException;
}
