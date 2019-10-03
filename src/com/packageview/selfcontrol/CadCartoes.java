package com.packageview.selfcontrol;

import com.example.selfcontrol.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CadCartoes extends Activity{
	
	private Button cadCartCre;
	private Button cadCartDeb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcartoes);
		
		//ACESSA A TELA DE CADASTRO DE CARTOES DE CREDITO      
		cadCartCre = (Button) findViewById(R.id.button1);		
		cadCartCre.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadCartCre.class);
					startActivity(i);
				}
		});
		
		//ACESSA A TELA DE CADASTRO DE CARTOES DE DEBITO       
		cadCartDeb = (Button) findViewById(R.id.button2);		
		cadCartDeb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadCartDeb.class);
					startActivity(i);
				}
			});
	}

}
