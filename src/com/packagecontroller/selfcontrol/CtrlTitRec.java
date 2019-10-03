package com.packagecontroller.selfcontrol;

import java.util.List;

import com.packageDAO.selfcontrol.TitRecDAO;
import com.packagemodel.selfcontrol.TituloReceber;

import android.content.Context;


public class CtrlTitRec {
	private TitRecDAO titRecDAO;
	
	public CtrlTitRec(Context contexto){
		titRecDAO = new TitRecDAO(contexto);
	}
	
	public boolean gravarTitRec(TituloReceber tituloReceber) throws Exception{
		return titRecDAO.inserir(tituloReceber);
	}
	
	public List<TituloReceber> buscaTitRec() throws Exception{
		return titRecDAO.buscar();		
	}
	
	public boolean deletarTitRec(TituloReceber tituloReceber) throws Exception{
		return titRecDAO.delete(tituloReceber);
	}
	
	public void editaTitRec(TituloReceber tituloReceber) throws Exception{
		titRecDAO.atualizar(tituloReceber);
	}
	
	public boolean validaCodigo(TituloReceber tituloReceber) throws Exception{
		return titRecDAO.verificaTitRec(tituloReceber);
	}
	
	public int buscaCodigo() throws Exception{
		return titRecDAO.buscaCodigo();
	}
}


