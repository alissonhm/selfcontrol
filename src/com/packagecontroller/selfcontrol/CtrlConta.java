package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.BancoDAO;
import com.packageDAO.selfcontrol.ContaDAO;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;

public class CtrlConta {
	private ContaDAO contaDAO;
	
	public CtrlConta(Context contexto){
		contaDAO = new ContaDAO(contexto);
	}
	
	public boolean gravarConta(Conta conta) throws Exception{
		return contaDAO.inserir(conta);
	}
	
	public List<Conta> buscaConta() throws Exception{
		return contaDAO.buscar();		
	}
	
	public boolean deletarConta(Conta conta) throws Exception{
		return contaDAO.delete(conta);
	}
	
	public void editaConta(Conta conta) throws Exception{
		contaDAO.atualizar(conta);
	}
	
	public boolean validaDados(Conta conta) throws Exception{
		return contaDAO.verificaConta(conta);
	}
	
	public int buscaCodigo() throws Exception{
		return contaDAO.buscaCodigo();
	}

}


