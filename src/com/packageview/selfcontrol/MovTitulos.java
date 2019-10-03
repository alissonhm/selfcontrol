package com.packageview.selfcontrol;

import com.example.selfcontrol.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MovTitulos extends Activity{
	
	private Button titulosPagar;
	private Button titulosReceber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movtitulos);
		
		//ACESSA A TELA DE MOVIMENTACOES DE TÍTULOS A PAGAR    
		titulosPagar = (Button) findViewById(R.id.button1);		
		titulosPagar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovTitPag.class);
					startActivity(i);
				}
			});
		
		//ACESSA A TELA DE MOVIMENTACOES DE TÍTULOS A RECEBER   
		titulosReceber = (Button) findViewById(R.id.button2);		
		titulosReceber.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovTitReceb.class);
					startActivity(i);
				}
			});		
	}

}
