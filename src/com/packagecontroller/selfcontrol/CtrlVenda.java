package com.packagecontroller.selfcontrol;

import java.util.List;

import com.packageDAO.selfcontrol.VendaDAO;
import com.packagemodel.selfcontrol.Venda;

import android.content.Context;

public class CtrlVenda {
	private VendaDAO vendaDAO;

	public CtrlVenda(Context contexto){
		vendaDAO = new VendaDAO(contexto);
	}
	
	public void gravarVenda(Venda venda) throws Exception{
		vendaDAO.inserir(venda);
	}
	
	public List<Venda> buscaVenda() throws Exception{
		return vendaDAO.buscar();		
	}
	
	public boolean deletarVenda(Venda venda) throws Exception{
		return vendaDAO.delete(venda);
	}
	
	public void editaVenda(Venda venda) throws Exception{
		vendaDAO.atualizar(venda);
	}
	
	public boolean validaDados(Venda venda) throws Exception{
		return vendaDAO.verificaVenda(venda);
	}
	
	public int buscaCodigo() throws Exception{
		return vendaDAO.buscaCodigo();
	}

}


