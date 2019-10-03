package com.packageview.selfcontrol;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.selfcontrol.R;
import com.packagemodel.selfcontrol.TituloReceber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class Calendario extends Activity{
	CalendarView calendar;
	private String dia, mes, ano;
	private Button btnSalvar, btnCancelar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendario);
		calendar = (CalendarView) findViewById(R.id.calendarView1);
		calendar.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				
				dia = Integer.toString(dayOfMonth);
				mes = Integer.toString(month+1);
				ano = Integer.toString(year);
			}
		});
		
		btnSalvar = (Button) findViewById(R.id.Button01);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Date dataHoje = new Date();
					SimpleDateFormat fd = new SimpleDateFormat("dd");  
					if (dia == null){
						dia = fd.format(dataHoje);
					}
					SimpleDateFormat fm = new SimpleDateFormat("MM");  
					if (mes == null){
						mes = fm.format(dataHoje);
					}
					SimpleDateFormat fy = new SimpleDateFormat("yyyy");
					if (ano == null){
						ano = fy.format(dataHoje);
					}
					if (dia.length() < 2)
						dia = "0" + dia;
					if (mes.length() < 2)
						mes = "0" + mes;
					Bundle b = new Bundle();
					b.putString("data", dia +"/"+ mes +"/"+ ano);
					Intent intent = new Intent();
					intent.putExtras(b);
					setResult(RESULT_OK,intent);      
					finish(); 
				}
					
		});
		
		btnCancelar = (Button) findViewById(R.id.button3);		
		btnCancelar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
		});
	}
}
