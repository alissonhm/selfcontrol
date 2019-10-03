package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class BancoDAO {
	private SQLiteDatabase db;
	
	public BancoDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(Banco banco) throws Exception{
		try {
			ContentValues valores = new ContentValues();
			valores.put("bc_codigo", banco.getCodigo());
			valores.put("bc_descricao", banco.getDescricao());
			
			return db.insert("banco", null, valores) > 0;

		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(Banco banco){
		ContentValues valores = new ContentValues();
		valores.put("bc_codigo", banco.getCodigo());
		valores.put("bc_descricao", banco.getDescricao());
		
		db.update("banco", valores, "bc_codigo = " + banco.getCodigo(), null);
	}
	
	public boolean delete(Banco banco){
		Cursor cursor = db.query("conta", null, "ct_bccodigo = " + banco.getCodigo(), null, null, null, null);
		if (cursor.getCount() < 1){
			db.delete("banco", "bc_codigo = " + banco.getCodigo(), null);
			return true;
		}else{
			return false;
		}
	}
	
	public List<Banco> buscar() throws Exception{
		try {
			List<Banco> list = new ArrayList<Banco>();
			String[] colunas = new String[]{"bc_codigo", "bc_descricao"};
			Cursor cursor = db.query("banco", colunas, null, null, null, null, "bc_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Banco b = new Banco();
					b.setCodigo(cursor.getInt(0));
					b.setDescricao(cursor.getString(1));
					list.add(b);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public boolean verificaBanco(Banco banco) throws Exception{
		try {
		Cursor cursor = db.query("banco", new String[]{"bc_codigo"} , "bc_codigo = " + banco.getCodigo(), null, null, null, "bc_codigo");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(bc_codigo)"};
			Cursor cursor = db.query("banco", colunas, null, null, null, null, null);
			
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
