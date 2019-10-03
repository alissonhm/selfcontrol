package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.CaixaDAO;
import com.packagemodel.selfcontrol.Caixa;


public class CtrlCaixa{
	private CaixaDAO caixaDAO;
	
	public CtrlCaixa(Context contexto){
		caixaDAO = new CaixaDAO(contexto);
	}
	
	public void gravarCaixa(Caixa caixa) throws Exception{
		caixaDAO.inserir(caixa);
	}
	
	public List<Caixa> buscaCaixa() throws Exception{
		return caixaDAO.buscar();		
	}
	
	public boolean deletarCaixa(Caixa caixa) throws Exception{
		return caixaDAO.delete(caixa);
	}
	
	public void editaCaixa(Caixa caixa) throws Exception{
		caixaDAO.atualizar(caixa);
	}
	
	public boolean validaCodigo(Caixa caixa) throws Exception{
		return caixaDAO.verificaCaixa(caixa);
	}
	
	public int buscaCodigo() throws Exception{
		return caixaDAO.buscaCodigo();
	}
}


