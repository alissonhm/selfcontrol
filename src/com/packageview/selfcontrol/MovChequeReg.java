package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlCheque;
import com.packagecontroller.selfcontrol.CtrlMovCheque;
import com.packagemodel.selfcontrol.Cheque;
import com.packagemodel.selfcontrol.MovimentoCheque;

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


public class MovChequeReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private MovimentoCheque movimentoCheque;
	private Cheque cheque;
	private CtrlMovCheque ctrlMovCheque;
	private boolean novo;
	private TextView codigo, valor;
	private CtrlCheque ctrlCheque;
	private ArrayList<Cheque> listaCheque;
	private Spinner combo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movchequereg);
		
		cheque = new Cheque();
		movimentoCheque= new MovimentoCheque();
		
		ctrlMovCheque = new CtrlMovCheque(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText01);
		codigo.setEnabled(false);
		
		//RECEBE O SPINNER
		ctrlCheque = new CtrlCheque(this.getApplicationContext());
		listaCheque = new ArrayList<Cheque>();
		loadCheque();
		combo = (Spinner) findViewById(R.id.spinner2);
				
		ArrayAdapter<Cheque> adapterCheque = new ArrayAdapter<Cheque>(this, android.R.layout.simple_spinner_item, listaCheque);  
		combo.setAdapter(adapterCheque);
		
		// RECEBE O VALOR
		valor = (TextView) findViewById(R.id.editText1);
				
		movimentoCheque = (MovimentoCheque) getIntent().getSerializableExtra("movCheque");
		
		try {
			codigo.setText(String.valueOf(ctrlMovCheque.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(movimentoCheque instanceof MovimentoCheque){
			novo = false;
			codigo.setText(String.valueOf(movimentoCheque.getCodigo()));
			int i = 0;
			for (Cheque cheque: listaCheque){
				if (cheque.getCodigo() == movimentoCheque.getCheque().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}
			valor.setText(String.valueOf(movimentoCheque.getValor()));	
		}
		
		//SALVA UMA NOVA MOVIMENTAÇÃO      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(String.valueOf(valor.getText()).equals("") ||  listaCheque.size() == 0){
					validaCadastro();
				}
				else{
					if (novo){
						movimentoCheque = new MovimentoCheque(Integer.parseInt(codigo.getText().toString()), listaCheque.get(combo.getSelectedItemPosition()),  0, Float.parseFloat(valor.getText().toString()), "A");
						if (validaTela()){
							setResult(2,null);
							finish();							
							}
						}else{
							editarMovCheque();
							setResult(2,null);
							finish();
						}
				}
			}
	});
		
		//CANCELA UM NOVO CADASTRO      
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

		public List<Cheque> loadCheque(){
			try {
				return listaCheque = (ArrayList<Cheque>) ctrlCheque.buscaCheque();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public boolean gravarMovCheque(){
			movimentoCheque = new MovimentoCheque(Integer.parseInt(codigo.getText().toString()), listaCheque.get(combo.getSelectedItemPosition()),  0, Float.parseFloat(valor.getText().toString()), "A");
			try {
				if (ctrlMovCheque.gravarMovCheque(movimentoCheque)){
					Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
					return true;
				}
				else{
					Toast.makeText(this, "Esse talão não possui mais folhas disponíveis", Toast.LENGTH_SHORT).show();
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		public void editarMovCheque(){
			movimentoCheque = new MovimentoCheque(Integer.parseInt(codigo.getText().toString()), listaCheque.get(combo.getSelectedItemPosition()),  0, Float.parseFloat(valor.getText().toString()), "A");
			try {
				ctrlMovCheque.editaMovCheque(movimentoCheque);
				Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean validaTela(){
			try {
				if (ctrlMovCheque.validaCodigo(movimentoCheque)){
					Toast.makeText(this, "Movimentação já cadastrado \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
					valor.setText("");
					return false;
				}else{
					if (gravarMovCheque())
						return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return false;
		}
		
		public void validaCadastro(){
			Toast.makeText(this, "Todos os dados são obrigatórios", Toast.LENGTH_SHORT).show();
		}
		

	}
