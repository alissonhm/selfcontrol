package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Caixa;
import com.packagemodel.selfcontrol.MovimentoCaixa;
import com.packagemodel.selfcontrol.TipoMovimentoCaixa;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class MovCaixaDAO {
	private SQLiteDatabase db;
	
	public MovCaixaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(MovimentoCaixa movCaixa) throws Exception{
		try {
			ContentValues valores = new ContentValues();
			valores.put("mvcx_codigo", movCaixa.getCodigo());
			valores.put("mvcx_cxcodigo", movCaixa.getCaixa().getCodigo());
			valores.put("mvcx_tmcxcodigo",movCaixa.getTipoMovimentoCaixa().getCodigo());
			valores.put("mvcx_data", movCaixa.getData());
			valores.put("mvcx_documento", movCaixa.getDocumento());
			
			if (String.valueOf(movCaixa.getTipoMovimentoCaixa().getTipo()).equals("DE")){
				valores.put("mvcx_valor", 0 - movCaixa.getValor());
			}
			if (String.valueOf(movCaixa.getTipoMovimentoCaixa().getTipo()).equals("RE")){
				valores.put("mvcx_valor", movCaixa.getValor());
			}
			
			
			return db.insert("movcaixa", null, valores) > 0;

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(MovimentoCaixa movCaixa){
		ContentValues valores = new ContentValues();
		valores.put("mvcx_codigo", movCaixa.getCodigo());
		valores.put("mvcx_cxcodigo", movCaixa.getCaixa().getCodigo());
		valores.put("mvcx_tmcxcodigo",movCaixa.getTipoMovimentoCaixa().getCodigo());
		valores.put("mvcx_data", movCaixa.getData());
		valores.put("mvcx_documento", movCaixa.getDocumento());
		if (String.valueOf(movCaixa.getTipoMovimentoCaixa().getTipo()).equals("DE")){
			valores.put("mvcx_valor", 0 - movCaixa.getValor());
		}
		if (String.valueOf(movCaixa.getTipoMovimentoCaixa().getTipo()).equals("RE")){
			valores.put("mvcx_valor", movCaixa.getValor());
		}
		
		db.update("movcaixa", valores, "mvcx_codigo = " + movCaixa.getCodigo(), null);
	}
	
	public boolean delete(MovimentoCaixa movCaixa){
		db.delete("movcaixa", "mvcx_codigo = " + movCaixa.getCodigo(), null);
		db.delete("movcaixati", "mvcxti_mvcxcodigo = " + movCaixa.getCodigo(), null);
		db.delete("movcaixacv", "mvcxcv_mvcxcodigo = " + movCaixa.getCodigo(), null);
		return true;
	}
	
	public List<MovimentoCaixa> buscar(Caixa caixa) throws Exception{
		try {
			List<MovimentoCaixa> list = new ArrayList<MovimentoCaixa>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("movcaixa, caixa, tpmovcaixa");
			query.appendWhere("movcaixa.mvcx_cxcodigo = caixa.cx_codigo and movcaixa.mvcx_tmcxcodigo = tpmovcaixa.tmcx_codigo and mvcx_cxcodigo  = " + caixa.getCodigo());
			String[] colunas = new String[]{"tmcx_codigo", "tmcx_descricao", "tmcx_tipo", "cx_codigo", "cx_descricao", "mvcx_codigo", "mvcx_documento", "mvcx_data", "mvcx_valor"};
			Cursor cursor = query.query(db, colunas, null, null, null, null, "mvcx_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					TipoMovimentoCaixa tmcx = new TipoMovimentoCaixa(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
					Caixa cx = new Caixa(cursor.getInt(3), cursor.getString(4));
					MovimentoCaixa mc = new MovimentoCaixa();
					mc.setCodigo(cursor.getInt(5));
					mc.setCaixa(cx);
					mc.setTipoMovimentoCaixa(tmcx);
					mc.setDocumento(cursor.getString(6));
					mc.setData(cursor.getString(7));
					mc.setValor(cursor.getFloat(8));
					list.add(mc);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean verificaMovCaixa(MovimentoCaixa movCaixa) throws Exception{
		try {
		Cursor cursor = db.query("movcaixa", new String[]{"mvcx_documento"} , "mvcx_documento = '" + movCaixa.getDocumento() + "'", null, null, null, "mvcx_documento");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
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
	
	public float calculaSaldo(Caixa caixa) throws Exception{
		try {
			float saldo = 0;
			String[] colunas = new String[]{"sum(mvcx_valor)"};
			Cursor cursor = db.query("movcaixa", colunas, "mvcx_cxcodigo = " + caixa.getCodigo(), null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					saldo = cursor.getFloat(0);
				}	
			}
			else{
				saldo = 0;
			}
			return saldo;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void baixaTituloPagar(TituloPagar tituloPagar, MovimentoCaixa movCaixa) throws Exception{
		try{
			ContentValues valores = new ContentValues();
			valores.put("ti_codigo", tituloPagar.getCodigo());
			valores.put("ti_forcli", tituloPagar.getFornecedor());
			valores.put("ti_documento", tituloPagar.getDocumento());
			valores.put("ti_vencimento", tituloPagar.getVencimento());
			valores.put("ti_valor", tituloPagar.getValor());
			valores.put("ti_desconto", tituloPagar.getDesconto());
			valores.put("ti_acrescimo", tituloPagar.getAcrescimo());
			valores.put("ti_status", "F");
			valores.put("ti_saldo", 0);
			db.update("titulo", valores, "ti_codigo = " + tituloPagar.getCodigo(), null);
			
			ContentValues valoresti = new ContentValues();
			valoresti.put("mvcxti_mvcxcodigo", movCaixa.getCaixa().getCodigo());
			valoresti.put("mvcxti_ticodigo", tituloPagar.getCodigo());
			db.insert("movcaixati", null, valoresti);
		
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void baixaTituloReceber(TituloReceber tituloReceber, MovimentoCaixa movCaixa) throws Exception{
		try{
			ContentValues valores = new ContentValues();
			valores.put("ti_codigo", tituloReceber.getCodigo());
			valores.put("ti_forcli", tituloReceber.getCliente());
			valores.put("ti_documento", tituloReceber.getDocumento());
			valores.put("ti_vencimento", tituloReceber.getVencimento());
			valores.put("ti_valor", tituloReceber.getValor());
			valores.put("ti_desconto", tituloReceber.getDesconto());
			valores.put("ti_acrescimo", tituloReceber.getAcrescimo());
			valores.put("ti_status", "F");
			valores.put("ti_saldo", 0);
			db.update("titulo", valores, "ti_codigo = " + tituloReceber.getCodigo(), null);
			
			ContentValues valoresti = new ContentValues();
			valoresti.put("mvcxti_mvcxcodigo", movCaixa.getCaixa().getCodigo());
			valoresti.put("mvcxti_ticodigo", tituloReceber.getCodigo());
			db.insert("movcaixati", null, valoresti);
		
		} catch (Exception e) {
			throw e;
		}
	}
	
}
