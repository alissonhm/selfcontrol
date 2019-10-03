package com.packageview.selfcontrol;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CadBancoReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Banco banco;
	private TextView codigo;
	private TextView descricao;
	private CtrlBanco ctrlBanco;
	private boolean novo;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadbancoreg);
		
		banco = new Banco();
		ctrlBanco = new CtrlBanco(this.getApplicationContext());
		novo = true;
		
		
		// RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.editText2);
		codigo.requestFocus();
		
		//RECEBE A DESCRICAO
		descricao = (TextView) findViewById(R.id.editText1);

		
		banco = (Banco) getIntent().getSerializableExtra("banco");
		
		try {
			codigo.setText(String.valueOf(ctrlBanco.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		if(banco instanceof Banco){
			novo = false;
			codigo.setText(String.valueOf(banco.getCodigo()));
			descricao.setText(banco.getDescricao());
			codigo.setEnabled(false);
		}
		
		//SALVA UM NOVO CAIXA      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if (String.valueOf(codigo.getText()).equals("") || String.valueOf(descricao.getText()).equals("")){
						validaCadastro();
					}
					else{
						if (novo){
							banco = new Banco(Integer.parseInt(codigo.getText().toString()), descricao.getText().toString());
							if (validaTela()){
								setResult(2,null);
								finish();							
							}
						}
						else{
							editarBanco();
							setResult(2,null);
							finish();
						}
					}
				}
		});
		
		//CANCELA UM NOVO CAIXA      
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
				
		});
	}
	
	
	public void gravarBanco(){
		banco = new Banco(Integer.parseInt(codigo.getText().toString()), descricao.getText().toString());
		try {
			
			ctrlBanco.gravarBanco(banco);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarBanco(){
		banco = new Banco(Integer.parseInt(codigo.getText().toString()), descricao.getText().toString());
		try {
			ctrlBanco.editaBanco(banco);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(){
		try {
			if (ctrlBanco.validaCodigo(banco)){
				Toast.makeText(this, "Banco já cadastrado \nFavor inserir um código válido", Toast.LENGTH_SHORT).show();
				codigo.setText("");
				codigo.requestFocus();
				return false;
			}else{
				gravarBanco();
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

