package com.packageDAO.selfcontrol;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.MovimentoCheque;
import com.packagemodel.selfcontrol.MovimentoConta;
import com.packagemodel.selfcontrol.TipoMovimentoConta;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;


public class MovContaDAO {
	private SQLiteDatabase db;
	
	public MovContaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(MovimentoConta movConta) throws Exception{
		try {
			ContentValues valores = new ContentValues();
			valores.put("mvct_codigo", movConta.getCodigo());
			valores.put("mvct_ctcodigo", movConta.getConta().getCodigo());
			valores.put("mvct_tmctcodigo",movConta.getTipoMovimentoConta().getCodigo());
			valores.put("mvct_data", movConta.getData());
			valores.put("mvct_documento", movConta.getDocumento());
			
			if (String.valueOf(movConta.getTipoMovimentoConta().getTipo()).equals("DE")){
				valores.put("mvct_valor", 0 - movConta.getValor());
			}
			if (String.valueOf(movConta.getTipoMovimentoConta().getTipo()).equals("RE")){
				valores.put("mvct_valor", movConta.getValor());
			}
			
			
			return db.insert("movconta", null, valores) > 0;

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(MovimentoConta movConta){
		ContentValues valores = new ContentValues();
		valores.put("mvct_codigo", movConta.getCodigo());
		valores.put("mvct_ctcodigo", movConta.getConta().getCodigo());
		valores.put("mvct_tmctcodigo",movConta.getTipoMovimentoConta().getCodigo());
		valores.put("mvct_data", movConta.getData());
		valores.put("mvct_documento", movConta.getDocumento());
		if (String.valueOf(movConta.getTipoMovimentoConta().getTipo()).equals("DE")){
			valores.put("mvct_valor", 0 - movConta.getValor());
		}
		if (String.valueOf(movConta.getTipoMovimentoConta().getTipo()).equals("RE")){
			valores.put("mvct_valor", movConta.getValor());
		}
		
		db.update("movconta", valores, "mvct_codigo = " + movConta.getCodigo(), null);
	}
	
	public boolean delete(MovimentoConta movConta){
		db.delete("movconta", "mvct_codigo = " + movConta.getCodigo(), null);
		db.delete("movcontati", "mvctti_mvctcodigo = " + movConta.getCodigo(), null);
		db.delete("movcontaca", "mvctca_mvctcodigo = " + movConta.getCodigo(), null);
		return true;
	}
	
	public List<MovimentoConta> buscar(Conta conta) throws Exception{
		try {
			List<MovimentoConta> list = new ArrayList<MovimentoConta>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("movconta, conta, tpmovconta, banco");
			query.appendWhere("movconta.mvct_ctcodigo = conta.ct_codigo and movconta.mvct_tmctcodigo = tpmovconta.tmct_codigo and conta.ct_bccodigo = banco.bc_codigo and mvct_ctcodigo  = " + conta.getCodigo());
			String[] colunas = new String[]{"tmct_codigo", "tmct_descricao", "tmct_tipo", "bc_codigo", "bc_descricao", "ct_codigo", "ct_agencia", "ct_conta", "ct_operacao", "ct_descricao"," mvct_codigo", "mvct_documento", "mvct_data", "mvct_valor"};
			Cursor cursor = query.query(db, colunas, null, null, null, null, "mvct_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					TipoMovimentoConta tmct = new TipoMovimentoConta(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
					Banco bc = new Banco(cursor.getInt(3), cursor.getString(4));
					Conta ct = new Conta(cursor.getInt(5), bc, cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
					MovimentoConta mc = new MovimentoConta();
					mc.setCodigo(cursor.getInt(10));
					mc.setConta(ct);
					mc.setTipoMovimentoConta(tmct);
					mc.setDocumento(cursor.getString(11));
					mc.setData(cursor.getString(12));
					mc.setValor(cursor.getFloat(13));
					list.add(mc);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean verificaMovConta(MovimentoConta movConta) throws Exception{
		try {
		Cursor cursor = db.query("movconta", new String[]{"mvct_documento"} , "mvct_documento = '" + movConta.getDocumento() + "'", null, null, null, "mvct_documento");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
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
	
	public float calculaSaldo(Conta conta) throws Exception{
		try {
			float saldo = 0;
			String[] colunas = new String[]{"sum(mvct_valor)"};
			Cursor cursor = db.query("movconta", colunas, "mvct_ctcodigo = " + conta.getCodigo(), null, null, null, null);
			
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
	
	public void baixaTituloPagar(TituloPagar tituloPagar, MovimentoConta movConta) throws Exception{
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
			valoresti.put("mvctti_mvctcodigo", movConta.getConta().getCodigo());
			valoresti.put("mvctti_ticodigo", tituloPagar.getCodigo());
			db.insert("movcontati", null, valoresti);
		
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void baixaTituloReceber(TituloReceber tituloReceber, MovimentoConta movConta) throws Exception{
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
			valoresti.put("mvctti_mvctcodigo", movConta.getConta().getCodigo());
			valoresti.put("mvctti_ticodigo", tituloReceber.getCodigo());
			db.insert("movcontati", null, valoresti);
		
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void compensaCheque(MovimentoCheque movimentoCheque, MovimentoConta movConta) throws Exception{
		try{
			ContentValues valores = new ContentValues();
			valores.put("mc_codigo", movimentoCheque.getCodigo());
			valores.put("mc_chcodigo", movimentoCheque.getCheque().getCodigo());
			valores.put("mc_folha", movimentoCheque.getFolha());
			valores.put("mc_valor", movimentoCheque.getValor());
			valores.put("mc_status", "F");
			db.update("movcheque", valores, "mc_codigo = " + movimentoCheque.getCodigo(), null);
			
			ContentValues valoresch = new ContentValues();
			valoresch.put("mvctch_mvctcodigo", movConta.getConta().getCodigo());
			valoresch.put("mvctti_mvchcodigo", movimentoCheque.getCodigo());
			db.insert("movcontach", null, valoresch);
		
		} catch (Exception e) {
			throw e;
		}
	}
	
}
