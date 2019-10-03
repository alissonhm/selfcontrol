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

public class Cadastro extends Activity {
	
	private Button btnCadBanco;
	private Button btnCadConta;
	private Button btnCadCaixa;
	private Button btnCadCheque;
	private Button btnCadCartao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadastro);	
		
		//ACESSA A TELA DE CADASTRO DE BANCOS       
		btnCadBanco = (Button) findViewById(R.id.button1);		
		btnCadBanco.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadBanco.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE CADASTRO DE CONTAS       
		btnCadConta = (Button) findViewById(R.id.button2);		
		btnCadConta.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadConta.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE CADASTRO DE CAIXAS       
		btnCadCaixa = (Button) findViewById(R.id.button5);		
		btnCadCaixa.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadCaixa.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE CADASTRO DE CHEQUES       
		btnCadCheque = (Button) findViewById(R.id.button4);		
		btnCadCheque.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadCheque.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE CADASTRO DE CARTOES       
		btnCadCartao = (Button) findViewById(R.id.button3);		
		btnCadCartao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadCartoes.class);
					startActivity(i);
				}
			});
	}
}
