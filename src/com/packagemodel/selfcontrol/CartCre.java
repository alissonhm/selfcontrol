package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class CartCre implements Serializable{
	private int codigo;
	private String nroCartao;
	private Conta conta;
	private String descricao;
	private String bandeira;
	private float limite;
	private int dtFechamento;
	private int dtVencimento;
	
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
	public float getLimite() {
		return limite;
	}
	public void setLimite(float limite) {
		this.limite = limite;
	}
	public int getDtFechamento() {
		return dtFechamento;
	}
	public void setDtFechamento(int dtFechamento) {
		this.dtFechamento = dtFechamento;
	}
	public int getDtVencimento() {
		return dtVencimento;
	}
	public void setDtVencimento(int dtVencimento) {
		this.dtVencimento = dtVencimento;
	}	
	public String getNroCartao() {
		return nroCartao;
	}
	public void setNroCartao(String nroCartao) {
		this.nroCartao = nroCartao;
	}
	public String getBandeira() {
		return bandeira;
	}
	public void setBandeira(String bandeira) {
		this.bandeira = bandeira;
	}
	public CartCre(){
		this(0,"",new Conta(),"","",0,0,0);
	}
	
	public CartCre(int codigo, String nroCartao, Conta conta, String descricao, String bandeira, float limite, int dtFechamento, int dtVencimento){
		this.codigo = codigo;
		this.nroCartao = nroCartao;
		this.conta = conta;
		this.descricao = descricao;
		this.bandeira = bandeira;
		this.limite = limite;
		this.dtFechamento = dtFechamento;
		this.dtVencimento = dtVencimento;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getCodigo() + " - " + getDescricao();
	}
}
