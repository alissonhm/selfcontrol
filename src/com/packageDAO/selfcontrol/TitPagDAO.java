package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.TituloPagar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;


public class TitPagDAO {
	private SQLiteDatabase db;
	
	public TitPagDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(TituloPagar tituloPagar) throws Exception{
		try {
			ContentValues valores = new ContentValues();
			valores.put("ti_codigo", tituloPagar.getCodigo());
			valores.put("ti_forcli", tituloPagar.getFornecedor());
			valores.put("ti_documento", tituloPagar.getDocumento());
			valores.put("ti_vencimento", tituloPagar.getVencimento());
			valores.put("ti_valor", tituloPagar.getValor());
			valores.put("ti_desconto", tituloPagar.getDesconto());
			valores.put("ti_acrescimo", tituloPagar.getAcrescimo());
			valores.put("ti_status", tituloPagar.getStatus());
			valores.put("ti_saldo", tituloPagar.getValor());
			valores.put("ti_tipo", "P");
			
			return db.insert("titulo", null, valores) > 0;

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(TituloPagar tituloPagar){
		ContentValues valores = new ContentValues();
		valores.put("ti_codigo", tituloPagar.getCodigo());
		valores.put("ti_forcli", tituloPagar.getFornecedor());
		valores.put("ti_documento", tituloPagar.getDocumento());
		valores.put("ti_vencimento", tituloPagar.getVencimento());
		valores.put("ti_valor", tituloPagar.getValor());
		valores.put("ti_desconto", tituloPagar.getDesconto());
		valores.put("ti_acrescimo", tituloPagar.getAcrescimo());
		valores.put("ti_status", tituloPagar.getStatus());
		valores.put("ti_saldo", tituloPagar.getSaldo());
		
		db.update("titulo", valores, "ti_codigo = " + tituloPagar.getCodigo(), null);
	}
	
	public boolean delete(TituloPagar tituloPagar){
		db.delete("titulo", "ti_codigo = " + tituloPagar.getCodigo(), null);
			return true;
	}
	
	public List<TituloPagar> buscar() throws Exception{
		try {
			List<TituloPagar> list = new ArrayList<TituloPagar>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("titulo");
			query.appendWhere("(ti_tipo = 'P' or ti_tipo = 'CC') and ti_status = 'A' and ti_saldo > 0");
			String[] colunas = new String[]{"ti_codigo", "ti_forcli", "ti_documento", "ti_vencimento",
											"ti_valor", "ti_desconto", "ti_acrescimo", "ti_status", "ti_saldo"};
			Cursor cursor = query.query(db, colunas, null, null, null, null, "ti_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					TituloPagar t = new TituloPagar();
					t.setCodigo(cursor.getInt(0));
					t.setFornecedor(cursor.getString(1));
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
	
	public boolean verificaTitPag(TituloPagar tituloPagar) throws Exception{
		try {
		Cursor cursor = db.query("titulo", new String[]{"ti_documento"} , "ti_documento = '" + tituloPagar.getDocumento() + "'", null, null, null, "ti_documento");
			
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
