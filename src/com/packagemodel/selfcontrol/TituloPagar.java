package com.packagemodel.selfcontrol;

import java.io.Serializable;
import java.util.Date;

public class TituloPagar implements Serializable{
	private int codigo;
	private String fornecedor;
	private String documento;
	private String vencimento;
	private float valor;
	private float desconto;
	private float acrescimo;
	private String status;
	private float saldo;
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getVencimento() {
		return vencimento;
	}
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public float getDesconto() {
		return desconto;
	}
	public void setDesconto(float desconto) {
		this.desconto = desconto;
	}
	public float getAcrescimo() {
		return acrescimo;
	}
	public void setAcrescimo(float acrescimo) {
		this.acrescimo = acrescimo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getSaldo() {
		return saldo;
	}
	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}
	public TituloPagar(){
		this(0,"","","",0,0,0,"",0);
	}
	
	public TituloPagar(int codigo, String fornecedor, String documento, String vencimento, float valor, float desconto, float acrescimo, String status, float saldo){
		this.codigo = codigo;
		this.fornecedor = fornecedor;
		this.documento = documento;
		this.vencimento = vencimento;
		this.valor = valor;
		this.desconto = desconto;
		this.acrescimo = acrescimo;
		this.status = status;
		this.saldo = saldo;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return	getDocumento() + " - " + 
				getFornecedor() + " - " + 
				getVencimento() + " - " +
				getSaldo();
	}
}
