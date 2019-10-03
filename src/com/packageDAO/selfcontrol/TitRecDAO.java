package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.TituloReceber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class TitRecDAO {
	private SQLiteDatabase db;
	
	public TitRecDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(TituloReceber tituloReceber) throws Exception{
		try {
			ContentValues valores = new ContentValues();
			valores.put("ti_codigo", tituloReceber.getCodigo());
			valores.put("ti_forcli", tituloReceber.getCliente());
			valores.put("ti_documento", tituloReceber.getDocumento());
			valores.put("ti_vencimento", tituloReceber.getVencimento());
			valores.put("ti_valor", tituloReceber.getValor());
			valores.put("ti_desconto", tituloReceber.getDesconto());
			valores.put("ti_acrescimo", tituloReceber.getAcrescimo());
			valores.put("ti_status", tituloReceber.getStatus());
			valores.put("ti_saldo", tituloReceber.getValor());
			valores.put("ti_tipo", "R");
			
			return db.insert("titulo", null, valores) > 0;

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(TituloReceber tituloReceber){
		ContentValues valores = new ContentValues();
		valores.put("ti_codigo", tituloReceber.getCodigo());
		valores.put("ti_forcli", tituloReceber.getCliente());
		valores.put("ti_documento", tituloReceber.getDocumento());
		valores.put("ti_vencimento", tituloReceber.getVencimento());
		valores.put("ti_valor", tituloReceber.getValor());
		valores.put("ti_desconto", tituloReceber.getDesconto());
		valores.put("ti_acrescimo", tituloReceber.getAcrescimo());
		valores.put("ti_status", tituloReceber.getStatus());
		valores.put("ti_saldo", tituloReceber.getSaldo());
		valores.put("ti_tipo", "R");
		
		db.update("titulo", valores, "ti_codigo = " + tituloReceber.getCodigo(), null);
	}
	
	public boolean delete(TituloReceber tituloReceber){
		db.delete("titulo", "ti_codigo = " + tituloReceber.getCodigo(), null);
			return true;
	}
	
	public List<TituloReceber> buscar() throws Exception{
		try {
			List<TituloReceber> list = new ArrayList<TituloReceber>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("titulo");
			query.appendWhere("ti_tipo = 'R' and ti_status = 'A'");
			String[] colunas = new String[]{"ti_codigo", "ti_forcli", "ti_documento", "ti_vencimento",
											"ti_valor", "ti_desconto", "ti_acrescimo", "ti_status", "ti_saldo"};
			Cursor cursor = query.query(db, colunas, null, null, null, null, "ti_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					TituloReceber t = new TituloReceber();
					t.setCodigo(cursor.getInt(0));
					t.setCliente(cursor.getString(1));
					t.setDocumento(cursor.getString(2));
					t.setVencimento(cursor.getString(3));
					t.setValor(cursor.getFloat(4));
					t.setDesconto(cursor.getFloat(5));
					t.setAcrescimo(cursor.getFloat(6));
					t.setStatus(cursor.getString(7));
					t.setSaldo(cursor.getFloat(8));
					list.add(t);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean verificaTitRec(TituloReceber tituloReceber) throws Exception{
		try {
		Cursor cursor = db.query("titulo", new String[]{"ti_documento"} , "ti_documento = '" + tituloReceber.getDocumento() + "'", null, null, null, "ti_documento");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
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
	
}
