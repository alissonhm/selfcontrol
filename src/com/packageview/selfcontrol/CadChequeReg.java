package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagecontroller.selfcontrol.CtrlCheque;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Cheque;
import com.packagemodel.selfcontrol.Conta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CadChequeReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Conta conta;
	private Cheque cheque;
	private boolean novo;
	private CtrlCheque ctrlCheque;
	private CtrlConta ctrlConta;
	private TextView codigo, descricao, folhasRest;
	private ArrayList<Conta> listaConta;
	private Spinner combo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadchequereg);
		
		conta = new Conta();
		cheque= new Cheque();
		
		ctrlCheque = new CtrlCheque(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText01);
		codigo.setEnabled(false);
		
		//RECEBE O SPINNER
		ctrlConta = new CtrlConta(this.getApplicationContext());
		listaConta = new ArrayList<Conta>();
		loadConta();
		combo = (Spinner) findViewById(R.id.spinner1);
				
		ArrayAdapter<Conta> adapterConta = new ArrayAdapter<Conta>(this, android.R.layout.simple_spinner_item, listaConta);  
		combo.setAdapter(adapterConta);
		
		// RECEBE A DESRCRICAO
		descricao = (TextView) findViewById(R.id.editText1);
		descricao.requestFocus();
				
		// RECEBE O NRO DE FOLHAS
		folhasRest = (TextView) findViewById(R.id.editText2);
		
		
		cheque = (Cheque) getIntent().getSerializableExtra("cheque");
		
		try {
			codigo.setText(String.valueOf(ctrlCheque.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(cheque instanceof Cheque){
			novo = false;
			codigo.setText(String.valueOf(cheque.getCodigo()));
			int i = 0;
			for (Conta conta: listaConta){
				if (conta.getCodigo() == cheque.getConta().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}			
			descricao.setText(cheque.getDescricao());
			folhasRest.setText(String.valueOf(cheque.getFolhasRest()));
		}
		
		
		//SALVA UM NOVO TALÃO     
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listaConta.size() == 0 || String.valueOf(descricao.getText()).equals("") || String.valueOf(folhasRest.getText()).equals("")){
						validaCadastro();
					}
					else{
						if (novo){
							cheque = new Cheque(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), Integer.parseInt(folhasRest.getText().toString()));
							if (validaTela()){
								setResult(2,null);
								finish();							
								}
							}else{
								editarCheque();
								setResult(2,null);
								finish();
							}
					}
				}
		});
		
		//CANCELA UM NOVO TALÃO     
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

	public List<Conta> loadConta(){
		try {
			return listaConta = (ArrayList<Conta>) ctrlConta.buscaConta();
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void gravarCheque(){
		cheque = new Cheque(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), Integer.parseInt(folhasRest.getText().toString()));
		try {
			
			ctrlCheque.gravarCheque(cheque);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarCheque(){
		cheque = new Cheque(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), Integer.parseInt(folhasRest.getText().toString()));
		try {
			ctrlCheque.editaCheque(cheque);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(){
		try {
			if (ctrlCheque.validaDados(cheque)){
				Toast.makeText(this, "Cheque já cadastrado \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
				descricao.setText("");
				folhasRest.setText("");
				return false;
			}else{
				gravarCheque();
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

}
