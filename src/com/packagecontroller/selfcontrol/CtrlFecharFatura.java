package com.packagecontroller.selfcontrol;

import android.content.Context;

import com.packageDAO.selfcontrol.FecharFaturaDAO;
import com.packagemodel.selfcontrol.CartCre;


public class CtrlFecharFatura {
	private FecharFaturaDAO faturaDAO;
	
	public CtrlFecharFatura(Context contexto){
		faturaDAO = new FecharFaturaDAO(contexto);
	}
	
	public boolean gravarFatura(CartCre cartCre, int mes, int ano) throws Exception{
		return faturaDAO.inserir(cartCre, mes, ano);
	}
}


