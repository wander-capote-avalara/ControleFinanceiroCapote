package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.FamiliaDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Invite;
import br.com.ControleFinanceiroCapote.objetos.Usuario;
import br.com.ControleFinanceiroCapote.sendEmail.SendMail;
import br.com.ControleFinanceiroCapote.validacao.ValidaFamilia;
import br.com.ControleFinanceiroCapote.validacao.ValidaUsuario;

public class JDBCFamiliaDAO implements FamiliaDAO {

	private Connection conexao;

	public JDBCFamiliaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	ValidaFamilia validf = new ValidaFamilia();
	ValidaUsuario valid = new ValidaUsuario();

	@Override
	public void inserir(Familia family) throws ValidationException {

		if (!listUserValidadeById(family.getUsers())) {
			throw new ValidationException(
					"Insira somente usuários existentes!!");
		}

		if (family.getId() == 0) {
			validf.insertValidation(family);
			StringBuilder comando = new StringBuilder();

			comando.append("INSERT INTO familias ");
			comando.append("(Nome, Id_Usuario) VALUES (?,?)");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(),
						Statement.RETURN_GENERATED_KEYS);
				p.setString(1, family.getName());
				p.setString(2, family.getOwner());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					family.setId(rs.getInt(1));
				}
				updateFamilies(family.getUsers(), family.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			validf.updateValidation(family);
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE familias set Nome = ?, Id_Usuario = ? ");
			comando.append("WHERE Id_Familias = ?");

			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando.toString());
				p.setString(1, family.getName());
				p.setString(2, family.getOwner());
				p.setInt(3, family.getId());
				p.execute();

				updateFamilies(family.getUsers(), family.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int createFamily(Familia family, int userId)
			throws ValidationException {
		validf.familyNameValidation(family.getName());
		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO familias ");
		comando.append("(Nome, Id_Usuario) VALUES (?,?)");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString(),
					Statement.RETURN_GENERATED_KEYS);
			p.setString(1, family.getName());
			p.setInt(2, userId);
			p.execute();
			rs = p.getGeneratedKeys();
			if (rs.next()) {
				family.setId(rs.getInt(1));
			}
			afterCreate(userId, family.getId());
			return family.getId();
		} catch (Exception e) {
			throw new ValidationException();
		}

	}

	public void afterCreate(int userId, int idFamilia)
			throws ValidationException {

		valid.userValidation(userId);
		validf.familyValidation(idFamilia);

		StringBuilder comando = new StringBuilder();

		comando.append("INSERT INTO user_family (Familia_Id, Usuario_Id) ");
		comando.append("VALUES (?, ?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, idFamilia);
			p.setInt(2, userId);
			p.execute();
		} catch (Exception e) {
			throw new ValidationException();
		}
	}

	public boolean hasFamily(int userId) throws ValidationException {

		valid.userValidation(userId);

		StringBuilder comando = new StringBuilder();

		comando.append("SELECT Familia_Id FROM user_family ");
		comando.append("WHERE Usuario_Id = ?");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			rs = p.executeQuery();

			return rs.next() ? true : false;

		} catch (Exception e) {
			throw new ValidationException();
		}
	}

	public void inviteUsers(Invite invite, int ownerId) throws ValidationException {
		invite.setOwnerId(ownerId);
		validf.familyValidation(invite.getFamilyId());

		for (Integer id : invite.getUsersToInvite())
			valid.userValidation(id);

		StringBuilder comando = new StringBuilder();

		comando.append("INSERT INTO convites (id_family, id_user) ");
		comando.append("VALUES (?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			for (Integer userId : invite.getUsersToInvite()) {
				p.setInt(1, invite.getFamilyId());
				p.setInt(2, userId);
				p.execute();
				sendEmail(invite, userId);
			}
		} catch (Exception e) {
			throw new ValidationException();
		}

	}

	private void sendEmail(Invite invite, int userId) throws Exception {
		try{
			invite.setFamilyName(getFamilyNameByInvite(invite));
			invite.setOwnerName(getFamilyOwnerNameByInvite(invite));
			
			String assunto = "Convite da família: "+invite.getFamilyName().toUpperCase(),
				mensagem = "O usuário "+invite.getOwnerName().toUpperCase()+" esta lhe convidando para participar da "
						+ "sua familia: "+invite.getFamilyName();
			SendMail sm = new SendMail();
			sm.sendMail("controlefinanceirocapote@gmail.com",getEmailByUserId(userId),assunto,mensagem);		
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
	}
	private String getFamilyNameByInvite(Invite invite) throws ValidationException{
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT Nome FROM familias ");
		comando.append("WHERE Id_Familias = ?");

		PreparedStatement p;
		ResultSet rs = null;
		
		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, invite.getFamilyId());
			rs = p.executeQuery();
			
			while(rs.next())
				return rs.getString("Nome");

		} catch (Exception e) {
			throw new ValidationException();
		}
		return null;
	}
	
	private String getFamilyOwnerNameByInvite(Invite invite) throws ValidationException{
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT Usuario FROM usuarios ");
		comando.append("WHERE Id_Usuarios = ?");

		PreparedStatement p;
		ResultSet rs = null;
		
		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, invite.getOwnerId());
			rs = p.executeQuery();
			
			while(rs.next())
				return rs.getString("Usuario");

		} catch (Exception e) {
			throw new ValidationException();
		}
		return null;
	}

	private String getEmailByUserId(int userId) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT Email FROM usuarios ");
		comando.append("WHERE Id_Usuarios = ?");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			rs = p.executeQuery();
			
			while(rs.next())
				return rs.getString("Email");
		} catch (Exception e) {
			throw new ValidationException(
					"Erro ao buscar email!", e);
		}
		return null;
	}

	public boolean listUserValidadeById(List<Integer> usersId)
			throws ValidationException {

		for (Integer id : usersId)
			valid.userValidation(id);

		StringBuilder comando = new StringBuilder();
		comando.append("SELECT Id_Usuarios FROM usuarios ");
		comando.append("WHERE Id_Usuarios = ?");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			for (Integer id : usersId) {
				p.setInt(1, id);
				rs = p.executeQuery();
				if (!rs.next()) {
					return false;
				}
			}
		} catch (Exception e) {
			throw new ValidationException(
					"Adicione somente usuários existentes!", e);
		}
		return true;
	}

	public void updateFamilies(List<Integer> users, int idFamilia)
			throws ValidationException {

		validf.familyValidation(idFamilia);
		listUserValidadeById(users);

		beforeInsert(idFamilia);

		StringBuilder comando = new StringBuilder();

		comando.append("INSERT INTO user_family (Familia_Id, Usuario_Id)");
		comando.append("VALUES (?, ?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			for (Integer id : users) {
				p.setInt(1, idFamilia);
				p.setInt(2, id);
				p.execute();
			}
		} catch (Exception e) {
			throw new ValidationException();
		}
	}

	private void beforeInsert(int id) throws ValidationException {

		validf.familyValidation(id);
		if (getFamilyById(id) == null)
			throw new ValidationException();

		StringBuilder comando = new StringBuilder();

		comando.append("DELETE FROM user_family ");
		comando.append("WHERE Familia_Id = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);

			p.execute();
		} catch (Exception e) {
			throw new ValidationException("Erro ao deletar família!", e);
		}
	}

	@Override
	public List<Familia> getFamily(int id) throws ValidationException {

		StringBuilder comando = new StringBuilder();
		comando.append("SELECT fa.Id_Familias as idfamilia, ");
		comando.append("fa.Nome as nomeFamilia, us.Usuario as owner FROM familias fa ");
		comando.append("INNER JOIN user_family uf on uf.Familia_Id = fa.Id_Familias ");
		comando.append("INNER JOIN usuarios us ON us.Id_Usuarios = uf.Usuario_Id ");
		comando.append("WHERE fa.Id_Usuario = uf.Usuario_Id ");
		if (id != 0) {
			validf.familyValidation(id);
			if (getFamilyById(id) == null)
				throw new ValidationException();

			comando.append("AND ");
			comando.append("fa.Id_Familias = " + id);
		}

		List<Familia> listFamilias = new ArrayList<Familia>();
		Familia familia = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
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

			for (Familia fam : listFamilias) {
				try {
					fam.setNames(getUserByFamilyId(fam.getId()));
				} catch (Exception e) {
					fam.setNames("Sem integrantes");
				}
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return listFamilias;
	}

	public String getUserByFamilyId(int id) throws ValidationException {

		validf.familyValidation(id);

		if (getFamilyById(id) == null)
			throw new ValidationException();

		StringBuilder comando2 = new StringBuilder();
		comando2.append("SELECT uss.Usuario as user from usuarios uss ");
		comando2.append("INNER join user_family uf on uf.Usuario_Id = uss.Id_Usuarios ");
		comando2.append("INNER JOIN familias faa ON faa.Id_Familias = uf.Familia_Id ");
		if (id != 0) {
			comando2.append("WHERE faa.Id_Familias = " + id);
		}

		List<String> names = new ArrayList<String>();

		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs2 = stmt.executeQuery(comando2.toString());
			while (rs2.next()) {
				names.add(rs2.getString("user"));
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}

		String user = names.get(0);
		for (int i = 1; i < names.size(); i++) {
			user += ", ";
			user += names.get(i);
		}

		return user;
	}

	public void deletaFamilia(int id) throws Exception {

		validf.familyValidation(id);

		if (getUserByFamilyId(id) == null) {
			throw new ValidationException("Família inexistente!");
		}

		beforeInsert(id);

		StringBuilder comando = new StringBuilder();

		comando.append("DELETE FROM familias ");
		comando.append("WHERE Id_Familias = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);

			p.execute();
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public Familia getFamilyById(int id) throws ValidationException {

		validf.familyValidation(id);

		StringBuilder comando = new StringBuilder();
		comando.append("SELECT * FROM familias ");
		comando.append("WHERE Id_Familias = ?");

		PreparedStatement p;
		ResultSet rs = null;
		Familia newFamily = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			rs = p.executeQuery();
			if (rs.next()) {
				newFamily = new Familia();
				newFamily.setId(id);
				newFamily.setName(rs.getString("Nome"));
				newFamily.setUsersName(getUserAndId(id));
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return newFamily;
	}

	public List<Usuario> getUserAndId(int id) throws ValidationException {

		validf.familyValidation(id);
		
		List<Usuario> listUser = new ArrayList<Usuario>();

		StringBuilder comando2 = new StringBuilder();
		comando2.append("SELECT uss.Id_Usuarios AS userId, uss.Usuario AS user FROM usuarios uss ");
		comando2.append("INNER JOIN user_family uf ON uf.Usuario_Id = uss.Id_Usuarios ");
		comando2.append("INNER JOIN familias faa ON faa.Id_Familias = uf.Familia_Id ");
		if (id != 0) {
			comando2.append("WHERE faa.Id_Familias = " + id);
		}

		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs2 = stmt.executeQuery(comando2.toString());
			while (rs2.next()) {
				Usuario user = new Usuario();
				user.setId(rs2.getInt("userId"));
				user.setUsuario(rs2.getString("user"));
				listUser.add(user);
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return listUser;
	}

	public List<Usuario> getFamilyMembers(int userId) throws ValidationException {
		return getUserAndId(getFamilyByUserId(userId));
	}

	public int getFamilyByUserId(int userId) throws ValidationException {
		
		valid.userValidation(userId);
		
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT uf.Familia_Id as familyId ");
		comando.append("FROM user_family uf ");
		comando.append("WHERE uf.Usuario_Id = ?");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			rs = p.executeQuery();
			if (rs.next()) {
				return rs.getInt("familyId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public void leadProvider(int id) throws ValidationException {
		valid.userValidation(id);
		
		StringBuilder comando = new StringBuilder();

		comando.append("UPDATE familias fa SET Id_Usuario=? ");
		comando.append("WHERE fa.Id_familias = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.setInt(2, getFamilyByUserId(id));

			p.execute();
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public boolean isLeader(int id) throws ValidationException {
		valid.userValidation(id);
		
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT fa.Nome FROM familias fa ");
		comando.append("WHERE fa.Id_Usuario = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);

			return p.executeQuery().next();
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public void kickUser(int id) throws ValidationException {
		valid.userValidation(id);
		
		StringBuilder comando = new StringBuilder();

		comando.append("DELETE FROM user_family ");
		comando.append("WHERE Usuario_Id = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();
			if (isLeader(id)) {
				deleteFamily(id);
			}
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}

	private void deleteFamily(int id) throws ValidationException {
		valid.userValidation(id);
		
		StringBuilder comando = new StringBuilder();

		comando.append("DELETE FROM familias ");
		comando.append("WHERE Id_Usuario = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();
		} catch (Exception e) {
			throw new ValidationException("Ainda há convites pendentes com essa familia!",e);
		}

	}

	public List<Invite> getInvites(int userId) throws ValidationException {
		valid.userValidation(userId);
		
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT id_family as inviteFrom FROM convites c ");
		comando.append("WHERE c.id_user = ?");
		List<Invite> listInvite = new ArrayList<Invite>();
		try {
			PreparedStatement stmt = conexao.prepareStatement(comando
					.toString());
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Invite newInvite = new Invite();
				newInvite.setFamilyId(rs.getInt("inviteFrom"));

				listInvite.add(newInvite);
			}
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
		return listInvite;
	}

	public List<Invite> getInvitesInfo(int userId) throws ValidationException {
		valid.userValidation(userId);
		
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT u.Usuario AS userName, f.Nome AS familyName, c.id_family as familyId ");
		comando.append("FROM convites c ");
		comando.append("INNER JOIN familias f ON f.Id_Familias = c.id_family ");
		comando.append("INNER JOIN usuarios u ON u.Id_Usuarios = f.Id_Usuario ");
		comando.append("WHERE c.id_user = ? ");
		List<Invite> listInvite = new ArrayList<Invite>();
		try {
			PreparedStatement stmt = conexao.prepareStatement(comando
					.toString());
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Invite newInvite = new Invite();
				newInvite.setFamilyName(rs.getString("familyName"));
				newInvite.setOwnerName(rs.getString("userName"));
				newInvite.setFamilyId(rs.getInt("familyId"));

				listInvite.add(newInvite);
			}
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
		return listInvite;
	}

	public void declineInvite(int id, int userId) throws ValidationException {

		valid.userValidation(userId);
		validf.familyValidation(id);
		
		StringBuilder comando = new StringBuilder();

		comando.append("DELETE FROM convites ");
		comando.append("WHERE id_family = ? AND id_user = ?");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.setInt(2, userId);
			p.execute();
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public void acceptInvite(int id, int userId) throws ValidationException {

		valid.userValidation(userId);
		validf.familyValidation(id);
		
		declineInvite(id, userId);
		kickUser(userId);
		

		StringBuilder comando = new StringBuilder();

		comando.append("INSERT INTO user_family (Familia_Id, Usuario_Id) ");
		comando.append("VALUES (?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.setInt(2, userId);
			p.execute();
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
}
