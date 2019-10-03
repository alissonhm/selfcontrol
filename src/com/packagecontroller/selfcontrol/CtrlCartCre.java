package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.CartCreDAO;
import com.packagemodel.selfcontrol.CartCre;


public class CtrlCartCre {
private CartCreDAO cartCreDAO;
	
	public CtrlCartCre(Context contexto){
		cartCreDAO = new CartCreDAO(contexto);
	}
	
	public void gravarCartCre(CartCre cartCre) throws Exception{
		cartCreDAO.inserir(cartCre);
	}
	
	public List<CartCre> buscaCartCre() throws Exception{
		return cartCreDAO.buscar();		
	}
	
	public boolean deletarCartCre(CartCre cartCre) throws Exception{
		return cartCreDAO.delete(cartCre);
	}
	
	public void editaCartCre(CartCre cartCre) throws Exception{
		cartCreDAO.atualizar(cartCre);
	}
	
	public boolean validaDados(CartCre cartCre) throws Exception{
		return cartCreDAO.verificaCartao(cartCre);
	}
	
	public int buscaCodigo() throws Exception{
		return cartCreDAO.buscaCodigo();
	}

}


