package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.ContaDAO;
import com.packageDAO.selfcontrol.PagamentoDAO;
import com.packageDAO.selfcontrol.TipoMovCaixaDAO;
import com.packageDAO.selfcontrol.TipoMovContaDAO;
import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.TipoMovimentoCaixa;
import com.packagemodel.selfcontrol.TipoMovimentoConta;

public class CtrlTipoMovConta {
	private TipoMovContaDAO tipoMovContaDAO;
	
	public CtrlTipoMovConta(Context contexto){
		tipoMovContaDAO = new TipoMovContaDAO(contexto);
	}
	
	public List<TipoMovimentoConta> buscar() throws Exception{
		return tipoMovContaDAO.buscar();		
	}

}
