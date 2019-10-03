package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class Cheque implements Serializable{
	private int codigo;
	private Conta conta;
	private String descricao;
	private int folhasRest;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Conta getConta() {
		return conta;
	}
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getFolhasRest() {
		return folhasRest;
	}
	public void setFolhasRest(int folhasRest) {
		this.folhasRest = folhasRest;
	}
	
	public Cheque(){
		this(0,new Conta(),"",0);
	}
	
	public Cheque(int codigo, Conta conta, String descricao, int folhasRest){
		this.codigo = codigo;
		this.conta = conta;
		this.descricao = descricao;
		this.folhasRest = folhasRest;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getCodigo() + " - " + getDescricao();
	}
	

}
