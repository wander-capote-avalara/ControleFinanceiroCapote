package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.sql.Date;

public class Conta implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	private String description;
	private int times;
	private double totalValue;
	private Date startDate;
	private String formatedDate;
	private int hasDeadline;
	private int categoria;
	private String categoriaName;
	private int userId;
	private String userName;
	private int status;
	private double parcelValue;
	private String formatedTotalValue;
	private String totalValueString;
	

	public String getFormatedDate() {
		return formatedDate;
	}
	public void setFormatedDate(String FormatedDate) {
		this.formatedDate = FormatedDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String UserName) {
		this.userName = UserName;
	}
	public int getHasDeadline() {
		return hasDeadline;
	}
	public void setHasDeadline(int hasDeadline) {
		this.hasDeadline = hasDeadline;
	}
	public double getParcelValue() {
		return parcelValue;
	}
	public void setParcelValue(double parcelValue) {
		this.parcelValue = parcelValue;
	}
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
	public double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getFormatedTotalValue() {
		return formatedTotalValue;
	}
	public void setFormatedTotalValue(String formatedTotalValue) {
		this.formatedTotalValue = formatedTotalValue;
	}
	public String getTotalValueString() {
		return totalValueString;
	}
	public void setTotalValueString(String totalValueString) {
		this.totalValueString = totalValueString;
	}
}
