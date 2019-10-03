package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.CartDebDAO;
import com.packagemodel.selfcontrol.CartDeb;

public class CtrlCartDeb {
private CartDebDAO cartDebDAO;
	
	public CtrlCartDeb(Context contexto){
		cartDebDAO = new CartDebDAO(contexto);
	}
	
	public void gravarCartDeb(CartDeb cartDeb) throws Exception{
		cartDebDAO.inserir(cartDeb);
	}
	
	public List<CartDeb> buscaCartDeb() throws Exception{
		return cartDebDAO.buscar();		
	}
	
	public boolean deletarCartDeb(CartDeb cartDeb) throws Exception{
		return cartDebDAO.delete(cartDeb);
	}
	
	public void editaCartDeb(CartDeb cartDeb) throws Exception{
		cartDebDAO.atualizar(cartDeb);
	}
	
	public boolean validaDados(CartDeb cartDeb) throws Exception{
		return cartDebDAO.verificaCartao(cartDeb);
	}
	
	public int buscaCodigo() throws Exception{
		return cartDebDAO.buscaCodigo();
	}

}


