package br.com.ControleFinanceiroCapote.jdbcinterface;

import java.util.List;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Invite;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public interface FamiliaDAO {

	public void inserir(Familia family) throws ValidationException;
	public List<Familia> getFamily(int id) throws ValidationException;
	public void acceptInvite(int id, int userId);
	public void declineInvite(int id, int userId);
	public void deletaFamilia(int id) throws Exception;
	public Familia getFamilyById(int id) throws ValidationException;
	public List<Usuario> getFamilyMembers(int userId) throws ValidationException;
	public List<Invite> getInvites(int userId);
	public List<Invite> getInvitesInfo(int userId); 
	public void inviteUsers(Invite invite) throws ValidationException;
	public void kickUser(int id);
	public void leadProvider(int id);
	public int getFamilyByUserId(int userId);
	public List<Usuario> getUserAndId(int id) throws ValidationException;
	public String getUserByFamilyId(int id) throws ValidationException;
	public boolean isLeader(int id);
	public boolean listUserValidadeById(List<Integer> usersId) throws ValidationException;
	public void updateFamilies(List<Integer> users,int  idFamilia) throws ValidationException;
}
