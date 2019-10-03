package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class TipoMovimentoConta implements Serializable{
	private int codigo;
	private String descricao;
	private String tipo;
	
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public TipoMovimentoConta(){
		this(0,"","");
	}
	
	public TipoMovimentoConta(int codigo, String descricao, String tipo){
		this.codigo = codigo;
		this.descricao = descricao;
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	getDescricao();
				
	}

}
