package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlCartCre;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagemodel.selfcontrol.CartCre;
import com.packagemodel.selfcontrol.CartDeb;
import com.packagemodel.selfcontrol.Conta;

import android.app.Activity;
import android.content.pm.FeatureInfo;
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

public class CadCartCreReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Conta conta;
	private CartCre cartCre;
	private CtrlCartCre ctrlCartCre;
	private boolean novo;
	private TextView codigo, nroCart, descricao, bandeira, limite, dtFech, dtVenc;
	private CtrlConta ctrlConta;
	private ArrayList<Conta> listaConta;
	private Spinner combo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcartcrereg);
		
		conta = new Conta();
		cartCre= new CartCre();
		
		ctrlCartCre = new CtrlCartCre(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText02);
		codigo.setEnabled(false);
		
		//RECEBE O NRO DO CARTAO
		nroCart = (TextView) findViewById(R.id.EditText01);
		nroCart.requestFocus();
		
		//RECEBE O SPINNER
		ctrlConta = new CtrlConta(this.getApplicationContext());
		listaConta = new ArrayList<Conta>();
		loadConta();
		combo = (Spinner) findViewById(R.id.spinner1);
				
		ArrayAdapter<Conta> adapterConta = new ArrayAdapter<Conta>(this, android.R.layout.simple_spinner_item, listaConta);  
		combo.setAdapter(adapterConta);
		
		// RECEBE A DESRCRICAO
		descricao = (TextView) findViewById(R.id.EditText03);
				
		// RECEBE A BANDEIRA
		bandeira = (TextView) findViewById(R.id.EditText04);
		
		// RECEBE A LIMITE
		limite = (TextView) findViewById(R.id.EditText05);
		
		// RECEBE A DATA DE FECHAMENTO
		dtFech = (TextView) findViewById(R.id.EditText06);
		
		// RECEBE A DATA DE VENCIMENTO
		dtVenc= (TextView) findViewById(R.id.EditText07);
		
		cartCre = (CartCre) getIntent().getSerializableExtra("cartCre");
		
		try {
			codigo.setText(String.valueOf(ctrlCartCre.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(cartCre instanceof CartCre){
			novo = false;
			codigo.setText(String.valueOf(cartCre.getCodigo()));
			nroCart.setText(cartCre.getNroCartao());
			int i = 0;
			for (Conta conta: listaConta){
				if (conta.getCodigo() == cartCre.getConta().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}
			descricao.setText(cartCre.getDescricao());
			bandeira.setText(cartCre.getBandeira());
			limite.setText(String.valueOf(cartCre.getLimite()));
			dtFech.setText(String.valueOf(cartCre.getDtFechamento()));
			dtVenc.setText(String.valueOf(cartCre.getDtVencimento()));		
		}
		
		//SALVA UM NOVO CARTAO      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(String.valueOf(nroCart.getText()).equals("") || String.valueOf(descricao.getText()).equals("") || String.valueOf(bandeira.getText()).equals("") || String.valueOf(limite.getText()).equals("") || String.valueOf(dtFech.getText()).equals("") || String.valueOf(dtVenc.getText()).equals("") || listaConta.size() == 0){
					validaCadastro();
				}
				else if(Integer.parseInt(String.valueOf(dtFech.getText())) > 31 || Integer.parseInt(String.valueOf(dtVenc.getText())) > 31)
					dataInvalida();
				else{
					if (novo){
						cartCre = new CartCre(Integer.parseInt(codigo.getText().toString()), nroCart.getText().toString(), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), Float.parseFloat(limite.getText().toString()), Integer.parseInt(dtFech.getText().toString()), Integer.parseInt(dtVenc.getText().toString()));
						if (validaTela(true)){
							setResult(2,null);
							finish();							
							}
						}else{
							cartCre = new CartCre(Integer.parseInt(codigo.getText().toString()), nroCart.getText().toString(), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), Float.parseFloat(limite.getText().toString()), Integer.parseInt(dtFech.getText().toString()), Integer.parseInt(dtVenc.getText().toString()));
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
		
		public void gravarCartCre(){
			cartCre = new CartCre(Integer.parseInt(codigo.getText().toString()), nroCart.getText().toString(), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), Float.parseFloat(limite.getText().toString()), Integer.parseInt(dtFech.getText().toString()), Integer.parseInt(dtVenc.getText().toString()));
			try {
				
				ctrlCartCre.gravarCartCre(cartCre);
				Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void editarCartCre(){
			cartCre = new CartCre(Integer.parseInt(codigo.getText().toString()), nroCart.getText().toString(), listaConta.get(combo.getSelectedItemPosition()), descricao.getText().toString(), bandeira.getText().toString(), Float.parseFloat(limite.getText().toString()), Integer.parseInt(dtFech.getText().toString()), Integer.parseInt(dtVenc.getText().toString()));
			try {
				ctrlCartCre.editaCartCre(cartCre);
				Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean validaTela(boolean novoCad){
			try {
				if (ctrlCartCre.validaDados(cartCre)){
					Toast.makeText(this, "Cartão já cadastrado \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
					nroCart.setText("");
					return false;
				}else{
					if (novoCad)
						gravarCartCre();
					else
						editarCartCre();
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
		
		public void dataInvalida(){
			Toast.makeText(this, "Os campos de data devem conter valores entre 1 e 31", Toast.LENGTH_SHORT).show();
			if (Integer.parseInt(String.valueOf(dtFech.getText())) > 31)
				dtFech.setText("");
			if (Integer.parseInt(String.valueOf(dtVenc.getText())) > 31)
				dtVenc.setText("");
		}
		

	}
