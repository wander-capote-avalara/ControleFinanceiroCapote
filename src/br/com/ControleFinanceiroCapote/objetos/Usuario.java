package br.com.ControleFinanceiroCapote.objetos;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DecimalDV;

import br.com.ControleFinanceiroCapote.rest.UsuarioRest;

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
	private int saldoAtual; 
	private int saldoProx; 
	private String next;
	
	public int getSaldoAtual() {
		return saldoAtual;
	}
	public void setSaldoAtual(int saldoAtual) {
		this.saldoAtual = saldoAtual;
	}
	public int getSaldoProx() {
		return saldoProx;
	}
	public void setSaldoProx(int saldoProx) {
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
}
