package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;

public class RangeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstMonth;
	private String firstYear;
	private String secondMonth;
	private String secondYear;
	
	public String getFirstMonth() {
		return firstMonth;
	}
	public void setFirstMonth(String firstMonth) {
		this.firstMonth = firstMonth;
	}
	public String getFirstYear() {
		return firstYear;
	}
	public void setFirstYear(String firstYear) {
		this.firstYear = firstYear;
	}
	public String getSecondMonth() {
		return secondMonth;
	}
	public void setSecondMonth(String secondMonth) {
		this.secondMonth = secondMonth;
	}
	public String getSecondYear() {
		return secondYear;
	}
	public void setSecondYear(String secondYear) {
		this.secondYear = secondYear;
	}
	
}
