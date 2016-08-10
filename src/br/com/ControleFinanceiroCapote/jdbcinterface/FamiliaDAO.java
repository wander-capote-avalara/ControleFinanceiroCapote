package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Invite;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public interface FamiliaDAO {

	public void inserir(Familia family) throws ValidationException;
	public List<Familia> getFamily(int id);
	public void acceptInvite(int id, int userId);
	public void declineInvite(int id, int userId);
	public void deletaFamilia(int id) throws Exception;
	public Familia getFamilyById(int id);
	public List<Usuario> getFamilyMembers(int userId);
	public List<Invite> getInvites(int userId);
	public List<Invite> getInvitesInfo(int userId); 
	public void inviteUsers(Invite invite);
	public void kickUser(int id);
	public void leadProvider(int id);
	public int getFamilyByUserId(int userId);
	public List<Usuario> getUserAndId(int id);
	public String getUserByFamilyId(int id);
	public boolean isLeader(int id);
	public boolean listUserValidadeById(List<Integer> usersId);
	public void updateFamilies(List<Integer> users,int  idFamilia);
}
