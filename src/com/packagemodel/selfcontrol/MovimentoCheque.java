package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class MovimentoCheque implements Serializable{
	private int codigo;
	private Cheque cheque;
	private int folha;
	private float valor;
	private String status;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Cheque getCheque() {
		return cheque;
	}
	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}
	public int getFolha() {
		return folha;
	}
	public void setFolha(int folha) {
		this.folha = folha;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public MovimentoCheque(){
		this(0,new Cheque(), 0, 0, "");
	}
	
	public MovimentoCheque(int codigo, Cheque cheque, int folha, float valor, String status){
		this.codigo = codigo;
		this.cheque = cheque;
		this.folha = folha;
		this.valor = valor;
		this.status = status;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	getCodigo() + " - " + getCheque().getDescricao() + " - " + getValor();
				
	}

}
