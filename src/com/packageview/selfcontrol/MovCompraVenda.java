package com.packageview.selfcontrol;

import com.example.selfcontrol.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MovCompraVenda extends Activity{
	
	private Button movCompra;
	private Button movVenda;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcompravenda);
		
		//ACESSA A TELA DE MOVIMENTACOES DE COMPRAS     
		movCompra = (Button) findViewById(R.id.button1);		
		movCompra.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovCompra.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE MOVIMENTACOES DE VENDAS  
		movVenda = (Button) findViewById(R.id.button2);		
		movVenda.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovVenda.class);
					startActivity(i);
				}
			});		
	}

}
