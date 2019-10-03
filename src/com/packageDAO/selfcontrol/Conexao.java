package com.packageDAO.selfcontrol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper{
	private static final String NOME_BD = "selfcontrol";
	private static final int VERSAO_BD = 33;
	
	public Conexao(Context ctx){
		super(ctx, NOME_BD, null, VERSAO_BD);
	}

	//EFETUA A CRIAÇÃO DA ESTRUTURA DO BANCO DE DADOS
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table banco (bc_codigo integer primary key, bc_descricao text not null);");
		db.execSQL("create table conta (ct_codigo integer primary key, ct_bccodigo integer not null references banco "
				+ "(bc_codigo), ct_agencia text not null, ct_conta text not null, ct_operacao text not null, "
				+ "ct_descricao text not null, UNIQUE(CT_BCCODIGO, CT_AGENCIA, CT_OPERACAO, CT_CONTA));");
		db.execSQL("create table caixa (cx_codigo integer primary key, cx_descricao text not null);");
		db.execSQL("create table cheque (ch_codigo integer primary key, ch_ctcodigo integer not null references conta "
				+ "(ct_codigo), ch_descricao text not null, ch_folhas integer not null);");
		db.execSQL("create table cartao (ca_codigo integer primary key, ca_cartao text not null unique, "
				+ "ca_ctcodigo integer not null references conta (ct_codigo), ca_descricao text not null, "
				+ "ca_bandeira text not null, ca_limite real, ca_fechamento integer, ca_vencimento integer, "
				+ "ca_tipo text not null);");
		db.execSQL("create table titulo (ti_codigo integer primary key, ti_forcli text not null, "
				+ "ti_documento text not null, ti_vencimento text not null, ti_valor real not null, ti_desconto real, "
				+ "ti_acrescimo real, ti_status text not null, ti_saldo real not null, ti_tipo text not null);");
		db.execSQL("create table pagamento (pg_codigo integer not null primary key, pg_descricao text not null, "
				+ "pg_tipo text not null);");
		db.execSQL("insert into pagamento values (1, 'A Prazo', 'AP');");
		db.execSQL("create table pagcaixa (pcx_pgcodigo integer not null references pagamento (pg_codigo), "
				+ "pcx_cxcodigo integer not null references caixa (cx_codigo), PRIMARY KEY(pcx_pgcodigo, pcx_cxcodigo));");
		db.execSQL("create table pagcheque (pch_pgcodigo integer not null references pagamento (pg_codigo), "
				+ "pch_chcodigo integer not null references cheque (ch_codigo), PRIMARY KEY(pch_pgcodigo, pch_chcodigo));");
		db.execSQL("create table pagcartao (pca_pgcodigo integer not null references pagamento (pg_codigo), "
				+ "pca_cacodigo integer not null references cartao (ca_codigo), primary key(pca_pgcodigo, pca_cacodigo));");
		db.execSQL("create table compravenda (cv_codigo integer not null primary key, cv_clifor text not null, "
				+ "cv_produto text not null, cv_data text not null, cv_valor real not null, "
				+ "cv_pgcodigo integer not null references pagamento (pg_codigo), cv_qtdparc integer, "
				+ "cv_dtparc integer, cv_tipo text not null);");
		db.execSQL("create table tpmovcaixa (tmcx_codigo integer not null primary key, tmcx_descricao text not null, "
				+ "tmcx_tipo text not null);");
		db.execSQL("insert into tpmovcaixa values (1, 'Despesa', 'DE');");
		db.execSQL("insert into tpmovcaixa values (2, 'Receita', 'RE');");
		db.execSQL("insert into tpmovcaixa values (3, 'Pagamento Título', 'PT');");
		db.execSQL("insert into tpmovcaixa values (4, 'Recebimento Título', 'RT');");
		db.execSQL("create table tpmovconta (tmct_codigo integer not null primary key, tmct_descricao text not null, "
				+ "tmct_tipo text not null);");
		db.execSQL("insert into tpmovconta values (1, 'Despesa', 'DE');");
		db.execSQL("insert into tpmovconta values (2, 'Receita', 'RE');");
		db.execSQL("insert into tpmovconta values (3, 'Pagamento Título', 'PT');");
		db.execSQL("insert into tpmovconta values (4, 'Recebimento Título', 'RT');");
		db.execSQL("insert into tpmovconta values (5, 'Compensação de Cheque', 'CC');");
		db.execSQL("create table movcheque (mc_codigo integer not null primary key, "
				+ "mc_chcodigo integer not null references cheque (ch_codigo), mc_folha integer not null, "
				+ "mc_valor real not null, mc_status text not null);");
		db.execSQL("create table movcaixa (mvcx_codigo integer not null primary key, "
				+ "mvcx_cxcodigo integer not null references caixa (cx_codigo), "
				+ "mvcx_tmcxcodigo integer not null references tpmovcaixa (tmcx_codigo), mvcx_data text not null, "
				+ "mvcx_documento text, mvcx_valor real not null);");
		db.execSQL("create table movcaixati (mvcxti_mvcxcodigo integer not null references movcaixa (mvcx_codigo), "
				+ "mvcxti_ticodigo integer not null references titulo (ti_codigo), primary key (mvcxti_mvcxcodigo, "
				+ "mvcxti_ticodigo));");
		db.execSQL("create table movcaixacv (mvcxcv_mvcxcodigo integer not null references movcaixa (mvcx_codigo), "
				+ "mvcxcv_cvcodigo integer not null references compravenda (cv_codigo), "
				+ "primary key (mvcxcv_mvcxcodigo, mvcxcv_cvcodigo))");
		db.execSQL("create table movconta (mvct_codigo integer not null primary key, "
				+ "mvct_ctcodigo integer not null references conta (ct_codigo), "
				+ "mvct_tmctcodigo integer not null references tpmovconta (tmct_codigo), mvct_data text not null, "
				+ "mvct_documento text, mvct_valor real not null);");
		db.execSQL("create table movcontati (mvctti_mvctcodigo integer not null references movconta (mvct_codigo), "
				+ "mvctti_ticodigo integer not null references titulo (ti_codigo), "
				+ "primary key (mvctti_mvctcodigo, mvctti_ticodigo));");
		db.execSQL("create table movcontach (mvctch_mvctcodigo integer not null references movconta (mvct_codigo), "
				+ "mvctch_mccodigo integer not null references movcheque (mc_codigo), "
				+ "primary key (mvctch_mvctcodigo, mvctch_mccodigo))");
		db.execSQL("create table movcontaca (mvctca_mvctcodigo integer not null references movconta (mvct_codigo), "
				+ "mvctca_cacodigo integer not null references cartao (ca_codigo), "
				+ "primary key (mvctca_mvctcodigo, mvctca_cacodigo))");
	}

	//É EXECUTADO CASO O BANCO JÁ EXISTA
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table compravenda;");
		db.execSQL("drop table pagcartao;");
		db.execSQL("drop table pagcheque;");
		db.execSQL("drop table pagcaixa;");
		db.execSQL("drop table pagamento;");
		db.execSQL("drop table titulo;");
		db.execSQL("drop table cartao;");
		db.execSQL("drop table cheque;");
		db.execSQL("drop table conta;");
		db.execSQL("drop table banco;");
		db.execSQL("drop table caixa;");
		db.execSQL("drop table tpmovcaixa;");
		db.execSQL("drop table tpmovconta;");
		db.execSQL("drop table movcheque;");
		db.execSQL("drop table movcaixa;");
		db.execSQL("drop table movcaixati;");
		db.execSQL("drop table movcaixacv;");
		db.execSQL("drop table movconta;");
		db.execSQL("drop table movcontati;");
		db.execSQL("drop table movcontach;");
		db.execSQL("drop table movcontaca;");
		onCreate(db);
	}

}
