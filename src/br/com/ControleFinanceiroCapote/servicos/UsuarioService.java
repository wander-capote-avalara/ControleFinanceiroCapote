package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.helpers.Helper;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Invite;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class UsuarioService {

	public UsuarioService() {

	}

	public Usuario GetUsersById(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcContato = new JDBCUsuarioDAO(conexao);
		Usuario user = jdbcContato.getUserById(id);
		conec.fecharConexao();

		return user;
	}

	public void AddUser(Usuario usuario) throws Exception {
		usuario.setSenha(Helper.hasher(usuario.getSenha()));

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcContato = new JDBCUsuarioDAO(conexao);
		jdbcContato.inserir(usuario);
		conec.fecharConexao();
	}

	public void DeleteUser(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
		jdbcUsuario.deletaUsuario(id);
		conec.fecharConexao();
	}

	public void ActiveUser(int id) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
		jdbcUsuario.ativaUsuario(id);
		conec.fecharConexao();
	}

	public List<Familia> GetFamilies() {
		List<Familia> data = new ArrayList<Familia>();
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcContato = new JDBCUsuarioDAO(conexao);
		data = jdbcContato.getFamilies();
		conec.fecharConexao();

		return data;
	}
	
	public List<Usuario> GetUsers(String text) {
		List<Usuario> data = new ArrayList<Usuario>();
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcContato = new JDBCUsuarioDAO(conexao);
		data = jdbcContato.getUsers(text);
		conec.fecharConexao();

		return data;
	}
	
	public Usuario getUserInfo(int id) {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCUsuarioDAO jdbcContato = new JDBCUsuarioDAO(conexao);
		Usuario user = jdbcContato.getUserInfoById(id);
		conec.fecharConexao();

		return user;
	}
}
