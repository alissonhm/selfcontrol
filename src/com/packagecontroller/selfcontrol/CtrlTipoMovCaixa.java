package com.packagecontroller.selfcontrol;

import java.util.List;

import android.content.Context;

import com.packageDAO.selfcontrol.ContaDAO;
import com.packageDAO.selfcontrol.PagamentoDAO;
import com.packageDAO.selfcontrol.TipoMovCaixaDAO;
import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.TipoMovimentoCaixa;

public class CtrlTipoMovCaixa {
	private TipoMovCaixaDAO tipoMovCaixaDAO;
	
	public CtrlTipoMovCaixa(Context contexto){
		tipoMovCaixaDAO = new TipoMovCaixaDAO(contexto);
	}
	
	public List<TipoMovimentoCaixa> buscar() throws Exception{
		return tipoMovCaixaDAO.buscar();		
	}

}
