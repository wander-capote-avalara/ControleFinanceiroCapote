package br.com.ControleFinanceiroCapote.servicos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbc.JDBCContaDAO;
import br.com.ControleFinanceiroCapote.jdbc.JDBCFamiliaDAO;
import br.com.ControleFinanceiroCapote.jdbc.JDBCRendaDAO;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Invite;
import br.com.ControleFinanceiroCapote.objetos.Renda;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class FamiliaService {

	public FamiliaService() {
		// TODO Auto-generated constructor stub
	}

	public void addfamily(Familia family) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.inserir(family);
		conec.fecharConexao();
	}


	public void inviteUsers(Invite invite, int owner) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		if (jdbcFamilia.isLeader(owner))
			jdbcFamilia.inviteUsers(invite, owner);
		else 
			throw new Exception("Você precisa ser dono da familia para fazer essa operação!");
		conec.fecharConexao();
	}

	public List<Familia> getFamilies(int id) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		List<Familia> families = jdbcFamilia.getFamily(id);
		conec.fecharConexao();

		return families;
	}

	public Familia getFamilyById(int id) throws ValidationException {

		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		Familia family = jdbcFamilia.getFamilyById(id);
		conec.fecharConexao();

		return family;
	}

	public void deleteFamily(int id) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.deletaFamilia(id);
		conec.fecharConexao();
	}

	public List<Usuario> getFamilyMembers(int userId, int familyId) throws ValidationException {
		List<Usuario> users = new ArrayList<Usuario>();
		
		if(familyId == 0)
			return users;
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);	
		JDBCUsuarioDAO userMethods = new JDBCUsuarioDAO(conexao);
		users = userMethods.getUsersInfo(jdbcFamilia.getFamilyMembers(userId));
		conec.fecharConexao();
		return users;
	}

	public void leadProvider(int id, int userId) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		if (jdbcFamilia.isLeader(userId))
			jdbcFamilia.leadProvider(id); 
		else 
			throw new Exception("Você precisa ser dono da familia para fazer essa operação!");
		
		conec.fecharConexao();
	}
	
	public void kickUser(int id, int userId) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		conexao.setAutoCommit(false);
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		if (jdbcFamilia.isLeader(userId)){
			jdbcFamilia.kickUser(id);
			conexao.commit();
		}else{
			conexao.rollback();
			throw new Exception("Você precisa ser dono da familia para fazer essa operação!");
		}
		conec.fecharConexao();
	}
	
	public List<Conta> getAllFamilyBills(int userId, int familyId) throws ValidationException {
		List<Conta> bills = new ArrayList<Conta>();
		
		if(familyId == 0)
			return bills;
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCContaDAO jdbcConta = new JDBCContaDAO(conexao);	
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		bills = jdbcConta.getAllFamilyBills(jdbcFamilia.getFamilyByUserId(userId));
		conec.fecharConexao();
		return bills;
	}

	public List<Renda> getAllFamilyIncomes(int userId, int familyId) throws ValidationException {
		List<Renda> incomes = new ArrayList<Renda>();
		
		if(familyId == 0)
			return incomes;
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCRendaDAO jdbcRenda = new JDBCRendaDAO(conexao);	
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		incomes = jdbcRenda.getAllFamilyIncomes(jdbcFamilia.getFamilyByUserId(userId));
		conec.fecharConexao();
		return incomes;
	}

	public List<Invite> getInvites(int userId) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		List<Invite> invites = jdbcFamilia.getInvites(userId);
		conec.fecharConexao();

		return invites;
	}

	public List<Invite> getInvitesInfo(int userId) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		List<Invite> invites = jdbcFamilia.getInvitesInfo(userId);
		conec.fecharConexao();

		return invites;
	}

	public void declineInvite(int id, int userId) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.declineInvite(id, userId);
		conec.fecharConexao();
	}

	public void acceptInvite(int id, int userId) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		jdbcFamilia.acceptInvite(id, userId);
		conec.fecharConexao();
	}	
	
	public boolean hasFamily(int userId) throws ValidationException {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		boolean hasFamily = jdbcFamilia.hasFamily(userId);
		conec.fecharConexao();
		return hasFamily;
	}
	
	public int createFamily(Familia family, int userId) throws Exception {
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFamiliaDAO jdbcFamilia = new JDBCFamiliaDAO(conexao);
		int familyId = jdbcFamilia.createFamily(family, userId);
		conec.fecharConexao();
		return familyId;
	}
}
