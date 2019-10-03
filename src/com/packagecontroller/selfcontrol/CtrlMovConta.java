package com.packagecontroller.selfcontrol;



import java.util.List;

import com.packageDAO.selfcontrol.MovContaDAO;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.MovimentoCheque;
import com.packagemodel.selfcontrol.MovimentoConta;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.content.Context;

public class CtrlMovConta {
	private MovContaDAO movContaDAO;
	
	public CtrlMovConta(Context contexto){
		movContaDAO = new MovContaDAO(contexto);
	}
	
	public boolean gravarMovConta(MovimentoConta movConta) throws Exception{
		return movContaDAO.inserir(movConta);
	}
	
	public List<MovimentoConta> buscaMovConta(Conta Conta) throws Exception{
		return movContaDAO.buscar(Conta);		
	}
	
	public boolean deletarMovConta(MovimentoConta movConta) throws Exception{
		return movContaDAO.delete(movConta);
	}
	
	public void editaMovConta(MovimentoConta movConta) throws Exception{
		movContaDAO.atualizar(movConta);
	}
	
	public boolean validaCodigo(MovimentoConta movConta) throws Exception{
		return movContaDAO.verificaMovConta(movConta);
	}
	
	public int buscaCodigo() throws Exception{
		return movContaDAO.buscaCodigo();
	}
	
	public float calculaSaldo(Conta conta) throws Exception{
		return movContaDAO.calculaSaldo(conta);
	}
	
	public void baixaTituloPagar(TituloPagar tituloPagar, MovimentoConta movConta) throws Exception{
		movContaDAO.baixaTituloPagar(tituloPagar, movConta);
	}
	
	public void baixaTituloReceber(TituloReceber tituloReceber, MovimentoConta movConta) throws Exception{
		movContaDAO.baixaTituloReceber(tituloReceber, movConta);
	}
	
	public void compensaCheque(MovimentoCheque movimentoCheque, MovimentoConta movConta) throws Exception{
		movContaDAO.compensaCheque(movimentoCheque, movConta);
	}
}



