package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Cheque;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.MovimentoCheque;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class MovChequeDAO {
	private SQLiteDatabase db;
	
	public MovChequeDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(MovimentoCheque movCheque) throws Exception{
		try {
			if (movCheque.getCheque().getFolhasRest() < 1){
				return false;
			}
			else{
				ContentValues valores = new ContentValues();
				valores.put("mc_codigo", movCheque.getCodigo());
				valores.put("mc_chcodigo", movCheque.getCheque().getCodigo());
				valores.put("mc_folha", movCheque.getFolha());
				valores.put("mc_valor", movCheque.getValor());
				valores.put("mc_status", "A");
				
				db.execSQL("update cheque set ch_folhas = ch_folhas - 1 where ch_codigo = " + movCheque.getCheque().getCodigo() + ";");
				return db.insert("movcheque", null, valores) > 0;
			}

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(MovimentoCheque movCheque){
		ContentValues valores = new ContentValues();
		valores.put("mc_codigo", movCheque.getCodigo());
		valores.put("mc_chcodigo", movCheque.getCheque().getCodigo());
		valores.put("mc_folha", movCheque.getFolha());
		valores.put("mc_valor", movCheque.getValor());
		valores.put("mc_status", "A");
		
		db.update("movcheque", valores, "mc_codigo = " + movCheque.getCodigo(), null);
	}
	
	public boolean delete(MovimentoCheque movCheque){
		db.delete("movcheque", "mc_codigo = " + movCheque.getCodigo(), null);
		db.execSQL("update cheque set ch_folhas = ch_folhas + 1 where ch_codigo = " + movCheque.getCheque().getCodigo() + ";");
			return true;
	}
	
	public List<MovimentoCheque> buscar() throws Exception{
		try {
			List<MovimentoCheque> list = new ArrayList<MovimentoCheque>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("movcheque, cheque, conta, banco");
			query.appendWhere("banco.bc_codigo = conta.ct_bccodigo and conta.ct_codigo = cheque.ch_ctcodigo and cheque.ch_codigo = movcheque.mc_chcodigo and mc_status = 'A'");
			String[] colunas = new String[]{"banco.bc_codigo, banco.bc_descricao," +
											"conta.ct_codigo, conta.ct_agencia, conta.ct_conta, conta.ct_operacao, conta.ct_descricao," +
											"cheque.ch_codigo, cheque.ch_descricao, cheque.ch_folhas," + 
											"movcheque.mc_codigo, movcheque.mc_folha, movcheque.mc_valor real, movcheque.mc_status"};
			Cursor cursor = query.query(db, colunas, null, null, null, null, "mc_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Banco b = new Banco(cursor.getInt(0), cursor.getString(1));
					Conta c = new Conta(cursor.getInt(2), b, cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
					Cheque ch = new Cheque(cursor.getInt(7), c, cursor.getString(8), cursor.getInt(9));
					MovimentoCheque mc = new MovimentoCheque();
					mc.setCodigo(cursor.getInt(10));
					mc.setCheque(ch);
					mc.setFolha(cursor.getInt(11));
					mc.setValor(cursor.getFloat(12));
					mc.setStatus(cursor.getString(13));
					list.add(mc);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean verificaMovCheque(MovimentoCheque movCheque) throws Exception{
		try {
		Cursor cursor = db.query("movcheque", new String[]{"mc_codigo"} , "mc_codigo = '" + movCheque.getCodigo() + "'", null, null, null, "mc_codigo");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
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
	
}
