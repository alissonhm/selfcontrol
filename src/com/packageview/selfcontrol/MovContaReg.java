package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagecontroller.selfcontrol.CtrlMovConta;
import com.packagecontroller.selfcontrol.CtrlTipoMovConta;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.MovimentoCheque;
import com.packagemodel.selfcontrol.MovimentoConta;
import com.packagemodel.selfcontrol.TipoMovimentoConta;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.app.Activity;
import android.content.Intent;
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

public class MovContaReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Button btnCalendario;
	private Conta conta;
	private MovimentoConta movimentoConta;
	private CtrlMovConta ctrlMovConta;
	private CtrlConta ctrlConta;
	private boolean novo;
	private TextView codigo, documento, data, valor;
	private ArrayList<Conta> listaConta;
	private ArrayList<TipoMovimentoConta> listaTipoMov;
	private Spinner comboConta, comboMov;
	private CtrlTipoMovConta ctrlTipoMovConta;
	private TituloPagar tituloPagar;
	private TituloReceber tituloReceber;
	private MovimentoCheque movimentoCheque;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcontareg);
		
		tituloPagar = new TituloPagar();
		tituloReceber = new TituloReceber();
		movimentoCheque = new MovimentoCheque();
		conta = new Conta();
		movimentoConta= new MovimentoConta();
		
		ctrlMovConta = new CtrlMovConta(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.editText3);
		codigo.setEnabled(false);
		
		//RECEBE O SPINNER DO CAIXA
		ctrlConta = new CtrlConta(this.getApplicationContext());
		listaConta = new ArrayList<Conta>();
		loadConta();
		comboConta = (Spinner) findViewById(R.id.spinner1);
				
		ArrayAdapter<Conta> adapterConta = new ArrayAdapter<Conta>(this, android.R.layout.simple_spinner_item, listaConta);  
		comboConta.setAdapter(adapterConta);
		
		//RECEBE O SPINNER TIPO DE MOVIMENTAÇÃO
		ctrlTipoMovConta = new CtrlTipoMovConta(this.getApplicationContext());
		listaTipoMov = new ArrayList<TipoMovimentoConta>();
		loadTipoMov();
		comboMov = (Spinner) findViewById(R.id.spinner2);
				
		ArrayAdapter<TipoMovimentoConta> adapterTipoMov = new ArrayAdapter<TipoMovimentoConta>(this, android.R.layout.simple_spinner_item, listaTipoMov);  
		comboMov.setAdapter(adapterTipoMov);
		
		// RECEBE O DOCUMENTO
		documento = (TextView) findViewById(R.id.editText1);
		
		// RECEBE A DATA
		data = (TextView) findViewById(R.id.editText2);
		data.setEnabled(false);
		
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
		
		// RECEBE O VALOR
		valor = (TextView) findViewById(R.id.editText4);
				
		movimentoConta = (MovimentoConta) getIntent().getSerializableExtra("movConta");
		
		try {
			codigo.setText(String.valueOf(ctrlMovConta.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(movimentoConta instanceof MovimentoConta){
			novo = false;
			codigo.setText(String.valueOf(movimentoConta.getCodigo()));
			int i = 0, j = 0;
			for (Conta conta: listaConta){
				if (conta.getCodigo() == movimentoConta.getConta().getCodigo()){
					comboConta.setSelection(i);
					break;
				}
				i++;
			}
			for (TipoMovimentoConta tipoMovimentoConta: listaTipoMov){
				if (tipoMovimentoConta.getCodigo() == movimentoConta.getTipoMovimentoConta().getCodigo()){
					comboMov.setSelection(j);
					break;
				}
				j++;
			}
			documento.setText(String.valueOf(movimentoConta.getDocumento()));
			data.setText(String.valueOf(movimentoConta.getData()));
			if (movimentoConta.getValor() < 0){
				valor.setText(String.valueOf(movimentoConta.getValor() * -1));	
			}
			else{
				valor.setText(String.valueOf(movimentoConta.getValor()));
			}
		}
		
		tituloPagar = (TituloPagar) getIntent().getSerializableExtra("movContaTitPag");
		
		if(tituloPagar instanceof TituloPagar){
			try {
				codigo.setText(String.valueOf(ctrlMovConta.buscaCodigo()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			comboMov.setSelection(0);
			comboMov.setEnabled(false);
			documento.setText(String.valueOf(tituloPagar.getDocumento()));
			valor.setText(String.valueOf(tituloPagar.getSaldo() + tituloPagar.getAcrescimo() - tituloPagar.getDesconto()));
		}
		
		tituloReceber = (TituloReceber) getIntent().getSerializableExtra("movContaTitRec");
		
		if(tituloReceber instanceof TituloReceber){
			try {
				codigo.setText(String.valueOf(ctrlMovConta.buscaCodigo()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			comboMov.setSelection(1);
			comboMov.setEnabled(false);
			documento.setText(String.valueOf(tituloReceber.getDocumento()));
			valor.setText(String.valueOf(tituloReceber.getSaldo() + tituloReceber.getAcrescimo() - tituloReceber.getDesconto()));
		}
		
		movimentoCheque = (MovimentoCheque) getIntent().getSerializableExtra("movCheque");
		
		if(movimentoCheque instanceof MovimentoCheque){
			try {
				codigo.setText(String.valueOf(ctrlMovConta.buscaCodigo()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			comboMov.setSelection(0);
			comboMov.setEnabled(false);
			valor.setText(String.valueOf(movimentoCheque.getValor()));
		}
		
		
		//SALVA UMA NOVA MOVIMENTACAO      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(String.valueOf(documento.getText()).equals("") || String.valueOf(data.getText()).equals("") || String.valueOf(valor.getText()).equals("") ||listaConta.size() == 0){
						validaCadastro();
					}
					else{
						if (novo){
							movimentoConta = new MovimentoConta(Integer.parseInt(codigo.getText().toString()), listaConta.get(comboConta.getSelectedItemPosition()), listaTipoMov.get(comboMov.getSelectedItemPosition()), data.getText().toString(), documento.getText().toString(), Float.parseFloat(valor.getText().toString()));
							if (validaTela()){
								setResult(2,null);
								finish();							
								}
							}else{
								editarMovConta();
								setResult(2,null);
								finish();
							}
					}
				}
		});
		
		//CANCELA UMA NOVA MOVIMENTACAO      
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
		});

		comboConta.setOnItemSelectedListener(new OnItemSelectedListener() {
			

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Alisson","clicado na posicao = " + position);
				comboConta.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
				}
			});
		
		comboMov.setOnItemSelectedListener(new OnItemSelectedListener() {
			

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Alisson","clicado na posicao = " + position);
				comboMov.setSelection(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
				}
			});
		
		comboMov.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (String.valueOf(listaTipoMov.get(comboMov.getSelectedItemPosition()).getTipo()).equals("PT")){
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovContaTitPag.class);
					finish();
					startActivityForResult(i, 10);
				}
				if (String.valueOf(listaTipoMov.get(comboMov.getSelectedItemPosition()).getTipo()).equals("RT")){
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovContaTitReceb.class);
					finish();
					startActivityForResult(i, 10);
				}
				if (String.valueOf(listaTipoMov.get(comboMov.getSelectedItemPosition()).getTipo()).equals("CC")){
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovContaMovCheque.class);
					finish();
					startActivityForResult(i, 10);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
		


		public List<Conta> loadConta(){
			try {
				return listaConta = (ArrayList<Conta>) ctrlConta.buscaConta();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public List<TipoMovimentoConta> loadTipoMov(){
			try {
				return listaTipoMov = (ArrayList<TipoMovimentoConta>) ctrlTipoMovConta.buscar();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public void gravarMovConta(){
			movimentoConta = new MovimentoConta(Integer.parseInt(codigo.getText().toString()), listaConta.get(comboConta.getSelectedItemPosition()), listaTipoMov.get(comboMov.getSelectedItemPosition()), data.getText().toString(), documento.getText().toString(), Float.parseFloat(valor.getText().toString()));
			try {
				
				ctrlMovConta.gravarMovConta(movimentoConta);
				Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tituloPagar instanceof TituloPagar){
				try {
					ctrlMovConta.baixaTituloPagar(tituloPagar, movimentoConta);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(tituloReceber instanceof TituloReceber){
				try {
					ctrlMovConta.baixaTituloReceber(tituloReceber, movimentoConta);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(movimentoCheque instanceof MovimentoCheque){
				try {
					ctrlMovConta.compensaCheque(movimentoCheque, movimentoConta);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void editarMovConta(){
			movimentoConta = new MovimentoConta(Integer.parseInt(codigo.getText().toString()), listaConta.get(comboConta.getSelectedItemPosition()), listaTipoMov.get(comboMov.getSelectedItemPosition()), data.getText().toString(), documento.getText().toString(), Float.parseFloat(valor.getText().toString()));
			try {
				ctrlMovConta.editaMovConta(movimentoConta);
				Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean validaTela(){
			try {
				if (ctrlMovConta.validaCodigo(movimentoConta)){
					Toast.makeText(this, "Movimentação já cadastrado \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
					documento.setText("");
					return false;
				}else{
					gravarMovConta();
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public void validaCadastro(){
			Toast.makeText(this, "Todos os dados são obrigatórios", Toast.LENGTH_SHORT).show();
		}
		
		protected void onActivityResult(int requestCode, int resultCode, Intent intent)
		{
			if (resultCode == RESULT_OK) {
				Bundle bundle = intent.getExtras();
				data.setText(bundle.getString("data"));
			}
			
		}

	}
