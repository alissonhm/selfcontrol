package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Pagamento;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PagamentoDAO {
private SQLiteDatabase db;
	
	public PagamentoDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public List<Pagamento> buscarPagar() throws Exception{
		try {
			List<Pagamento> list = new ArrayList<Pagamento>();
			String[] colunas = new String[]{"pg_codigo", "pg_descricao", "pg_tipo"};
			Cursor cursor = db.query("pagamento", colunas, null, null, null, null, "pg_tipo, pg_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Pagamento p = new Pagamento();
					p.setCodigo(cursor.getInt(0));
					p.setDescricao(cursor.getString(1));
					p.setTipo(cursor.getString(2));
					list.add(p);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public List<Pagamento> buscarReceber() throws Exception{
		try {
			List<Pagamento> list = new ArrayList<Pagamento>();
			String[] colunas = new String[]{"pg_codigo", "pg_descricao", "pg_tipo"};
			Cursor cursor = db.query("pagamento", colunas, "pg_tipo = 'CX' or pg_tipo = 'AP'", null, null, null, "pg_tipo, pg_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Pagamento p = new Pagamento();
					p.setCodigo(cursor.getInt(0));
					p.setDescricao(cursor.getString(1));
					p.setTipo(cursor.getString(2));
					list.add(p);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}

}
