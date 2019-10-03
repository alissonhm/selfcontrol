package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.ChequeDAO;
import com.packagemodel.selfcontrol.Cheque;

public class CtrlCheque {
private ChequeDAO chequeDAO;
	
	public CtrlCheque(Context contexto){
		chequeDAO = new ChequeDAO(contexto);
	}
	
	public void gravarCheque(Cheque cheque) throws Exception{
		chequeDAO.inserir(cheque);
	}
	
	public List<Cheque> buscaCheque() throws Exception{
		return chequeDAO.buscar();		
	}
	
	public boolean deletarCheque(Cheque cheque) throws Exception{
		return chequeDAO.delete(cheque);
	}
	
	public void editaCheque(Cheque cheque) throws Exception{
		chequeDAO.atualizar(cheque);
	}
	
	public boolean validaDados(Cheque cheque) throws Exception{
		return chequeDAO.verificaCheque(cheque);
	}
	
	public int buscaCodigo() throws Exception{
		return chequeDAO.buscaCodigo();
	}

}


