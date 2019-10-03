package com.packagecontroller.selfcontrol;

import java.util.List;

import com.packageDAO.selfcontrol.MovCaixaDAO;
import com.packagemodel.selfcontrol.Caixa;
import com.packagemodel.selfcontrol.MovimentoCaixa;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.content.Context;

public class CtrlMovCaixa {
	private MovCaixaDAO movCaixaDAO;
	
	public CtrlMovCaixa(Context contexto){
		movCaixaDAO = new MovCaixaDAO(contexto);
	}
	
	public boolean gravarMovCaixa(MovimentoCaixa movCaixa) throws Exception{
		return movCaixaDAO.inserir(movCaixa);
	}
	
	public List<MovimentoCaixa> buscaMovCaixa(Caixa caixa) throws Exception{
		return movCaixaDAO.buscar(caixa);		
	}
	
	public boolean deletarMovCaixa(MovimentoCaixa movCaixa) throws Exception{
		return movCaixaDAO.delete(movCaixa);
	}
	
	public void editaMovCaixa(MovimentoCaixa movCaixa) throws Exception{
		movCaixaDAO.atualizar(movCaixa);
	}
	
	public boolean validaCodigo(MovimentoCaixa movCaixa) throws Exception{
		return movCaixaDAO.verificaMovCaixa(movCaixa);
	}
	
	public int buscaCodigo() throws Exception{
		return movCaixaDAO.buscaCodigo();
	}
	
	public float calculaSaldo(Caixa caixa) throws Exception{
		return movCaixaDAO.calculaSaldo(caixa);
	}
	
	public void baixaTituloPagar(TituloPagar tituloPagar, MovimentoCaixa movCaixa) throws Exception{
		movCaixaDAO.baixaTituloPagar(tituloPagar, movCaixa);
	}
	
	public void baixaTituloReceber(TituloReceber tituloReceber, MovimentoCaixa movCaixa) throws Exception{
		movCaixaDAO.baixaTituloReceber(tituloReceber, movCaixa);
	}
}



