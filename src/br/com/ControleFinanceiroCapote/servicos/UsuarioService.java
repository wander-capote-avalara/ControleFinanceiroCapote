package br.com.ControleFinanceiroCapote.servicos;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.helpers.Helper;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;
import br.com.ControleFinanceiroCapote.rest.UtilRest;

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
}
