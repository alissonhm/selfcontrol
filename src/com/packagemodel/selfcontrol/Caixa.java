package com.packagemodel.selfcontrol;

import java.io.Serializable;

public class Caixa implements Serializable{
	private int codigo;
	private String descricao;
	
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
	
	public Caixa(){
		this(0,"");
	}
	
	public Caixa(int codigo, String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	getCodigo() + " - " + getDescricao();
	}

}
