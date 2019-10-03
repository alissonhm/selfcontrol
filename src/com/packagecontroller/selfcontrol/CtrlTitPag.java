package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.BancoDAO;
import com.packageDAO.selfcontrol.TitPagDAO;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.TituloPagar;

public class CtrlTitPag {
	private TitPagDAO titPagDAO;
	
	public CtrlTitPag(Context contexto){
		titPagDAO = new TitPagDAO(contexto);
	}
	
	public boolean gravarTitPag(TituloPagar tituloPagar) throws Exception{
		return titPagDAO.inserir(tituloPagar);
	}
	
	public List<TituloPagar> buscaTitPag() throws Exception{
		return titPagDAO.buscar();		
	}
	
	public boolean deletarTitPag(TituloPagar tituloPagar) throws Exception{
		return titPagDAO.delete(tituloPagar);
	}
	
	public void editaTitPag(TituloPagar tituloPagar) throws Exception{
		titPagDAO.atualizar(tituloPagar);
	}
	
	public boolean validaCodigo(TituloPagar tituloPagar) throws Exception{
		return titPagDAO.verificaTitPag(tituloPagar);
	}
	
	public int buscaCodigo() throws Exception{
		return titPagDAO.buscaCodigo();
	}

}


