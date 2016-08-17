package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.sql.Date;

public class Parcela implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private double parcelValue;
	private int status;
	private Date paymentDate;
	private Date dueDate;
	private String formatedDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getFormatedDate() {
		return formatedDate;
	}
	public void setFormatedDate(String formatedDate) {
		this.formatedDate = formatedDate;
	}
	
}
