package com.packageDAO.selfcontrol;

import com.packagemodel.selfcontrol.CartCre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FecharFaturaDAO {
	private SQLiteDatabase db;
	
	public FecharFaturaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public boolean  inserir(CartCre cartCre, int mes, int ano) throws Exception{
		float valorParc= 0;
		String[] colunas = new String[]{"sum(ti_saldo)"};
		Cursor cursor = db.query("titulo", colunas , "ti_vencimento like '%/" + mes + "/" + ano + 
				"' and ti_status = 'A' and ti_tipo = 'CC'", null, null, null, "ti_codigo");
		if (cursor.getCount() > 0){
			while(cursor.moveToNext()){
				valorParc = cursor.getInt(0);
			}
		}
		
		ContentValues valorestiant = new ContentValues();
		valorestiant.put("ti_status", "F");		
		db.update("titulo", valorestiant, "ti_vencimento like '%/" + mes + "/" + ano + "' and ti_tipo = 'CC'", null);
		
		if (mes == 12){
			mes = 1;
			ano++;
		}
		
		else{
			mes++;
		}
			
		ContentValues valoresti = new ContentValues();
		valoresti.put("ti_codigo", buscaCodigoTitulo());
		valoresti.put("ti_forcli", cartCre.getConta().getBanco().getDescricao() + " / " + cartCre.getDescricao());
		valoresti.put("ti_documento", "Fatura " + mes + "/" + ano);
		valoresti.put("ti_vencimento", String.valueOf(buscaVencimentoCartao(cartCre.getCodigo())) + "/" + mes + "/" + ano);
		valoresti.put("ti_valor", valorParc);
		valoresti.put("ti_desconto", 0);
		valoresti.put("ti_acrescimo", 0);
		valoresti.put("ti_status", "A");
		valoresti.put("ti_saldo", valorParc);
		valoresti.put("ti_tipo", "P");
		
		
		return db.insert("titulo", null, valoresti) > 0;
						
	}
	
	public int buscaCodigoTitulo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(ti_codigo)"};
			Cursor cursor = db.query("titulo", colunas, null, null, null, null, null);
			
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
	
	public int buscaVencimentoCartao(int codigo){
		int diaVencimento = 0;
		String[] colunasCartao = new String[]{"ca_vencimento"};
		Cursor cursorCartao = db.query("cartao", colunasCartao, "ca_codigo = " + codigo, null, null, null, "ca_vencimento");
		if (cursorCartao.getCount() > 0){
			while(cursorCartao.moveToNext()){
				diaVencimento = cursorCartao.getInt(0);
			}
		}
		return diaVencimento;
}
	
}
