package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class Conta implements Serializable{
	private int codigo;
	private Banco banco;
	private String agencia;
	private String conta;
	private String operacao;
	private String descricao;
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
		
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Conta(){
		this(0, new Banco(),"","","","");
	}
	
	public Conta(int codigo, Banco banco, String agencia, String conta, String operacao, String descricao){
		this.codigo = codigo;
		this.banco = banco;
		this.agencia = agencia;
		this.conta = conta;
		this.operacao = operacao;
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return	getCodigo() + " - " + getDescricao();
	}

}
