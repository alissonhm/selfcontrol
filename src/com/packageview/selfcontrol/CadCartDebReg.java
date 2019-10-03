package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlCartDeb;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagemodel.selfcontrol.CartDeb;
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

public class CadCartDebReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Conta conta;
	private CartDeb cartDeb;
	private CtrlCartDeb ctrlCartDeb;
	private boolean novo;
	private TextView codigo, descricao, nroCart, bandeira;
	private ArrayList<Conta> listaConta;
	private CtrlConta ctrlConta;
	private Spinner combo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcartdebreg);
		
		conta = new Conta();
		cartDeb= new CartDeb();
		
		ctrlCartDeb = new CtrlCartDeb(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText01);
		codigo.setEnabled(false);
		
		//RECEBE O NRO DO CARTAO
		nroCart = (TextView) findViewById(R.id.EditText02);
		nroCart.requestFocus();
		
		//RECEBE O SPINNER
		ctrlConta = new CtrlConta(this.getApplicationContext());
		listaConta = new ArrayList<Conta>();
		loadConta();
		combo = (Spinner) findViewById(R.id.spinner1);
				
		ArrayAdapter<Conta> adapterConta = new ArrayAdapter<Conta>(this, android.R.layout.simple_spinner_item, listaConta);  
		combo.setAdapter(adapterConta);
		
		// RECEBE A DESRCRICAO
		descricao = (TextView) findViewById(R.id.editText2);
				
		// RECEBE A BANDEIRA
		bandeira = (TextView) findViewById(R.id.editText4);
		
		
		cartDeb = (CartDeb) getIntent().getSerializableExtra("cartDeb");
		
		try {
			codigo.setText(String.valueOf(ctrlCartDeb.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(cartDeb instanceof CartDeb){
			novo = false;
			codigo.setText(String.valueOf(cartDeb.getCodigo()));
			int i = 0;
			for (Conta conta: listaConta){
				if (conta.getCodigo() == cartDeb.getConta().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}	
			nroCart.setText(cartDeb.getNroCart());
			descricao.setText(cartDeb.getDescricao());
			bandeira.setText(cartDeb.getBandeira());
		}
		
		//SALVA UM NOVO CARTAO      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(String.valueOf(nroCart.getText()).equals("") || String.valueOf(descricao.getText()).equals("") || String.valueOf(bandeira.getText()).equals("") || listaConta.size() == 0){
					validaCadastro();
				}
				else{
					if (novo){
						cartDeb = new CartDeb(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), nroCart.getText().toString());
						if (validaTela(true)){
							setResult(2,null);
							finish();							
							}
						}else{
							cartDeb = new CartDeb(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), nroCart.getText().toString());
							if (validaTela(false)){
							setResult(2,null);
							finish();
							}
						}
				}
			}
	});
		
		//CANCELA UM NOVO CARTAO      
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
	
	public void gravarCartDeb(){
		cartDeb = new CartDeb(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), nroCart.getText().toString());
		try {
			
			ctrlCartDeb.gravarCartDeb(cartDeb);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarCartDeb(){
		cartDeb = new CartDeb(Integer.parseInt(codigo.getText().toString()), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), nroCart.getText().toString());
		try {
			ctrlCartDeb.editaCartDeb(cartDeb);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(boolean novoCad){
		try {
			if (ctrlCartDeb.validaDados(cartDeb)){
				Toast.makeText(this, "Cartão já cadastrado \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
				nroCart.setText("");
				return false;
			}else{
				if (novoCad)
					gravarCartDeb();
				else
					editarCartDeb();
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
