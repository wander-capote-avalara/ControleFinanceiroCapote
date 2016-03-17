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
import org.omg.CORBA.COMM_FAILURE;

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
		int id = user.getId(),
			familyId = user.getId_familia();
		
		if (id == 0) {
			String comando = "insert into usuarios " + "(Usuario, Senha, Email, Nivel, Ativo) "
					+ "values(?,?,?,?,?)";

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando, Statement.RETURN_GENERATED_KEYS);
				p.setString(1, user.getUsuario());
				p.setString(2, user.getSenha());
				p.setString(3, user.getEmail());
				p.setInt(4, user.getNivel());
				p.setInt(5, user.getAtivo());

				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next() && familyId != 0) {
					id = rs.getInt(1);
					setFamily(id, familyId, true);
				}
				
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

	private void setFamily(int id, int familyId, boolean iOu) {
		StringBuilder comando = new StringBuilder();
		if (iOu) {
			comando.append("insert into user_family ");
			comando.append("(Familia_Id, Usuario_Id) values(?,?)");	
		}else {
			comando.append("update user_family ");
			comando.append("set Familia_Id = ? ");
			comando.append("where Usuario_Id = ?");
		}

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, familyId);
			p.setInt(2, id);

			p.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public List<Usuario> getUsers(String text) {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT *, familias.Nome AS NomeFamilia from usuarios ");
		comando.append("LEFT JOIN user_family ON user_family.Usuario_Id = usuarios.Id_Usuarios ");
		comando.append("LEFT JOIN familias ON familias.Id_Familias = user_family.Familia_Id ");
		if (!text.equals("") && !text.equals(null)) {
			comando.append("WHERE usuarios.Usuario LIKE '%"+text+"%'");
			comando.append(" AND usuarios.Id_Usuarios NOT IN ");
			comando.append("(SELECT user_family.Usuario_Id FROM user_family)");
		}
		
		List<Usuario> listUsuario = new ArrayList<Usuario>();
		Usuario usuario = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
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
