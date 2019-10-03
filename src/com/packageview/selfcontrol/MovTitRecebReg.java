package com.packageview.selfcontrol;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlTitRec;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MovTitRecebReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Button btnCalendario;
	private TituloReceber tituloReceber;
	private CtrlTitRec ctrlTitRec;
	private boolean novo;
	private TextView codigo, cliente, documento, vencimento, valor, desconto, acrescimo, saldo;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_titulorecebreg);
		
		tituloReceber = new TituloReceber();
		ctrlTitRec = new CtrlTitRec(this.getApplicationContext());
		novo = true;
		
		// RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.editText6);
		codigo.setEnabled(false);
				
		//RECEBE O CLIENTE
		cliente = (TextView) findViewById(R.id.editText1);
		
		//RECEBE O DOCUMENTO
		documento = (TextView) findViewById(R.id.editText2);
		
		//RECEBE O VENCIMENTO
		vencimento = (TextView) findViewById(R.id.editText3);
		vencimento.setEnabled(false);
		
		//RECEBE O BOTÃO CALENDÁRIO
		btnCalendario = (Button) findViewById(R.id.button2);		
		btnCalendario.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), Calendario.class);
					startActivityForResult(i, 10);
				}
		});
		
		//RECEBE O VALOR
		valor = (TextView) findViewById(R.id.editText4);
		
		//RECEBE O DESCONTO
		desconto = (TextView) findViewById(R.id.editText5);
		desconto.setText("0");
		
		//RECEBE O ACRESCIMO
		acrescimo = (TextView) findViewById(R.id.EditText01);
		acrescimo.setText("0");
		
		//RECEBE O SALDO
		saldo = (TextView) findViewById(R.id.EditText02);
		saldo.setEnabled(false);
		
		
		tituloReceber = (TituloReceber) getIntent().getSerializableExtra("tituloReceber");
				
		try {
			codigo.setText(String.valueOf(ctrlTitRec.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tituloReceber instanceof TituloReceber){
			novo = false;
			codigo.setText(String.valueOf(tituloReceber.getCodigo()));
			cliente.setText(tituloReceber.getCliente());
			documento.setText(tituloReceber.getDocumento());
			vencimento.setText(String.valueOf(tituloReceber.getVencimento()));
			valor.setText(String.valueOf(tituloReceber.getValor()));
			desconto.setText(String.valueOf(tituloReceber.getDesconto()));
			acrescimo.setText(String.valueOf(tituloReceber.getAcrescimo()));
			saldo.setText(String.valueOf(tituloReceber.getSaldo()));
		}
		
		
		//SALVA UM NOVO TITULO A RECEBER      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (String.valueOf(cliente.getText()).equals("") || String.valueOf(documento.getText()).equals("") || String.valueOf(vencimento.getText()).equals("") || String.valueOf(valor.getText()).equals("")){
					validaCadastro();
				}
				else{
					if (novo){
						if (String.valueOf(desconto.getText()).equals("")){
							desconto.setText("0");
						}
						if (String.valueOf(acrescimo.getText()).equals("")){
							acrescimo.setText("0");
						}
						tituloReceber = new TituloReceber(Integer.parseInt(codigo.getText().toString()), cliente.getText().toString(), documento.getText().toString(), vencimento.getText().toString(), Float.parseFloat(valor.getText().toString()), Float.parseFloat(desconto.getText().toString()), Float.parseFloat(acrescimo.getText().toString()), "A", Float.parseFloat(valor.getText().toString()));
						if (validaTela()){
							setResult(2,null);
							finish();							
						}
					}
					else{
						editarTituloReceber();
						setResult(2,null);
						finish();
					}
			
				}
			}	
		});
		//CANCELA UM NOVO TITULO A RECEBER      
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
		});
	}
	
		public void gravarTiuloReceber(){
			tituloReceber = new TituloReceber(Integer.parseInt(codigo.getText().toString()), cliente.getText().toString(), documento.getText().toString(), vencimento.getText().toString(), Float.parseFloat(valor.getText().toString()), Float.parseFloat(desconto.getText().toString()), Float.parseFloat(acrescimo.getText().toString()), "A", Float.parseFloat(valor.getText().toString()));
			try {
				
				ctrlTitRec.gravarTitRec(tituloReceber);
				Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void editarTituloReceber(){
			tituloReceber = new TituloReceber(Integer.parseInt(codigo.getText().toString()), cliente.getText().toString(), documento.getText().toString(), vencimento.getText().toString(), Float.parseFloat(valor.getText().toString()), Float.parseFloat(desconto.getText().toString()), Float.parseFloat(acrescimo.getText().toString()), "A", Float.parseFloat(saldo.getText().toString()));
			try {
				ctrlTitRec.editaTitRec(tituloReceber);
				Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean validaTela(){
			try {
				if (ctrlTitRec.validaCodigo(tituloReceber)){
					Toast.makeText(this, "Título já cadastrado \nFavor inserir um código válido", Toast.LENGTH_SHORT).show();
					documento.setText("");
					documento.requestFocus();
					return false;
				}else{
					gravarTiuloReceber();
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public void validaCadastro(){
			Toast.makeText(this, "Cliente, Documento, Vencimento, e Valor são obrigatórios", Toast.LENGTH_SHORT).show();
		}
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				vencimento.setText(bundle.getString("data"));
			}
			
		}

	}

