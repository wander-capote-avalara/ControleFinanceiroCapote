package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.util.List;

public class Invite implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int familyOwner;
	private List<Integer> usersToInvite;
	private String familyName;
	private String ownerName;
	
	
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
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

}
