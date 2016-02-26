package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;

public class Familia implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private int id;
		private String name;
		private String owner;
		
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
		
		
		
	}

