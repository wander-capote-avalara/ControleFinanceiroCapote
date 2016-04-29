package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.util.Date;

public class Renda implements Serializable{

	private static final long serialVersionUID = 1L;

	int id;
	String description;
	int times;
	int totalValue;
	java.sql.Date startDate;
	int isFixed;
	int categoria;
	String categoriaName;
	int userId;
	int status;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCategoriaName() {
		return categoriaName;
	}
	public void setCategoriaName(String categoriaName) {
		this.categoriaName = categoriaName;
	}
	
	public int getCategoria() {
		return categoria;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}
	public int getIsFixed() {
		return isFixed;
	}
	public void setIsFixed(int isFixed) {
		this.isFixed = isFixed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(int totalValue) {
		this.totalValue = totalValue;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}
}
