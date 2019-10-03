package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterCartCre;
import com.packagecontroller.selfcontrol.CtrlCartCre;
import com.packagecontroller.selfcontrol.CtrlFecharFatura;
import com.packagemodel.selfcontrol.CartCre;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FecharFatura extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Spinner combo;
	private MinhaClasseAdapterCartCre mca;
	private ArrayList<CartCre> listaCartCre;
	private CtrlCartCre ctrlCartCre;
	private TextView ano, mes;
	private CartCre cartCre;
	private CtrlFecharFatura ctrlFecharFatura;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fecharfatura);
		
		
		cartCre = new CartCre();
		
		ctrlFecharFatura = new CtrlFecharFatura(this.getApplicationContext());
		ctrlCartCre = new CtrlCartCre(this.getApplicationContext());
		
		//RECEBE O SPINNER
		listaCartCre = new ArrayList<CartCre>();
		loadCartCre();
		combo = (Spinner) findViewById(R.id.spinner1);
		
		ArrayAdapter<CartCre> adapterBanco = new ArrayAdapter<CartCre>(this, android.R.layout.simple_spinner_item, listaCartCre);  
		combo.setAdapter(adapterBanco);	
		
		//RECEBE O MÊS
		mes = (TextView) findViewById(R.id.editText1);
		mes.requestFocus();
				
		// RECEBE O ANO
		ano = (TextView) findViewById(R.id.editText2);
		
		
		//SALVA UMA NOVA FATURA      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
						if (listaCartCre.size() == 0 || String.valueOf(ano.getText()).equals("") || String.valueOf(mes.getText()).equals("")){
						validaDados();
						}
						else if (Integer.parseInt(String.valueOf(mes.getText())) > 12)
							dataInvalida();
						else{
							gravarFatura();
							finish();
						}
				}
		});
		
		//CANCELA UMA NOVA CONTA     
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
		});
		
		combo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Alisson","clicado na posicao = " + position);
				combo.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});

		
	}
	
	public List<CartCre> loadCartCre(){
		try {
			return listaCartCre = (ArrayList<CartCre>) ctrlCartCre.buscaCartCre();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void gravarFatura(){
		try {
				ctrlFecharFatura.gravarFatura(listaCartCre.get(combo.getSelectedItemPosition()), Integer.parseInt(mes.getText().toString()), Integer.parseInt(ano.getText().toString()));
				Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
			}
			
			catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void validaDados(){
		Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
	}
	
	public void dataInvalida(){
		Toast.makeText(this, "O mês deve ser entre 1 e 12", Toast.LENGTH_SHORT).show();
		mes.setText("");
	}
	
}