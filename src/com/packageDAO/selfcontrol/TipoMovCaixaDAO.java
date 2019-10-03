package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.TipoMovimentoCaixa;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TipoMovCaixaDAO {
private SQLiteDatabase db;
	
	public TipoMovCaixaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public List<TipoMovimentoCaixa> buscar() throws Exception{
		try {
			List<TipoMovimentoCaixa> list = new ArrayList<TipoMovimentoCaixa>();
			String[] colunas = new String[]{"tmcx_codigo", "tmcx_descricao", "tmcx_tipo"};
			Cursor cursor = db.query("tpmovcaixa", colunas, null, null, null, null, "tmcx_codigo");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					TipoMovimentoCaixa p = new TipoMovimentoCaixa();
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
