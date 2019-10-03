package com.packagecontroller.selfcontrol;

import java.util.List;

import com.packageDAO.selfcontrol.CompraDAO;
import com.packagemodel.selfcontrol.Compra;
import com.packagemodel.selfcontrol.Pagamento;

import android.content.Context;



public class CtrlCompra {
	private CompraDAO compraDAO;

	public CtrlCompra(Context contexto){
		compraDAO = new CompraDAO(contexto);
	}
	
	public void gravarCompra(Compra compra) throws Exception{
		compraDAO.inserir(compra);
	}
	
	public List<Compra> buscaCompra() throws Exception{
		return compraDAO.buscar();		
	}
	
	public boolean deletarCompra(Compra compra) throws Exception{
		return compraDAO.delete(compra);
	}
	
	public void editaCompra(Compra compra) throws Exception{
		compraDAO.atualizar(compra);
	}
	
	public boolean validaDados(Compra compra) throws Exception{
		return compraDAO.verificaCompra(compra);
	}
	
	public int buscaCodigo() throws Exception{
		return compraDAO.buscaCodigo();
	}
	
	public boolean verificaFolhasCheque (Pagamento pgto) throws Exception{
		return compraDAO.verificaFolhasCheque(pgto);
	}

}


