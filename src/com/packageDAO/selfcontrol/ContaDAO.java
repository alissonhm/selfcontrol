package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;

public class ContaDAO {
	private SQLiteDatabase db;
	
	public ContaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(Conta conta) throws Exception{
		try {
			ContentValues valores = new ContentValues();
			valores.put("ct_codigo", conta.getCodigo());
			valores.put("ct_bccodigo", conta.getBanco().getCodigo());
			valores.put("ct_agencia", conta.getAgencia());
			valores.put("ct_conta", conta.getConta());
			valores.put("ct_operacao", conta.getOperacao());
			valores.put("ct_descricao", conta.getDescricao());
			
			return db.insert("conta", null, valores) > 0;

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(Conta conta){
		ContentValues valores = new ContentValues();
		valores.put("ct_codigo", conta.getCodigo());
		valores.put("ct_bccodigo", conta.getBanco().getCodigo());
		valores.put("ct_agencia", conta.getAgencia());
		valores.put("ct_conta", conta.getConta());
		valores.put("ct_operacao", conta.getOperacao());
		valores.put("ct_descricao", conta.getDescricao());
		
		db.update("conta", valores, "ct_codigo = " + conta.getCodigo(), null);
	}
	
	public boolean delete(Conta conta){
		Cursor cursor = db.query("cheque", null, "ch_ctcodigo = " + conta.getCodigo(), null, null, null, null);
		Cursor cursor1 = db.query("cartao", null, "ca_ctcodigo = " + conta.getCodigo(), null, null, null, null);
		Cursor cursor2 = db.query("movconta", null, "mvct_ctcodigo = " + conta.getCodigo(), null, null, null, null);
		if (cursor.getCount() < 1){
			if (cursor1.getCount() < 1){
				if(cursor2.getCount() < 1){
					db.delete("conta", "ct_codigo = " + conta.getCodigo(), null);
					return true;
				}else{
					return false;
				}
			}
		}else{
			return false;
			}
		return false;
		}
	

	public List<Conta> buscar() throws Exception{
		try {
			List<Conta> list = new ArrayList<Conta>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("banco,conta");
			query.appendWhere("banco.bc_codigo = conta.ct_bccodigo");
			String retorno[] = {"banco.bc_codigo", "banco.bc_descricao", "conta.ct_codigo", "conta.ct_agencia", 
					"conta.ct_conta", "conta.ct_operacao", "conta.ct_descricao"};
			Cursor cursor = query.query(db, retorno, null, null, null, null, "ct_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Banco banco = new Banco(cursor.getInt(0), cursor.getString(1));
					Conta c = new Conta();
					c.setCodigo(cursor.getInt(2));
					c.setBanco(banco);
					c.setAgencia(cursor.getString(3));
					c.setConta(cursor.getString(4));
					c.setOperacao(cursor.getString(5));
					c.setDescricao(cursor.getString(6));
					list.add(c);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		} 
		
	} 
	
	public boolean verificaConta(Conta conta) throws Exception{
		try {
		Cursor cursor = db.query("conta", new String[]{"ct_bccodigo","ct_agencia","ct_conta","ct_operacao"} , "ct_conta = '" + conta.getConta() + "' and ct_agencia = '" + conta.getAgencia() + "' and ct_operacao = '" + conta.getOperacao() + "' and ct_bccodigo = " + conta.getBanco().getCodigo() + " and ct_codigo <> " + conta.getCodigo(), null, null, null, "ct_bccodigo, ct_agencia, ct_operacao, ct_conta");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(ct_codigo)"};
			Cursor cursor = db.query("conta", colunas, null, null, null, null, null);
			
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