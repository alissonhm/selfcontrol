package com.packageview.selfcontrol;

import com.example.selfcontrol.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Movimentacoes extends Activity {
	
	private Button movConta;
	private Button movCompraVenda;
	private Button movCaixa;
	private Button movTitulo;
	private Button fechaFatura;
	private Button movCheque;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movimentacoes);
		
		//ACESSA A TELA DE MOVIMENTACOES DE CONTA     
		movConta = (Button) findViewById(R.id.button1);		
		movConta.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovConta.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE MOVIMENTACOES COMPRA E VENDA     
		movCompraVenda = (Button) findViewById(R.id.button5);		
		movCompraVenda.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovCompraVenda.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE MOVIMENTACOES DE CAIXA     
		movCaixa = (Button) findViewById(R.id.button2);		
		movCaixa.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovCaixa.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE MOVIMENTACOES DE TITULOS     
		movTitulo = (Button) findViewById(R.id.button3);		
		movTitulo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovTitulos.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE FECHAMENTO DE FATURA
		fechaFatura = (Button) findViewById(R.id.Button01);
		fechaFatura.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), FecharFatura.class);
				startActivity(i);
			}
		});
		
		//ACESSA A TELA DE MOVIMENTAÇÃO DE CHEQUES
		movCheque = (Button) findViewById(R.id.Button02);
		movCheque.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MovCheque.class);
				startActivity(i);
			}
		});
	}
}
