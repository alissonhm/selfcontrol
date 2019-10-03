package com.packagecontroller.selfcontrol;

import java.util.List;

import com.packageDAO.selfcontrol.MovChequeDAO;
import com.packagemodel.selfcontrol.MovimentoCheque;

import android.content.Context;

public class CtrlMovCheque {
	private MovChequeDAO movChequeDAO;
	
	public CtrlMovCheque(Context contexto){
		movChequeDAO = new MovChequeDAO(contexto);
	}
	
	public boolean gravarMovCheque(MovimentoCheque movCheque) throws Exception{
		return movChequeDAO.inserir(movCheque);
	}
	
	public List<MovimentoCheque> buscaMovCheque() throws Exception{
		return movChequeDAO.buscar();		
	}
	
	public boolean deletarMovCheque(MovimentoCheque movCheque) throws Exception{
		return movChequeDAO.delete(movCheque);
	}
	
	public void editaMovCheque(MovimentoCheque movCheque) throws Exception{
		movChequeDAO.atualizar(movCheque);
	}
	
	public boolean validaCodigo(MovimentoCheque movCheque) throws Exception{
		return movChequeDAO.verificaMovCheque(movCheque);
	}
	
	public int buscaCodigo() throws Exception{
		return movChequeDAO.buscaCodigo();
	}
}


