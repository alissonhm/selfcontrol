package com.packageDAO.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.Venda;

public class VendaDAO {private SQLiteDatabase db;

	public VendaDAO(Context context){
		Conexao auxBd = new Conexao(context);
		db = auxBd.getWritableDatabase();
	}
	
	public void inserir(Venda venda) throws Exception{
		try {
			ContentValues valorescv = new ContentValues();
			
			valorescv.put("cv_codigo", venda.getCodigo());
			valorescv.put("cv_clifor", venda.getCliente());
			valorescv.put("cv_produto", venda.getProduto());
			valorescv.put("cv_data", venda.getData());
			valorescv.put("cv_valor", venda.getValor());
			valorescv.put("cv_pgcodigo", venda.getPagamento().getCodigo());
			valorescv.put("cv_qtdparc", venda.getQtdParcela());
			valorescv.put("cv_dtparc", venda.getDtParcela());
			valorescv.put("cv_tipo", "V");
			
			
			db.insert("compravenda", null, valorescv);
			
			if (String.valueOf(venda.getPagamento().getTipo()).equals("AP")){
				int cont = 1, mesInt, anoInt, diaInt, diaComp;
				float valorParc = venda.getValor() / venda.getQtdParcela();
				diaComp = Integer.parseInt(venda.getData().substring(0, 2));
				mesInt = Integer.parseInt(venda.getData().substring(3, 5));
				anoInt = Integer.parseInt(venda.getData().substring(6, 10));				 
				while (cont <= venda.getQtdParcela()){
					diaInt = venda.getDtParcela();
					
					if (diaInt < diaComp || cont > 1)
						mesInt++;
					if (mesInt == 2 && diaInt > 28)
						diaInt = 28;
					if ((mesInt == 4 || mesInt == 6 || mesInt == 9 || mesInt == 11) && diaInt > 30)
						diaInt = 30;
					
					if (mesInt > 12){
						mesInt = 1;
						anoInt++;
					}
					
					ContentValues valoresti = new ContentValues();
					valoresti.put("ti_codigo", buscaCodigoTitulo());
					valoresti.put("ti_forcli", venda.getCliente());
					valoresti.put("ti_documento", "Venda: " + venda.getCodigo() + ", parcela " + cont + "/" + venda.getQtdParcela());
					valoresti.put("ti_vencimento", diaInt + "/" + mesInt + "/" + anoInt);
					valoresti.put("ti_valor", valorParc);
					valoresti.put("ti_desconto", 0);
					valoresti.put("ti_acrescimo", 0);
					valoresti.put("ti_status", "A");
					valoresti.put("ti_saldo", valorParc);
					valoresti.put("ti_tipo", "R");
				
					db.insert("titulo", null, valoresti);
					cont++;					
				}
			}
			
			if (String.valueOf(venda.getPagamento().getTipo()).equals("CX")){
				ContentValues valoresve = new ContentValues();
				valoresve.put("mvcx_codigo", buscaCodigoMovCaixa());
				valoresve.put("mvcx_cxcodigo", buscaPagamentoCaixa(venda.getPagamento()));
				valoresve.put("mvcx_tmcxcodigo", 2);
				valoresve.put("mvcx_data", venda.getData());
				valoresve.put("mvcx_documento", "Compra: " + venda.getCodigo());
				valoresve.put("mvcx_valor", 0 - venda.getValor());
				db.insert("movcaixa", null, valoresve);
				
				ContentValues valores = new ContentValues();
				valores.put("mvcxcv_mvcxcodigo", buscaPagamentoCaixa(venda.getPagamento()));
				valores.put("mvcxcv_cvcodigo", venda.getCodigo());
				db.insert("movcaixacv", null, valores);
				
			}
			

	
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	public void atualizar(Venda venda){
		ContentValues valorescv = new ContentValues();
		
		valorescv.put("cv_codigo", venda.getCodigo());
		valorescv.put("cv_clifor", venda.getCliente());
		valorescv.put("cv_produto", venda.getProduto());
		valorescv.put("cv_data", venda.getData());
		valorescv.put("cv_valor", venda.getValor());
		valorescv.put("cv_pgcodigo", venda.getPagamento().getCodigo());
		valorescv.put("cv_qtdparc", venda.getQtdParcela());
		valorescv.put("cv_dtparc", venda.getDtParcela());
		valorescv.put("cv_tipo", "V");
		
		db.update("compravenda", valorescv, "cv_codigo = " + venda.getCodigo(), null);
	}
	
	public boolean delete(Venda venda){
		db.delete("compravenda", "cv_codigo = " + venda.getCodigo(), null);
		return true;
	}
	
	
	public List<Venda> buscar() throws Exception{
		try {
			List<Venda> list = new ArrayList<Venda>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables("compravenda,pagamento");
			query.appendWhere("compravenda.cv_pgcodigo = pagamento.pg_codigo and compravenda.cv_tipo = 'V'");
			String retorno[] = {"pagamento.pg_codigo", "pagamento.pg_descricao", "pagamento.pg_tipo", 
					"compravenda.cv_codigo", "compravenda.cv_clifor", "compravenda.cv_produto", "compravenda.cv_data", 
					"compravenda.cv_valor", "compravenda.cv_qtdparc", "compravenda.cv_dtparc"};
			Cursor cursor = query.query(db, retorno, null, null, null, null, "cv_codigo desc");
			
			if (cursor.getCount() > 0){
				
				while(cursor.moveToNext()){
					Pagamento pagamento = new Pagamento(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
					Venda v = new Venda();
					v.setCodigo(cursor.getInt(3));
					v.setCliente(cursor.getString(4));
					v.setProduto(cursor.getString(5));
					v.setData(cursor.getString(6));
					v.setValor(cursor.getFloat(7));
					v.setPagamento(pagamento);
					v.setQtdParcela(cursor.getInt(8));
					v.setDtParcela(cursor.getInt(9));
					list.add(v);
				}
			}
			
			return (list);
			
		} catch (Exception e) {
			throw e;
		} 
		
	} 
	
	public boolean verificaVenda(Venda venda) throws Exception{
		try {
		Cursor cursor = db.query("compravenda", new String[]{"cv_codigo"} , "cv_codigo= " + venda.getCodigo(), null, null, null, "cv_codigo");
			
			return cursor.getCount() > 0;
		
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int buscaCodigo() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(cv_codigo)"};
			Cursor cursor = db.query("compravenda", colunas, null, null, null, null, null);
			
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
	
	public int buscaCodigoMovCaixa() throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"max(mvcx_codigo)"};
			Cursor cursor = db.query("movcaixa", colunas, null, null, null, null, null);
			
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
	
	public int buscaPagamentoCaixa(Pagamento pagto) throws Exception{
		try {
			int codigo = 0;
			String[] colunas = new String[]{"pcx_cxcodigo"};
			Cursor cursor = db.query("pagcaixa", colunas, "pcx_pgcodigo = " + pagto.getCodigo(), null, null, null, null);
			
			if (cursor.getCount() > 0){
				while(cursor.moveToNext()){
					codigo = cursor.getInt(0);
				}
			}			
			return codigo;
			
		} catch (Exception e) {
			throw e;
		}
	}
}

