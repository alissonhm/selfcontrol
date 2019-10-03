package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class Venda implements Serializable{
	private int codigo;
	private String cliente;
	private String produto;
	private String data;
	private float valor;
	private Pagamento pagamento;
	private int qtdParcela;
	private int dtParcela;
	

	
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public int getQtdParcela() {
		return qtdParcela;
	}

	public void setQtdParcela(int qtdParcela) {
		this.qtdParcela = qtdParcela;
	}

	public int getDtParcela() {
		return dtParcela;
	}

	public void setDtParcela(int dtParcela) {
		this.dtParcela = dtParcela;
	}

	public Venda(){
		this(0,"","","",0,new Pagamento(),0,0);
	}
	
	public Venda(int codigo, String cliente, String produto, String data, float valor, Pagamento pagamento, int qtdParcela, int dtParcela){
		this.codigo = codigo;
		this.cliente = cliente;
		this.produto = produto;
		this.data = data;
		this.valor = valor;
		this.pagamento = pagamento;
		this.qtdParcela = qtdParcela;
		this.dtParcela = dtParcela;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	getCodigo() + " - " + getData() + " - " + getValor();
				
	}
}