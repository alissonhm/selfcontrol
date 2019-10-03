package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class MovimentoCaixa implements Serializable{

	private int codigo;
	private Caixa caixa;
	private TipoMovimentoCaixa tipoMovimentoCaixa;
	private String data;
	private String documento;
	private float valor;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Caixa getCaixa() {
		return caixa;
	}
	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	public TipoMovimentoCaixa getTipoMovimentoCaixa(){
		return tipoMovimentoCaixa;
	}
	public void setTipoMovimentoCaixa(TipoMovimentoCaixa tipoMovimentoCaixa){
		this.tipoMovimentoCaixa = tipoMovimentoCaixa;
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
	
	public MovimentoCaixa(){
		this(0, new Caixa(), new TipoMovimentoCaixa(), "", "", 0);
	}
	
	public MovimentoCaixa(int codigo, Caixa caixa, TipoMovimentoCaixa tipoMovimentoCaixa, String data, String documento, float valor){
		this.codigo = codigo;
		this.caixa = caixa;
		this.tipoMovimentoCaixa = tipoMovimentoCaixa;
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