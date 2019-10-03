package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.CartCre;
import com.packagemodel.selfcontrol.Conta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class CartCreDAO {
private SQLiteDatabase db;
	
	public CartCreDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public void  inserir(CartCre cartCre) throws Exception{
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
			
			ContentValues valorescc = new ContentValues();
			ContentValues valorespg = new ContentValues();
			ContentValues valorespca = new ContentValues();
			
			valorescc.put("ca_codigo", cartCre.getCodigo());
			valorescc.put("ca_cartao", cartCre.getNroCartao());
			valorescc.put("ca_ctcodigo", cartCre.getConta().getCodigo());
			valorescc.put("ca_descricao", cartCre.getDescricao());
			valorescc.put("ca_bandeira", cartCre.getBandeira());
			valorescc.put("ca_limite", cartCre.getLimite());
			valorescc.put("ca_fechamento", cartCre.getDtFechamento());
			valorescc.put("ca_vencimento", cartCre.getDtVencimento());
			valorescc.put("ca_tipo", "C");
			
			valorespg.put("pg_codigo", codigo);
			valorespg.put("pg_descricao", cartCre.getDescricao());
			valorespg.put("pg_tipo", "CC");
			
			valorespca.put("pca_pgcodigo", codigo);
			valorespca.put("pca_cacodigo", cartCre.getCodigo());
			
			db.insert("cartao", null, valorescc);
			db.insert("pagamento", null, valorespg);
			db.insert("pagcartao", null, valorespca);

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(CartCre cartCre){
		int codigo = 0;
		Cursor cursor = db.query("pagcartao", new String[]{"pca_pgcodigo"} , "pca_cacodigo = " + cartCre.getCodigo(), null, null, null, "pca_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
			
		ContentValues valorescc = new ContentValues();
		ContentValues valorespg = new ContentValues();
		
		valorescc.put("ca_codigo", cartCre.getCodigo());
		valorescc.put("ca_cartao", cartCre.getNroCartao());
		valorescc.put("ca_ctcodigo", cartCre.getConta().getCodigo());
		valorescc.put("ca_descricao", cartCre.getDescricao());
		valorescc.put("ca_bandeira", cartCre.getBandeira());
		valorescc.put("ca_limite", cartCre.getLimite());
		valorescc.put("ca_fechamento", cartCre.getDtFechamento());
		valorescc.put("ca_vencimento", cartCre.getDtVencimento());
		valorescc.put("ca_tipo", "C");
		
		valorespg.put("pg_codigo", codigo);
		valorespg.put("pg_descricao", cartCre.getDescricao());
		valorespg.put("pg_tipo", "CC");
		
		db.update("cartao", valorescc, "ca_codigo = " + cartCre.getCodigo(), null);
		db.update("pagamento", valorespg, "pg_codigo = " + codigo, null);
		}
	}
	
	public boolean delete(CartCre cartCre){
		int codigo = 0;
		Cursor cursor = db.query("pagcartao", new String[]{"pca_pgcodigo"} , "pca_cacodigo = " + cartCre.getCodigo(), null, null, null, "pca_pgcodigo");

		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				codigo = cursor.getInt(0);
			}
			
		Cursor cursor1 = db.query("movcontaca", null, "mvctca_cacodigo = " + cartCre.getCodigo(), null, null, null, null);
		Cursor cursor2 = db.query("compravenda", null, "cv_pgcodigo = " + codigo, null, null, null, null);
		if (cursor2.getCount() < 1){
			if (cursor1.getCount() < 1){
				db.delete("pagcartao", "pca_cacodigo = " + cartCre.getCodigo() + " and pca_pgcodigo = " + codigo, null);
				db.delete("pagamento", "pg_codigo = " + codigo, null);
				db.delete("cartao", "ca_codigo = " + cartCre.getCodigo(), null);
				return true;
				}
			return false;
			}
		}
		return false;
	}
	

	public List<CartCre> buscar() throws Exception{
		try {
			List<CartCre> list = new ArrayList<CartCre>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("conta,cartao,banco");
			query.appendWhere("conta.ct_codigo = cartao.ca_ctcodigo and conta.ct_bccodigo = banco.bc_codigo and cartao.ca_tipo = 'C'");
			String retorno[] = {"conta.ct_codigo","conta.ct_bccodigo", "banco.bc_descricao", "conta.ct_agencia", 
					"conta.ct_conta", "conta.ct_operacao", "conta.ct_descricao", "cartao.ca_codigo", "cartao.ca_cartao", 
					"cartao.ca_descricao", "cartao.ca_bandeira", "cartao.ca_limite", "cartao.ca_fechamento", "cartao.ca_vencimento" };
			Cursor cursor = query.query(db, retorno, null, null, null, null, "ct_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Conta conta = new Conta(cursor.getInt(0), new Banco(cursor.getInt(1), cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
					CartCre c = new CartCre();
					c.setCodigo(cursor.getInt(7));
					c.setNroCartao(cursor.getString(8));
					c.setConta(conta);					
					c.setDescricao(cursor.getString(9));
					c.setBandeira(cursor.getString(10));
					c.setLimite(cursor.getFloat(11));
					c.setDtFechamento(cursor.getInt(12));
					c.setDtVencimento(cursor.getInt(13));

					list.add(c);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		} 
		
	} 
	
	public boolean verificaCartao(CartCre cartCre) throws Exception{
		try {
		Cursor cursor = db.query("cartao", new String[]{"ca_cartao"} , "ca_cartao = '" + cartCre.getNroCartao() + "' and ca_codigo <> " + cartCre.getCodigo(), null, null, null, "ca_cartao");
			
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