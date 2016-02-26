package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.text.html.parser.Parser;

import org.apache.catalina.User;

import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.UsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;
import br.com.ControleFinanceiroCapote.validacao.ValidaUsuario;
import sun.print.resources.serviceui;

public class JDBCUsuarioDAO implements UsuarioDAO {

	private Connection conexao;

	public JDBCUsuarioDAO(Connection conexao) {
		this.conexao = conexao;
	}

	ValidaUsuario valid = new ValidaUsuario();

	public boolean inserir(Usuario user) throws ValidationException {
		valid.insertValidation(user);
		if (user.getId() == 0) {
			String comando = "insert into usuarios " + "(Usuario, Senha, Email, Nivel, Ativo, Id_Familia) "
					+ "values(?,?,?,?,?,?)";

			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando);
				p.setString(1, user.getUsuario());
				p.setString(2, user.getSenha());
				p.setString(3, user.getEmail());
				p.setInt(4, user.getNivel());
				p.setInt(5, user.getAtivo());

				if (user.getId_familia() == 0)
					p.setNull(6, Types.INTEGER);
				else
					p.setInt(6, user.getId_familia());

				p.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			String comando = "Update usuarios set Usuario=?, Senha=?, Email=?, Nivel=?, Ativo=?, Id_Familia=? ";
			comando += "where Id_Usuarios=?";

			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando);
				p.setString(1, user.getUsuario());
				p.setString(2, user.getSenha());
				p.setString(3, user.getEmail());
				p.setInt(4, user.getNivel());
				p.setInt(5, user.getAtivo());

				if (user.getId_familia() == 0)
					p.setNull(6, Types.INTEGER);
				else
					p.setInt(6, user.getId_familia());

				p.setInt(7, user.getId());
				p.execute();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}

	@Override
	public List<Usuario> getUsers() {
		String comando = "select *, familias.Nome as NomeFamilia from usuarios left join familias on familias.Id_Familias = usuarios.Id_Familia";

		List<Usuario> listUsuario = new ArrayList<Usuario>();
		Usuario usuario = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			while (rs.next()) {
				usuario = new Usuario();
				int idUsuario = rs.getInt("Id_Usuarios");
				int nivel = rs.getInt("Nivel");
				String username = rs.getString("Usuario");
				String email = rs.getString("Email");
				String family = rs.getString("NomeFamilia");
				int isActive = rs.getInt("Ativo");

				usuario.setId(idUsuario);
				usuario.setNivel(nivel);
				usuario.setUsuario(username);
				usuario.setEmail(email);
				usuario.setNomeFamilia(family);
				usuario.setAtivo(isActive);
				listUsuario.add(usuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listUsuario;
	}

	@Override
	public boolean deletaUsuario(int id) throws ValidationException {
		//getUsers().forEach((user) -> user.getId());
		//getUsers().stream().filter(user -> user.getId() == id).findAny().isPresent();
		
		valid.userValidation(id);
		
		String comando = "UPDATE usuarios SET Ativo = 0 WHERE Id_Usuarios =" + id;
		Statement p;
		try {
			p = this.conexao.createStatement();
			p.execute(comando);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean authUser(Usuario user) throws SQLException {
		String comando = "select * from usuarios ";
		if (!user.equals("null") || !user.equals("")) {
			comando += "where Usuario=? && Senha=?";
		}
		PreparedStatement p;
		ResultSet rs = null;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, user.getUsuario());
			p.setString(2, user.getSenha());
			rs = p.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs.next() ? true : false;
	}

	@Override
	public boolean ativaUsuario(int id) throws ValidationException {
		
		valid.userValidation(id);
		
		String comando = "UPDATE usuarios SET Ativo = 1 WHERE Id_Usuarios =" + id;
		Statement p;
		try {
			p = this.conexao.createStatement();
			p.execute(comando);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<Familia> getFamilies() {
		String comando = "SELECT familias.Id_Familias as idfamilia, familias.Nome as nomeFamilia, usuarios.Usuario as owner FROM familias LEFT JOIN usuarios ON usuarios.Id_Usuarios = familias.Id_Usuario";

		List<Familia> listFamilias = new ArrayList<Familia>();
		Familia familia = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			while (rs.next()) {
				familia = new Familia();
				int idFamily = rs.getInt("idfamilia");
				String nameFamily = rs.getString("nomeFamilia");
				String ownerFamily = rs.getString("owner");

				familia.setId(idFamily);
				familia.setName(nameFamily);
				familia.setOwner(ownerFamily);
				listFamilias.add(familia);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFamilias;
	}

	@Override
	public Usuario getUserById(int id) throws ValidationException {
		
		valid.userValidation(id);
		
		String comando = "select * from usuarios where Id_Usuarios=" + id;
		Usuario user = new Usuario();
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			while (rs.next()) {
				int idUser = rs.getInt("Id_Usuarios");
				int idFamily = rs.getInt("Id_Familia");
				String username = rs.getString("Usuario");
				int level = rs.getInt("Nivel");
				String email = rs.getString("Email");
				int ativo = rs.getInt("Ativo");
				user.setId(idUser);
				user.setId_familia(idFamily);
				user.setUsuario(username);
				user.setNivel(level);
				user.setEmail(email);
				user.setAtivo(ativo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
