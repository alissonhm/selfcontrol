package com.packagecontroller.selfcontrol;
import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.BancoDAO;
import com.packagemodel.selfcontrol.Banco;



public class CtrlBanco {
	private BancoDAO bancoDAO;
	
	public CtrlBanco(Context contexto){
		bancoDAO = new BancoDAO(contexto);
	}
	
	public boolean gravarBanco(Banco banco) throws Exception{
		return bancoDAO.inserir(banco);
	}
	
	public List<Banco> buscaBanco() throws Exception{
		return bancoDAO.buscar();		
	}
	
	public boolean deletarBanco(Banco banco) throws Exception{
		return bancoDAO.delete(banco);
	}
	
	public void editaBanco(Banco banco) throws Exception{
		bancoDAO.atualizar(banco);
	}
	
	public boolean validaCodigo(Banco banco) throws Exception{
		return bancoDAO.verificaBanco(banco);
	}
	
	public int buscaCodigo() throws Exception{
		return bancoDAO.buscaCodigo();
	}
}


