package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.util.List;

public class Familia implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private int id;
		private String name;
		private String owner;
		private List<Integer> users;
		private List<Usuario> usersName;
		private String names;
		
		public String getNames() {
			return names;
		}
		public void setNames(String names) {
			this.names = names;
		}
		public List<Usuario> getUsersName() {
			return usersName;
		}
		public void setUsersName(List<Usuario> usersName) {
			this.usersName = usersName;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public List<Integer> getUsers() {
			return users;
		}
		public void setUsers(List<Integer> users) {
			this.users = users;
		}	
	}
