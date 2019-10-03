package com.packageDAO.selfcontrol;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Compra;
import com.packagemodel.selfcontrol.Pagamento;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.format.DateFormat;

public class CompraDAO {
private SQLiteDatabase db;
	
	public CompraDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public void inserir(Compra compra) throws Exception{
		try {
			ContentValues valorescv = new ContentValues();
			
			valorescv.put("cv_codigo", compra.getCodigo());
			valorescv.put("cv_clifor", compra.getFornecedor());
			valorescv.put("cv_produto", compra.getProduto());
			valorescv.put("cv_data", compra.getData());
			valorescv.put("cv_valor", compra.getValor());
			valorescv.put("cv_pgcodigo", compra.getPagamento().getCodigo());
			valorescv.put("cv_qtdparc", compra.getQtdParcela());
			valorescv.put("cv_dtparc", compra.getDtParcela());
			valorescv.put("cv_tipo", "C");
			
			
			db.insert("compravenda", null, valorescv);
			
			if (String.valueOf(compra.getPagamento().getTipo()).equals("AP")){
				int cont = 1, mesInt, anoInt, diaInt, diaComp;
				float valorParc = compra.getValor() / compra.getQtdParcela();
				diaComp = Integer.parseInt(compra.getData().substring(0, 2));
				mesInt = Integer.parseInt(compra.getData().substring(3, 5));
				anoInt = Integer.parseInt(compra.getData().substring(6, 10));				 
				while (cont <= compra.getQtdParcela()){
					diaInt = compra.getDtParcela();
					
					if (diaInt < diaComp || cont > 1)
						mesInt++;
					if (mesInt == 2 && diaInt > 28)
						diaInt = 28;
					if ((mesInt == 4 || mesInt == 6 || mesInt == 9 || mesInt == 11) && diaInt > 30)
						diaInt = 30;
					
					if (mesInt > 12){
						mesInt = 1;
						anoInt++;
					}

					ContentValues valoresti = new ContentValues();
					valoresti.put("ti_codigo", buscaCodigoTitulo());
					valoresti.put("ti_forcli", compra.getFornecedor());
					valoresti.put("ti_documento", "Compra: " + compra.getCodigo() + ", parcela " + cont + "/" + compra.getQtdParcela());
					valoresti.put("ti_vencimento", diaInt + "/" + mesInt + "/" + anoInt);
					valoresti.put("ti_valor", valorParc);
					valoresti.put("ti_desconto", 0);
					valoresti.put("ti_acrescimo", 0);
					valoresti.put("ti_status", "A");
					valoresti.put("ti_saldo", valorParc);
					valoresti.put("ti_tipo", "P");
				
					db.insert("titulo", null, valoresti);
					cont++;					
				}
			}
			
			if (String.valueOf(compra.getPagamento().getTipo()).equals("CC")){
				int cont = 1, mesInt, anoInt, diaInt, diaComp;
				float valorParc = compra.getValor() / compra.getQtdParcela();
				diaComp = Integer.parseInt(compra.getData().substring(0, 2));
				mesInt = Integer.parseInt(compra.getData().substring(3, 5));
				anoInt = Integer.parseInt(compra.getData().substring(6, 10));				 
				while (cont <= compra.getQtdParcela()){
					diaInt = buscaFechamentoCartao(compra.getPagamento().getCodigo());
					
					if (diaInt < diaComp || cont > 1)
						mesInt++;
					if (mesInt == 2 && diaInt > 28)
						diaInt = 28;
					if ((mesInt == 4 || mesInt == 6 || mesInt == 9 || mesInt == 11) && diaInt > 30)
						diaInt = 30;
					
					if (mesInt > 12){
						mesInt = 1;
						anoInt++;
					}
					
					ContentValues valoresti = new ContentValues();
					valoresti.put("ti_codigo", buscaCodigoTitulo());
					valoresti.put("ti_forcli", compra.getFornecedor() + " / " + compra.getPagamento().getDescricao());
					valoresti.put("ti_documento", "Compra: " + compra.getCodigo() + ", parcela " + cont + "/" + compra.getQtdParcela());
					valoresti.put("ti_vencimento", diaInt + "/" + mesInt + "/" + anoInt);
					valoresti.put("ti_valor", valorParc);
					valoresti.put("ti_desconto", 0);
					valoresti.put("ti_acrescimo", 0);
					valoresti.put("ti_status", "A");
					valoresti.put("ti_saldo", valorParc);
					valoresti.put("ti_tipo", "CC");
				
					db.insert("titulo", null, valoresti);
					cont++;					
				}
			}
			
			if (String.valueOf(compra.getPagamento().getTipo()).equals("CH")){
				ContentValues valoresch = new ContentValues();
				valoresch.put("mc_codigo", buscaCodigoMovCheque());
				valoresch.put("mc_chcodigo", buscaCodigoCheque(compra.getPagamento().getCodigo()));
				valoresch.put("mc_folha", buscaFolhaCheque(buscaCodigoCheque(compra.getPagamento().getCodigo())));
				valoresch.put("mc_valor", compra.getValor());
				valoresch.put("mc_status", "A");
				
				db.insert("movcheque", null, valoresch);
				db.execSQL("update cheque set ch_folhas = ch_folhas - 1 where ch_codigo = " + buscaCodigoCheque(compra.getPagamento().getCodigo()) + ";");
			}
			
			if (String.valueOf(compra.getPagamento().getTipo()).equals("CX")){
				ContentValues valoresco = new ContentValues();
				valoresco.put("mvcx_codigo", buscaCodigoMovCaixa());
				valoresco.put("mvcx_cxcodigo", buscaPagamentoCaixa(compra.getPagamento()));
				valoresco.put("mvcx_tmcxcodigo", 1);
				valoresco.put("mvcx_data", compra.getData());
				valoresco.put("mvcx_documento", "Compra: " + compra.getCodigo());
				valoresco.put("mvcx_valor", 0 - compra.getValor());
				db.insert("movcaixa", null, valoresco);
				
				ContentValues valores = new ContentValues();
				valores.put("mvcxcv_mvcxcodigo", buscaPagamentoCaixa(compra.getPagamento()));
				valores.put("mvcxcv_cvcodigo", compra.getCodigo());
				db.insert("movcaixacv", null, valores);
			}
			
			if (String.valueOf(compra.getPagamento().getTipo()).equals("CD")){
				ContentValues valorescd = new ContentValues();
				valorescd.put("mvct_codigo", buscaCodigoMovConta());
				valorescd.put("mvct_ctcodigo", buscaPagamentoCartao(compra.getPagamento()));
				valorescd.put("mvct_tmctcodigo", 1);
				valorescd.put("mvct_data", compra.getData());
				valorescd.put("mvct_documento", "Compra: " + compra.getCodigo());
				valorescd.put("mvct_valor", 0 - compra.getValor());
				db.insert("movconta", null, valorescd);
				
				ContentValues valores = new ContentValues();
				valores.put("mvctca_mvctcodigo", buscaPagamentoCaixa(compra.getPagamento()));
				valores.put("mvctca_cacodigo", compra.getCodigo());
				db.insert("movcontaca", null, valores);
			}
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(Compra compra){
		ContentValues valorescv = new ContentValues();
		
		valorescv.put("cv_codigo", compra.getCodigo());
		valorescv.put("cv_clifor", compra.getFornecedor());
		valorescv.put("cv_produto", compra.getProduto());
		valorescv.put("cv_data", compra.getData());
		valorescv.put("cv_valor", compra.getValor());
		valorescv.put("cv_pgcodigo", compra.getPagamento().getCodigo());
		valorescv.put("cv_qtdparc", compra.getQtdParcela());
		valorescv.put("cv_dtparc", compra.getDtParcela());
		valorescv.put("cv_tipo", "C");
		
		db.update("compravenda", valorescv, "cv_codigo = " + compra.getCodigo(), null);
	}
	
	public boolean delete(Compra compra){
		db.delete("compravenda", "cv_codigo = " + compra.getCodigo(), null);
		return true;
	}
	

	public List<Compra> buscar() throws Exception{
		try {
			List<Compra> list = new ArrayList<Compra>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("compravenda,pagamento");
			query.appendWhere("compravenda.cv_pgcodigo = pagamento.pg_codigo and compravenda.cv_tipo = 'C'");
			String retorno[] = {"pagamento.pg_codigo", "pagamento.pg_descricao", "pagamento.pg_tipo", 
					"compravenda.cv_codigo", "compravenda.cv_clifor", "compravenda.cv_produto", "compravenda.cv_data", 
					"compravenda.cv_valor", "compravenda.cv_qtdparc", "compravenda.cv_dtparc"};
			Cursor cursor = query.query(db, retorno, null, null, null, null, "cv_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Pagamento pagamento = new Pagamento(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
					Compra c = new Compra();
					c.setCodigo(cursor.getInt(3));
					c.setFornecedor(cursor.getString(4));
					c.setProduto(cursor.getString(5));
					c.setData(cursor.getString(6));
					c.setValor(cursor.getFloat(7));
					c.setPagamento(pagamento);
					c.setQtdParcela(cursor.getInt(8));
					c.setDtParcela(cursor.getInt(9));
					list.add(c);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		} 
		
	} 
	
	public boolean verificaCompra(Compra compra) throws Exception{
		try {
		Cursor cursor = db.query("compravenda", new String[]{"cv_codigo"} , "cv_codigo= " + compra.getCodigo(), null, null, null, "cv_codigo");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(cv_codigo)"};
			Cursor cursor = db.query("compravenda", colunas, null, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			return (codigo + 1);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public int buscaCodigoTitulo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(ti_codigo)"};
			Cursor cursor = db.query("titulo", colunas, null, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			return (codigo + 1);
			} catch (Exception e) {
			throw e;
		}
		
	}
	
	public int buscaCodigoMovCheque() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(mc_codigo)"};
			Cursor cursor = db.query("movcheque", colunas, null, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			return (codigo + 1);
			} catch (Exception e) {
			throw e;
		}
		
	}
	
	public int buscaFechamentoCartao(int codigoPagamento){
			int codigo = 0, diaFechamento = 0;
			String[] colunasPagCartao = new String[]{"pca_cacodigo"};
			Cursor cursorPagamentoCartao = db.query("pagcartao", colunasPagCartao, "pca_pgcodigo = " + codigoPagamento, null, null, null, "pca_pgcodigo");
			if (cursorPagamentoCartao.getCount() > 0){
				while(cursorPagamentoCartao.moveToNext()){
					codigo = cursorPagamentoCartao.getInt(0);
				}
			}
			String[] colunasCartao = new String[]{"ca_fechamento"};
			Cursor cursorCartao = db.query("cartao", colunasCartao, "ca_codigo = " + codigo, null, null, null, "ca_vencimento");
			if (cursorCartao.getCount() > 0){
				while(cursorCartao.moveToNext()){
					diaFechamento = cursorCartao.getInt(0);
				}
			}
			return diaFechamento;
	}
	
	public int buscaCodigoCheque(int codigoPagamento){
		int codigo = 0;
		String[] colunasPagCheque = new String[]{"pch_chcodigo"};
		Cursor cursorPagamentoCheque = db.query("pagcheque", colunasPagCheque, "pch_pgcodigo = " + codigoPagamento, null, null, null, "pch_pgcodigo");
		if (cursorPagamentoCheque.getCount() > 0){
			while(cursorPagamentoCheque.moveToNext()){
				codigo = cursorPagamentoCheque.getInt(0);
			}
		}
		return codigo;
	}
	
	public int buscaFolhaCheque(int codigoCheque) throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(mc_folha)"};
			Cursor cursor = db.query("movcheque", colunas, "mc_chcodigo = " + codigoCheque, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			return (codigo + 1);
			} catch (Exception e) {
			throw e;
		}
		
	}
	
	public int buscaCodigoMovCaixa() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(mvcx_codigo)"};
			Cursor cursor = db.query("movcaixa", colunas, null, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			return (codigo + 1);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public int buscaCodigoMovConta() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(mvct_codigo)"};
			Cursor cursor = db.query("movconta", colunas, null, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			return (codigo + 1);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public int buscaPagamentoCaixa(Pagamento pagto) throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"pcx_cxcodigo"};
			Cursor cursor = db.query("pagcaixa", colunas, "pcx_pgcodigo = " + pagto.getCodigo(), null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}			
			return codigo;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaPagamentoCartao(Pagamento pagto) throws Exception{
		try {
			int codigoCartao = 0, codigoConta = 0;
			
			String[] colunas = new String[]{"pca_cacodigo"};
			Cursor cursor = db.query("pagcartao", colunas, "pca_pgcodigo = " + pagto.getCodigo(), null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigoCartao = cursor.getInt(0);
				}
			}	
			
			String[] colunas2 = new String[]{"ca_ctcodigo"};
			Cursor cursor2 = db.query("cartao", colunas2, "ca_codigo = " + codigoCartao, null, null, null, null);
			
			if (cursor2.getCount() > 0){
				while(cursor2.moveToNext()){
					codigoConta = cursor2.getInt(0);
				}
			}	
			return codigoConta;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public boolean verificaFolhasCheque(Pagamento pagto) throws Exception{
		try {
			int codigoCheque = 0, folhas = 0;
			
			String[] colunas = new String[]{"pch_chcodigo"};
			Cursor cursor = db.query("pagcheque", colunas, "pch_pgcodigo = " + pagto.getCodigo(), null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigoCheque = cursor.getInt(0);
				}
			}	
			
			String[] colunas2 = new String[]{"ch_folhas"};
			Cursor cursor2 = db.query("cheque", colunas2, "ch_codigo = " + codigoCheque, null, null, null, null);
			
			if (cursor2.getCount() > 0){
				while(cursor2.moveToNext()){
					folhas = cursor2.getInt(0);
				}
			}	
			
			if (folhas < 1)
				return false;
			else
				return true;
			
		} catch (Exception e) {
			throw e;
		}
	}
}

