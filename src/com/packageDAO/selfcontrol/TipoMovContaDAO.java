package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.TipoMovimentoCaixa;
import com.packagemodel.selfcontrol.TipoMovimentoConta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TipoMovContaDAO {
private SQLiteDatabase db;
	
	public TipoMovContaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public List<TipoMovimentoConta> buscar() throws Exception{
		try {
			List<TipoMovimentoConta> list = new ArrayList<TipoMovimentoConta>();
			String[] colunas = new String[]{"tmct_codigo", "tmct_descricao", "tmct_tipo"};
			Cursor cursor = db.query("tpmovconta", colunas, null, null, null, null, "tmct_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					TipoMovimentoConta p = new TipoMovimentoConta();
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
