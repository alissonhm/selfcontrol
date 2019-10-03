package com.packageview.selfcontrol;

import java.util.Date;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagecontroller.selfcontrol.CtrlTitPag;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.TituloPagar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MovTitPagReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Button btnCalendario;
	private TextView codigo, fornecedor, documento, vencimento, valor, desconto, acrescimo, saldo;
	private boolean novo;
	private CtrlTitPag ctrlTitPag;
	private TituloPagar tituloPagar;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_titulopagarreg);
		
		tituloPagar = new TituloPagar();
		ctrlTitPag = new CtrlTitPag(this.getApplicationContext());
		novo = true;
		
		// RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.editText6);
		codigo.setEnabled(false);
				
		//RECEBE O FORNECEDOR
		fornecedor = (TextView) findViewById(R.id.editText1);
		
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
		
		tituloPagar = (TituloPagar) getIntent().getSerializableExtra("tituloPagar");
				
		try {
			codigo.setText(String.valueOf(ctrlTitPag.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tituloPagar instanceof TituloPagar){
			novo = false;
			codigo.setText(String.valueOf(tituloPagar.getCodigo()));
			fornecedor.setText(tituloPagar.getFornecedor());
			documento.setText(tituloPagar.getDocumento());
			vencimento.setText(String.valueOf(tituloPagar.getVencimento()));
			valor.setText(String.valueOf(tituloPagar.getValor()));
			desconto.setText(String.valueOf(tituloPagar.getDesconto()));
			acrescimo.setText(String.valueOf(tituloPagar.getAcrescimo()));
			saldo.setText(String.valueOf(tituloPagar.getSaldo()));
		}
		
		
		//SALVA UMA NOVO TITULO A PAGAR
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (String.valueOf(fornecedor.getText()).equals("") || String.valueOf(documento.getText()).equals("") || String.valueOf(vencimento.getText()).equals("") || String.valueOf(valor.getText()).equals("")){
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
						tituloPagar = new TituloPagar(Integer.parseInt(codigo.getText().toString()), fornecedor.getText().toString(), documento.getText().toString(), vencimento.getText().toString(), Float.parseFloat(valor.getText().toString()), Float.parseFloat(desconto.getText().toString()), Float.parseFloat(acrescimo.getText().toString()), "A", Float.parseFloat(valor.getText().toString()));
						if (validaTela()){
							setResult(2,null);
							finish();							
						}
					}
					else{
						editarTituloPagar();
						setResult(2,null);
						finish();
					}
				}
			}
		});
		//CANCELA UMA NOVO TITULO A PAGAR      
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
		});
	}
		
		public void gravarTiuloPagar(){
			tituloPagar = new TituloPagar(Integer.parseInt(codigo.getText().toString()), fornecedor.getText().toString(), documento.getText().toString(), vencimento.getText().toString(), Float.parseFloat(valor.getText().toString()), Float.parseFloat(desconto.getText().toString()), Float.parseFloat(acrescimo.getText().toString()), "A", Float.parseFloat(valor.getText().toString()));
			try {
				
				ctrlTitPag.gravarTitPag(tituloPagar);
				Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void editarTituloPagar(){
			tituloPagar = new TituloPagar(Integer.parseInt(codigo.getText().toString()), fornecedor.getText().toString(), documento.getText().toString(), vencimento.getText().toString(), Float.parseFloat(valor.getText().toString()), Float.parseFloat(desconto.getText().toString()), Float.parseFloat(acrescimo.getText().toString()), "A", Float.parseFloat(saldo.getText().toString()));
			try {
				ctrlTitPag.editaTitPag(tituloPagar);
				Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean validaTela(){
			try {
				if (ctrlTitPag.validaCodigo(tituloPagar)){
					Toast.makeText(this, "Título já cadastrado \nFavor inserir um código válido", Toast.LENGTH_SHORT).show();
					documento.setText("");
					documento.requestFocus();
					return false;
				}else{
					gravarTiuloPagar();
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public void validaCadastro(){
			Toast.makeText(this, "Fornecedor, Documento, Vencimento, e Valor são obrigatórios", Toast.LENGTH_SHORT).show();
		}
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				vencimento.setText(bundle.getString("data"));
			}
			
		}

	}

