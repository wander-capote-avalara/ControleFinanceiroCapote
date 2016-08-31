package br.com.ControleFinanceiroCapote.objetos;

import java.util.List;

public class DataTable {
	List<?> data;
	
	public DataTable(List<?> data) {
		super();
		this.data = data;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
}
