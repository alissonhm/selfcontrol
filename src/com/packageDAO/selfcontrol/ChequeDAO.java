package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Cheque;
import com.packagemodel.selfcontrol.Conta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class ChequeDAO {
private SQLiteDatabase db;
	
	public ChequeDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public void  inserir(Cheque cheque) throws Exception{
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
			
			ContentValues valoresch = new ContentValues();
			ContentValues valorespg = new ContentValues();
			ContentValues valorespch = new ContentValues();
			
			valoresch.put("ch_codigo", cheque.getCodigo());
			valoresch.put("ch_ctcodigo", cheque.getConta().getCodigo());
			valoresch.put("ch_descricao", cheque.getDescricao());
			valoresch.put("ch_folhas", cheque.getFolhasRest());
			
			valorespg.put("pg_codigo", codigo);
			valorespg.put("pg_descricao", cheque.getDescricao());
			valorespg.put("pg_tipo", "CH");
			
			valorespch.put("pch_pgcodigo", codigo);
			valorespch.put("pch_chcodigo", cheque.getCodigo());
			
			db.insert("cheque", null, valoresch);
			db.insert("pagamento", null, valorespg);
			db.insert("pagcheque", null, valorespch);

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(Cheque cheque){
		int codigo = 0;
		Cursor cursor = db.query("pagcheque", new String[]{"pch_pgcodigo"} , "pch_chcodigo = " + cheque.getCodigo(), null, null, null, "pch_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
			
		ContentValues valoresch = new ContentValues();
		ContentValues valorespg = new ContentValues();
		
		valoresch.put("ch_codigo", cheque.getCodigo());
		valoresch.put("ch_ctcodigo", cheque.getConta().getCodigo());
		valoresch.put("ch_descricao", cheque.getDescricao());
		valoresch.put("ch_folhas", cheque.getFolhasRest());
		
		valorespg.put("pg_codigo", codigo);
		valorespg.put("pg_descricao", cheque.getDescricao());
		valorespg.put("pg_tipo", "CH");
		
		db.update("cheque", valoresch, "ch_codigo = " + cheque.getCodigo(), null);
		db.update("pagamento", valorespg, "pg_codigo = " + codigo, null);
		}
	}
	
	public boolean delete(Cheque cheque){
		int codigo = 0;
		Cursor cursor = db.query("pagcheque", new String[]{"pch_pgcodigo"} , "pch_chcodigo = " + cheque.getCodigo(), null, null, null, "pch_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
		Cursor cursoruso = db.query("movcheque", null, "mc_chcodigo = " + cheque.getCodigo(), null, null, null, null);
		if (cursoruso.getCount() < 1){
			db.delete("pagcheque", "pch_chcodigo = " + cheque.getCodigo() + " and pch_pgcodigo = " + codigo, null);
			db.delete("pagamento", "pg_codigo = " + codigo, null);	
			db.delete("cheque", "ch_codigo = " + cheque.getCodigo(), null);
			return true;
			}
		}
		return false;
	}
	

	public List<Cheque> buscar() throws Exception{
		try {
			List<Cheque> list = new ArrayList<Cheque>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("conta,cheque,banco");
			query.appendWhere("conta.ct_codigo = cheque.ch_ctcodigo and ct_bccodigo = bc_codigo");
			String retorno[] = {"conta.ct_codigo","conta.ct_bccodigo", "banco.bc_descricao", "conta.ct_agencia", 
					"conta.ct_conta", "conta.ct_operacao", "conta.ct_descricao", "cheque.ch_codigo", 
					"cheque.ch_descricao", "cheque.ch_folhas"};
			Cursor cursor = query.query(db, retorno, null, null, null, null, "ch_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Conta conta = new Conta(cursor.getInt(0), new Banco(cursor.getInt(1), cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
					Cheque c = new Cheque();
					c.setCodigo(cursor.getInt(7));
					c.setConta(conta);
					c.setDescricao(cursor.getString(8));
					c.setFolhasRest(cursor.getInt(9));
					list.add(c);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		} 
		
	} 
	
	public boolean verificaCheque(Cheque cheque) throws Exception{
		try {
		Cursor cursor = db.query("cheque", new String[]{"ch_codigo"} , "ch_codigo = " + cheque.getCodigo(), null, null, null, "ch_codigo");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(ch_codigo)"};
			Cursor cursor = db.query("cheque", colunas, null, null, null, null, null);
			
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