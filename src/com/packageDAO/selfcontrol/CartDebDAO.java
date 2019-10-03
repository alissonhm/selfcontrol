package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.CartDeb;
import com.packagemodel.selfcontrol.Conta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class CartDebDAO {
private SQLiteDatabase db;
	
	public CartDebDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public void  inserir(CartDeb cartDeb) throws Exception{
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
			
			ContentValues valorescd = new ContentValues();
			ContentValues valorespg = new ContentValues();
			ContentValues valorespca = new ContentValues();
			
			valorescd.put("ca_codigo", cartDeb.getCodigo());
			valorescd.put("ca_cartao", cartDeb.getNroCart());
			valorescd.put("ca_ctcodigo", cartDeb.getConta().getCodigo());
			valorescd.put("ca_descricao", cartDeb.getDescricao());
			valorescd.put("ca_bandeira", cartDeb.getBandeira());
			valorescd.put("ca_tipo", "D");
			
			valorespg.put("pg_codigo", codigo);
			valorespg.put("pg_descricao", cartDeb.getDescricao());
			valorespg.put("pg_tipo", "CD");
			
			valorespca.put("pca_pgcodigo", codigo);
			valorespca.put("pca_cacodigo", cartDeb.getCodigo());
			
			db.insert("cartao", null, valorescd);
			db.insert("pagamento", null, valorespg);
			db.insert("pagcartao", null, valorespca);

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(CartDeb cartDeb){
		int codigo = 0;
		Cursor cursor = db.query("pagcartao", new String[]{"pca_pgcodigo"} , "pca_cacodigo = " + cartDeb.getCodigo(), null, null, null, "pca_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
			
		ContentValues valorescd = new ContentValues();
		ContentValues valorespg = new ContentValues();
		
		valorescd.put("ca_codigo", cartDeb.getCodigo());
		valorescd.put("ca_cartao", cartDeb.getNroCart());
		valorescd.put("ca_ctcodigo", cartDeb.getConta().getCodigo());
		valorescd.put("ca_descricao", cartDeb.getDescricao());
		valorescd.put("ca_bandeira", cartDeb.getBandeira());
		valorescd.put("ca_tipo", "D");
		
		valorespg.put("pg_codigo", codigo);
		valorespg.put("pg_descricao", cartDeb.getDescricao());
		valorespg.put("pg_tipo", "CD");
		
		db.update("cartao", valorescd, "ca_codigo = " + cartDeb.getCodigo(), null);
		db.update("pagamento", valorespg, "pg_codigo = " + codigo, null);
		}
	}
	
	public boolean delete(CartDeb cartDeb){
		int codigo = 0;
		Cursor cursor = db.query("pagcartao", new String[]{"pca_pgcodigo"} , "pca_cacodigo = " + cartDeb.getCodigo(), null, null, null, "pca_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
			Cursor cursor1 = db.query("movcontaca", null, "mvctca_cacodigo = " + cartDeb.getCodigo(), null, null, null, null);
			Cursor cursor2 = db.query("compravenda", null, "cv_pgcodigo = " + codigo, null, null, null, null);
			if (cursor2.getCount() < 1){
				if (cursor1.getCount() < 1){
					db.delete("pagcartao", "pca_cacodigo = " + cartDeb.getCodigo() + " and pca_pgcodigo = " + codigo, null);
					db.delete("pagamento", "pg_codigo = " + codigo, null);
					db.delete("cartao", "ca_codigo = " + cartDeb.getCodigo(), null);
					return true;
				}
			}return false;
					
		}
		return false;
	}
	

	public List<CartDeb> buscar() throws Exception{
		try {
			List<CartDeb> list = new ArrayList<CartDeb>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("conta,cartao,banco");
			query.appendWhere("conta.ct_codigo = cartao.ca_ctcodigo and conta.ct_bccodigo = banco.bc_codigo and cartao.ca_tipo = 'D'");
			String retorno[] = {"conta.ct_codigo","conta.ct_bccodigo", "banco.bc_descricao", "conta.ct_agencia", 
					"conta.ct_conta", "conta.ct_operacao", "conta.ct_descricao", "cartao.ca_codigo", "cartao.ca_descricao",
					"cartao.ca_bandeira", "cartao.ca_cartao"};
			Cursor cursor = query.query(db, retorno, null, null, null, null, "ct_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Conta conta = new Conta(cursor.getInt(0), new Banco(cursor.getInt(1), cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
					CartDeb c = new CartDeb();
					c.setCodigo(cursor.getInt(7));
					c.setConta(conta);
					c.setDescricao(cursor.getString(8));
					c.setBandeira(cursor.getString(9));
					c.setNroCart(cursor.getString(10));
					list.add(c);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		} 
		
	} 
	
	public boolean verificaCartao(CartDeb cartDeb) throws Exception{
		try {
		Cursor cursor = db.query("cartao", new String[]{"ca_cartao"} , "ca_cartao = '" + cartDeb.getNroCart() + "' and ca_codigo <> " + cartDeb.getCodigo() , null, null, null, "ca_cartao");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(ca_codigo)"};
			Cursor cursor = db.query("cartao", colunas, null, null, null, null, null);
			
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