package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;

public class Usuario implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int id_familia;
	private String usuario;
	private String email;
	private String senha;
	private String confirmaSenha;
	private int nivel;
	private int ativo;
	private String nomeFamilia;
	private double saldoAtual; 
	private double saldoProx; 
	private String next;
	private String currentBallanceFormated;
	private String nextBallanceFormated;
	
	public double getSaldoAtual() {
		return saldoAtual;
	}
	public void setSaldoAtual(double saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	public double getSaldoProx() {
		return saldoProx;
	}
	public void setSaldoProx(double saldoProx) {
		this.saldoProx = saldoProx;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getNomeFamilia() {
		return nomeFamilia;
	}
	public void setNomeFamilia(String nomeFamilia) {
		this.nomeFamilia = nomeFamilia;
	}
	public int getAtivo() {
		return ativo;
	}
	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getConfirmaSenha() {
		return confirmaSenha;
	}
	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
	public int getId_familia() {
		return id_familia;
	}
	public void setId_familia(int id_familia) {
		this.id_familia = id_familia;
	}
	public String getCurrentBallanceFormated() {
		return currentBallanceFormated;
	}
	public void setCurrentBallanceFormated(String currentBallanceFormated) {
		this.currentBallanceFormated = currentBallanceFormated;
	}
	public String getNextBallanceFormated() {
		return nextBallanceFormated;
	}
	public void setNextBallanceFormated(String nextBallanceFormated) {
		this.nextBallanceFormated = nextBallanceFormated;
	}
}
