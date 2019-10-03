package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class MovimentoConta implements Serializable{

	private int codigo;
	private Conta conta;
	private TipoMovimentoConta tipoMovimentoConta;
	private String data;
	private String documento;
	private float valor;
	
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
		this.conta= conta;
	}
	public TipoMovimentoConta getTipoMovimentoConta(){
		return tipoMovimentoConta;
	}
	public void setTipoMovimentoConta(TipoMovimentoConta tipoMovimentoConta){
		this.tipoMovimentoConta = tipoMovimentoConta;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	
	public MovimentoConta(){
		this(0, new Conta(), new TipoMovimentoConta(), "", "", 0);
	}
	
	public MovimentoConta(int codigo, Conta conta, TipoMovimentoConta tipoMovimentoConta, String data, String documento, float valor){
		this.codigo = codigo;
		this.conta = conta;
		this.tipoMovimentoConta = tipoMovimentoConta;
		this.data = data;
		this.documento = documento;
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	getCodigo() + " - " + getDocumento() + " - " + getValor();
				
	}
}