package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.ContaDAO;
import com.packageDAO.selfcontrol.PagamentoDAO;
import com.packagemodel.selfcontrol.Pagamento;

public class CtrlPagto {
	private PagamentoDAO pagtoDAO;
	
	public CtrlPagto(Context contexto){
		pagtoDAO = new PagamentoDAO(contexto);
	}
	
	public List<Pagamento> buscaPagamentosPagar() throws Exception{
		return pagtoDAO.buscarPagar();		
	}
	
	public List<Pagamento> buscaPagamentosReceber() throws Exception{
		return pagtoDAO.buscarReceber();		
	}

}
