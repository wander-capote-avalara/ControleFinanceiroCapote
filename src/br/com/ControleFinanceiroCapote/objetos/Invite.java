package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.util.List;

public class Invite implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int familyOwner;
	private List<Integer> usersToInvite;
	
	
	public int getFamilyOwner() {
		return familyOwner;
	}
	public void setFamilyOwner(int familyOwner) {
		this.familyOwner = familyOwner;
	}
	public List<Integer> getUsersToInvite() {
		return usersToInvite;
	}
	public void setUsersToInvite(List<Integer> usersToInvite) {
		this.usersToInvite = usersToInvite;
	}

}
