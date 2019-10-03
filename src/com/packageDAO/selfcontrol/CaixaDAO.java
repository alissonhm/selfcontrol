package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Caixa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class CaixaDAO {
	private SQLiteDatabase db;
	
	public CaixaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public void  inserir(Caixa caixa) throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(pg_codigo)"};
			Cursor cursor = db.query("pagamento", colunas, null, null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}
			else{
				codigo = 0;
			}
			
			codigo++;
			
			ContentValues valorescx = new ContentValues();
			ContentValues valorespg = new ContentValues();
			ContentValues valorespcx = new ContentValues();
			
			valorescx.put("cx_codigo", caixa.getCodigo());
			valorescx.put("cx_descricao", caixa.getDescricao());
			
			valorespg.put("pg_codigo", codigo);
			valorespg.put("pg_descricao", caixa.getDescricao());
			valorespg.put("pg_tipo", "CX");
			
			valorespcx.put("pcx_pgcodigo", codigo);
			valorespcx.put("pcx_cxcodigo", caixa.getCodigo());
			
			db.insert("caixa", null, valorescx);
			db.insert("pagamento", null, valorespg);
			db.insert("pagcaixa", null, valorespcx);

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(Caixa caixa){
		int codigo = 0;
		Cursor cursor = db.query("pagcaixa", new String[]{"pcx_pgcodigo"} , "pcx_cxcodigo = " + caixa.getCodigo(), null, null, null, "pcx_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
		
		
		ContentValues valorescx = new ContentValues();
		ContentValues valorespg = new ContentValues();
				
		valorescx.put("cx_codigo", caixa.getCodigo());
		valorescx.put("cx_descricao", caixa.getDescricao());
		
		valorespg.put("pg_codigo", codigo);
		valorespg.put("pg_descricao", caixa.getDescricao());
		valorespg.put("pg_tipo", "CA");
		
		db.update("caixa", valorescx, "cx_codigo = " + caixa.getCodigo(), null);
		db.update("pagamento", valorespg, "pg_codigo = " + codigo, null);
		}
	}
	
	public boolean delete(Caixa caixa){
		int codigo = 0;
		Cursor cursor = db.query("pagcaixa", new String[]{"pcx_pgcodigo"} , "pcx_cxcodigo = " + caixa.getCodigo(), null, null, null, "pcx_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
			Cursor cursoruso = db.query("movcaixa", null, "mvcx_cxcodigo = " + caixa.getCodigo(), null, null, null, null);
			if (cursoruso.getCount() < 1){
				db.delete("pagcaixa", "pcx_cxcodigo = " + caixa.getCodigo() + " and pcx_pgcodigo = " + codigo, null);
				db.delete("pagamento", "pg_codigo = " + codigo, null);
				db.delete("caixa", "cx_codigo = " + caixa.getCodigo(), null);
				return true;
			}
		}
		
		return false;
	}
	
	public List<Caixa> buscar() throws Exception{
		try {
			List<Caixa> list = new ArrayList<Caixa>();
			String[] colunas = new String[]{"cx_codigo", "cx_descricao"};
			Cursor cursor = db.query("caixa", colunas, null, null, null, null, "cx_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Caixa c = new Caixa();
					c.setCodigo(cursor.getInt(0));
					c.setDescricao(cursor.getString(1));
					list.add(c);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean verificaCaixa(Caixa caixa) throws Exception{
		try {
		Cursor cursor = db.query("caixa", new String[]{"cx_codigo"} , "cx_codigo = " + caixa.getCodigo(), null, null, null, "cx_codigo");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(cx_codigo)"};
			Cursor cursor = db.query("caixa", colunas, null, null, null, null, null);
			
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
	
}
