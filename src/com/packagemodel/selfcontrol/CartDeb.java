package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class CartDeb implements Serializable{
	private int codigo;
	private Conta conta;
	private String descricao;
	private String bandeira;
	private String nroCart;
	
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
	public String getBandeira() {
		return bandeira;
	}
	public void setBandeira(String banceira) {
		this.bandeira = banceira;
	}
	
	public String getNroCart() {
		return nroCart;
	}
	public void setNroCart(String nroCart) {
		this.nroCart = nroCart;
	}

	
	public CartDeb(){
		this(0,new Conta(),"","","");
	}
	
	public CartDeb(int codigo, Conta conta, String descricao, String bandeira, String nroCart){
		this.codigo = codigo;
		this.conta = conta;
		this.descricao = descricao;
		this.bandeira = bandeira;
		this.nroCart = nroCart;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getCodigo() + " - " + getDescricao();
	}

}