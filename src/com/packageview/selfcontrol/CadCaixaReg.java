package com.packageview.selfcontrol;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlCaixa;
import com.packagemodel.selfcontrol.Caixa;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CadCaixaReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Caixa caixa;
	private TextView codigo;
	private TextView descricao;
	private CtrlCaixa ctrlCaixa;
	private boolean novo;
	private int codAnt;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcaixareg);
		
		caixa = new Caixa();
		ctrlCaixa = new CtrlCaixa(this.getApplicationContext());
		novo = true;
		
		
		// RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.editText2);
		codigo.requestFocus();
		codigo.setEnabled(false);
		
		//RECEBE A DESCRICAO
		descricao = (TextView) findViewById(R.id.editText1);

		
		caixa = (Caixa) getIntent().getSerializableExtra("caixa");
		
		try {
			codigo.setText(String.valueOf(ctrlCaixa.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		if(caixa instanceof Caixa){
			novo = false;
			codigo.setText(String.valueOf(caixa.getCodigo()));
			descricao.setText(caixa.getDescricao());
		}
		
		//SALVA UM NOVO BANCO      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if (String.valueOf(codigo.getText()).equals("") || String.valueOf(descricao.getText()).equals("")){
						validaCadastro();
					}
					else{
						if (novo){
							caixa = new Caixa(Integer.parseInt(codigo.getText().toString()), descricao.getText().toString());
							if (validaTela()){
								setResult(2,null);
								finish();							
							}
						}
						else{
							editarCaixa();
							setResult(2,null);
							finish();
						}
					}
				}
		});
		
		//CANCELA UM NOVO BANCO      
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
				
		});
	}
	
	
	public void gravarCaixa(){
		caixa = new Caixa(Integer.parseInt(codigo.getText().toString()), descricao.getText().toString());
		try {
			
			ctrlCaixa.gravarCaixa(caixa);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarCaixa(){
		caixa = new Caixa(Integer.parseInt(codigo.getText().toString()), descricao.getText().toString());
		try {
			ctrlCaixa.editaCaixa(caixa);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(){
		try {
			if (ctrlCaixa.validaCodigo(caixa)){
				Toast.makeText(this, "Caixa já cadastrado \nFavor inserir um código válido", Toast.LENGTH_SHORT).show();
				codigo.setText("");
				codigo.requestFocus();
				return false;
			}else{
				gravarCaixa();
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
