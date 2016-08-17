package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.UsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;
import br.com.ControleFinanceiroCapote.validacao.ValidaUsuario;

public class JDBCUsuarioDAO implements UsuarioDAO {

	private Connection conexao;

	public JDBCUsuarioDAO(Connection conexao) {
		this.conexao = conexao;
	}

	ValidaUsuario valid = new ValidaUsuario();

	public boolean inserir(Usuario user) throws ValidationException {
		valid.insertValidation(user);
		int id = user.getId(), familyId = user.getId_familia();

		if (id == 0) {
			String comando = "insert into usuarios " + "(Usuario, Senha, Email, Nivel, Ativo) " + "values(?,?,?,?,?)";

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando, Statement.RETURN_GENERATED_KEYS);
				p.setString(1, user.getUsuario());
				p.setString(2, user.getSenha());
				p.setString(3, user.getEmail());
				p.setInt(4, user.getNivel());
				p.setInt(5, 1);

				p.executeUpdate();
				rs = p.getGeneratedKeys();
				if (familyId != 0 && rs.next()) {
					id = rs.getInt(1);
					setFamily(id, familyId, true);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			String comando = "Update usuarios set Usuario=?, Senha=?, Email=?, Nivel=?, Ativo=? ";
			comando += "where Id_Usuarios=?";

			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando);
				p.setString(1, user.getUsuario());
				p.setString(2, user.getSenha());
				p.setString(3, user.getEmail());
				p.setInt(4, user.getNivel());
				p.setInt(5, user.getAtivo());
				p.setInt(6, user.getId());

				p.execute();
				if (familyId != 0) {
					setFamily(id, familyId, getFamilyByUserID(id) == 0 ? true : false);
				}
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
		} else {
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
			comando.append("WHERE usuarios.Usuario LIKE '%" + text + "%'");
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
		// getUsers().forEach((user) -> user.getId());
		// getUsers().stream().filter(user -> user.getId() ==
		// id).findAny().isPresent();

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
	public Usuario authUser(Usuario user) throws SQLException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT u.Id_Usuarios as userid, u.Usuario as user, ");
		comando.append("u.Email as email, u.Nivel as nivel, uf.Familia_Id as familyId ");
		comando.append(" FROM usuarios u ");
		comando.append("LEFT JOIN user_family uf ON uf.Usuario_Id = u.Id_Usuarios ");
		if (!user.equals("null") || !user.equals("")) {
			comando.append("WHERE Usuario=? AND Senha=?");
			comando.append(" AND ");
			comando.append("Ativo > 0");
		}
		PreparedStatement p;
		ResultSet rs = null;
		Usuario usuario = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setString(1, user.getUsuario());
			p.setString(2, user.getSenha());
			rs = p.executeQuery();

			while (rs.next()) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("userid"));
				usuario.setUsuario(rs.getString("user"));
				usuario.setEmail(rs.getString("email"));
				usuario.setNivel(rs.getInt("nivel"));
				usuario.setId_familia(rs.getInt("familyId"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}

	public Usuario getUserInfoById(int id) {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT a.Usuario as userName, c.Nome as familyName  FROM usuarios a ");
		comando.append("LEFT JOIN user_family b ON b.Usuario_Id = a.Id_Usuarios ");
		comando.append("LEFT JOIN familias c ON c.Id_Familias = b.Familia_Id ");
		comando.append("WHERE a.Id_Usuarios = ?");
		Usuario user = null ;
		try {
			PreparedStatement stmt = conexao.prepareStatement(comando.toString());
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			
		
			while (rs.next()) {
				user = new Usuario();
				user.setId(id);
				user.setUsuario(rs.getString("userName"));
				user.setNomeFamilia(rs.getString("familyName") == null ? "Sem familia" : rs.getString("familyName"));
				user.setSaldoAtual(getActualBalanceById(id, false));
				user.setNext(getNextBill(id));
				user.setSaldoProx(getNextBalanceById(id));
			}
			if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}

	private int getNextBalanceById(int id) {
		return getActualBalanceById(id, true) + getActualBalanceById(id, false);
	}

	private String getNextBill(int id) {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT a.Data_Vencimento as lastDate FROM contas a ");
		comando.append("WHERE a.Id_Usuario = "+id+" AND a.Status_Conta = 1 ");
		comando.append("ORDER BY lastDate DESC LIMIT 1");

		Date lastDate = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			
			while (rs.next()) {
				lastDate = rs.getDate("lastDate");
			}
			try {
				return new SimpleDateFormat("dd/MM/yyyy").format(lastDate);	
			} catch (Exception e) {
				return "Não há próxima fatura";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private int getActualBalanceById(int id, boolean next) {
		return getActualRentsById(id, next) - getActualBillsById(id, next);
	}
	
	private int getActualBillsById(int id, boolean next) {
		StringBuilder comando = new StringBuilder();
		int nextMonth = Calendar.getInstance().get(Calendar.MONTH) + 2,
			thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		comando.append("SELECT SUM(Valor_Contas) as vlrConta FROM contas a ");
		comando.append("WHERE a.Id_Usuario = "+id+" AND a.Status_Conta = 1 AND a.Conta_Fixa = 0");
		comando.append(next ? " AND MONTH(a.Data_Vencimento) = "+nextMonth : " AND MONTH(a.Data_Vencimento) <= "+thisMonth);

		int balance = 0;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			
			while (rs.next()) {
				balance += (int)rs.getInt("vlrConta");
			}
			
			balance += getParcelsValues(id, next);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return balance;
	}
	
	
	private double getParcelsValues(int id, boolean next) {
		StringBuilder comando = new StringBuilder();
		int nextMonth = Calendar.getInstance().get(Calendar.MONTH) + 2,
			thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		comando.append("SELECT SUM(Valor_Parcela) as vlrConta FROM parcela_conta a ");
		comando.append("WHERE a.Status_Parcela = 1 AND a.Id_Conta IN (SELECT c.Id_Contas FROM contas c where c.Id_Usuario = "+id+") AND MONTH(a.Data_Vencimento) = "+thisMonth);
		comando.append(next ? " AND MONTH(a.Data_Vencimento) = "+nextMonth : " AND MONTH(a.Data_Vencimento) <= "+thisMonth);

		double balance = 0;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			
			while (rs.next()) {
				balance += (int)rs.getInt("vlrConta");
			}
			
			return balance;		
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private int getActualRentsById(int id, boolean next) {
		StringBuilder comando = new StringBuilder();
		int nextMonth = Calendar.getInstance().get(Calendar.MONTH) + 2,
			thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		comando.append("SELECT SUM(Valor_Rendas) as vlrRenda FROM rendas a ");
		comando.append("WHERE a.Id_Usuario = "+id+" AND a.Status_Renda = 1");
		comando.append(next ? " AND MONTH(a.Data_Vencimento) = "+nextMonth : " AND MONTH(a.Data_Vencimento) <= "+thisMonth);
		comando.append(" OR ");
		comando.append("a.Data_Vencimento IS NULL AND a.Id_Usuario = "+id+" AND a.Status_Renda = 1 AND a.Renda_Fixa = 1");
		
		int balance = 0;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			
			while (rs.next()) {
				balance += (int)rs.getInt("vlrRenda");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return balance;
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
		StringBuilder comando = new StringBuilder();
		valid.userValidation(id);

		comando.append("SELECT * from usuarios ");
		comando.append("WHERE Id_Usuarios=" + id);
		Usuario user = new Usuario();
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				int idUser = rs.getInt("Id_Usuarios");
				String username = rs.getString("Usuario");
				int level = rs.getInt("Nivel");
				String email = rs.getString("Email");
				int ativo = rs.getInt("Ativo");
				user.setId(idUser);
				user.setId_familia(getFamilyByUserID(idUser));
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

	private int getFamilyByUserID(int idUser) {

		StringBuilder comandoSQL = new StringBuilder();
		int idFamilia = 0;
		comandoSQL.append("SELECT * FROM user_family ");
		comandoSQL.append("WHERE Usuario_Id = " + idUser);
		comandoSQL.append(" LIMIT 1");
		try {
			java.sql.Statement stmtt = conexao.createStatement();
			ResultSet rss = stmtt.executeQuery(comandoSQL.toString());
			while (rss.next()) {
				idFamilia = rss.getInt("Familia_Id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idFamilia;
	}

	public List<Usuario> getUsersInfo(List<Usuario> familyMembers) {
		List<Usuario> membersWInfo = new ArrayList<Usuario>();
		for (Usuario user : familyMembers) {
			membersWInfo.add(getUserInfoById(user.getId()));
		}
		return membersWInfo;
	}

}
