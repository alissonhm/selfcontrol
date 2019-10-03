package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

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

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterConta;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;

public class CadContaReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Spinner combo;
	private MinhaClasseAdapterConta mca;
	private ArrayList<Banco> listaBanco;
	private CtrlBanco ctrlBanco;
	private CtrlConta ctrlConta;
	private TextView codigo, agencia, nroConta, operacao, descricao;
	private boolean novo;
	private Conta conta;
	private Banco banco;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcontareg);
		
		
		banco = new Banco();
		conta = new Conta();
		
		ctrlConta = new CtrlConta(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText01);
		codigo.setEnabled(false);
		
		//RECEBE O SPINNER
		ctrlBanco = new CtrlBanco(this.getApplicationContext());
		listaBanco = new ArrayList<Banco>();
		loadBanco();
		combo = (Spinner) findViewById(R.id.spinner1);
		
		ArrayAdapter<Banco> adapterBanco = new ArrayAdapter<Banco>(this, android.R.layout.simple_spinner_item, listaBanco);  
		combo.setAdapter(adapterBanco);		
		
		// RECEBE A AGENCIA
		agencia = (TextView) findViewById(R.id.editText1);
		agencia.requestFocus();
		
		// RECEBE A CONTA
		nroConta = (TextView) findViewById(R.id.editText2);
		
		// RECEBE A OPERACAO
		operacao = (TextView) findViewById(R.id.editText4);
		
		// RECEBE A DESCRICAO
		descricao = (TextView) findViewById(R.id.editText3);	
		
		conta = (Conta) getIntent().getSerializableExtra("conta");
		
		try {
			codigo.setText(String.valueOf(ctrlConta.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(conta instanceof Conta){
			novo = false;
			codigo.setText(String.valueOf(conta.getCodigo()));
			int i = 0;
			for (Banco banco : listaBanco){
				if (banco.getCodigo() == conta.getBanco().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}			
			agencia.setText(conta.getAgencia());
			nroConta.setText(conta.getConta());
			operacao.setText(conta.getOperacao());
			descricao.setText(conta.getDescricao());
		}
		
		
		//SALVA UMA NOVA CONTA      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
						if (listaBanco.size() == 0 || String.valueOf(agencia.getText()).equals("") || String.valueOf(nroConta.getText()).equals("") || String.valueOf(operacao.getText()).equals("") || String.valueOf(descricao.getText()).equals("")){
							validaDados();
						}
						else{
							if (novo){
								conta = new Conta(Integer.parseInt(codigo.getText().toString()), listaBanco.get(combo.getSelectedItemPosition()), agencia.getText().toString(), nroConta.getText().toString(), operacao.getText().toString(), descricao.getText().toString());
								if (validaTela(true)){
									setResult(2,null);
									finish();							
								}
							}
							else{
								conta = new Conta(Integer.parseInt(codigo.getText().toString()), listaBanco.get(combo.getSelectedItemPosition()), agencia.getText().toString(), nroConta.getText().toString(), operacao.getText().toString(), descricao.getText().toString());
								if (validaTela(false)){
								setResult(2,null);
								finish();
								}
							}
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
	
	public List<Banco> loadBanco(){
		try {
			return listaBanco = (ArrayList<Banco>) ctrlBanco.buscaBanco();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void gravarConta(){
		conta = new Conta(Integer.parseInt(codigo.getText().toString()), listaBanco.get(combo.getSelectedItemPosition()), agencia.getText().toString(), nroConta.getText().toString(), operacao.getText().toString(), descricao.getText().toString());
		try {
			
			ctrlConta.gravarConta(conta);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarConta(){
		conta = new Conta(Integer.parseInt(codigo.getText().toString()), listaBanco.get(combo.getSelectedItemPosition()), agencia.getText().toString(), nroConta.getText().toString(), operacao.getText().toString(), descricao.getText().toString());
		try {
			ctrlConta.editaConta(conta);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(boolean novoCad){
		try {
			if (ctrlConta.validaDados(conta)){
				Toast.makeText(this, "Conta já cadastrada \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
				agencia.setText("");
				nroConta.setText("");
				operacao.setText("");
				agencia.requestFocus();
				return false;
			}else{
				if (novoCad)
					gravarConta();
				else
					editarConta();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public void validaDados(){
		Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
	}
	
}