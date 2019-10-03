package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlCaixa;
import com.packagecontroller.selfcontrol.CtrlMovCaixa;
import com.packagecontroller.selfcontrol.CtrlTipoMovCaixa;
import com.packagemodel.selfcontrol.Caixa;
import com.packagemodel.selfcontrol.Cheque;
import com.packagemodel.selfcontrol.MovimentoCaixa;
import com.packagemodel.selfcontrol.MovimentoCheque;
import com.packagemodel.selfcontrol.TipoMovimentoCaixa;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

import android.app.Activity;
import android.content.Intent;
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

public class MovCaixaReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Button btnCalendario;
	private Caixa caixa;
	private MovimentoCaixa movimentoCaixa;
	private CtrlMovCaixa ctrlMovCaixa;
	private CtrlCaixa ctrlCaixa;
	private boolean novo;
	private TextView codigo, documento, data, valor;
	private ArrayList<Caixa> listaCaixa;
	private ArrayList<TipoMovimentoCaixa> listaTipoMov;
	private Spinner comboCaixa, comboMov;
	private CtrlTipoMovCaixa ctrlTipoMovCaixa;
	private TituloPagar tituloPagar;
	private TituloReceber tituloReceber;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcaixareg);
		
		tituloPagar = new TituloPagar();
		tituloReceber = new TituloReceber();
		caixa = new Caixa();
		movimentoCaixa= new MovimentoCaixa();
		
		ctrlMovCaixa = new CtrlMovCaixa(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.editText3);
		codigo.setEnabled(false);
		
		//RECEBE O SPINNER DO CAIXA
		ctrlCaixa = new CtrlCaixa(this.getApplicationContext());
		listaCaixa = new ArrayList<Caixa>();
		loadCaixa();
		comboCaixa = (Spinner) findViewById(R.id.spinner1);
				
		ArrayAdapter<Caixa> adapterCaixa = new ArrayAdapter<Caixa>(this, android.R.layout.simple_spinner_item, listaCaixa);  
		comboCaixa.setAdapter(adapterCaixa);
		
		//RECEBE O SPINNER TIPO DE MOVIMENTAÇÃO
		ctrlTipoMovCaixa = new CtrlTipoMovCaixa(this.getApplicationContext());
		listaTipoMov = new ArrayList<TipoMovimentoCaixa>();
		loadTipoMov();
		comboMov = (Spinner) findViewById(R.id.spinner2);
				
		ArrayAdapter<TipoMovimentoCaixa> adapterTipoMov = new ArrayAdapter<TipoMovimentoCaixa>(this, android.R.layout.simple_spinner_item, listaTipoMov);  
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
				
		movimentoCaixa = (MovimentoCaixa) getIntent().getSerializableExtra("movCaixa");
		
		try {
			codigo.setText(String.valueOf(ctrlMovCaixa.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(movimentoCaixa instanceof MovimentoCaixa){
			novo = false;
			codigo.setText(String.valueOf(movimentoCaixa.getCodigo()));
			int i = 0, j = 0;
			for (Caixa caixa: listaCaixa){
				if (caixa.getCodigo() == movimentoCaixa.getCaixa().getCodigo()){
					comboCaixa.setSelection(i);
					break;
				}
				i++;
			}
			for (TipoMovimentoCaixa tipoMovimentoCaixa: listaTipoMov){
				if (tipoMovimentoCaixa.getCodigo() == movimentoCaixa.getTipoMovimentoCaixa().getCodigo()){
					comboMov.setSelection(j);
					break;
				}
				j++;
			}
			documento.setText(String.valueOf(movimentoCaixa.getDocumento()));
			data.setText(String.valueOf(movimentoCaixa.getData()));
			if (movimentoCaixa.getValor() < 0){
				valor.setText(String.valueOf(movimentoCaixa.getValor() * -1));	
			}
			else{
				valor.setText(String.valueOf(movimentoCaixa.getValor()));
			}
		}
		
		tituloPagar = (TituloPagar) getIntent().getSerializableExtra("movCaixaTitPag");
		
		if(tituloPagar instanceof TituloPagar){
			try {
				codigo.setText(String.valueOf(ctrlMovCaixa.buscaCodigo()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			comboMov.setSelection(0);
			comboMov.setEnabled(false);
			documento.setText(String.valueOf(tituloPagar.getDocumento()));
			valor.setText(String.valueOf(tituloPagar.getSaldo() + tituloPagar.getAcrescimo() - tituloPagar.getDesconto()));
		}
		
		tituloReceber = (TituloReceber) getIntent().getSerializableExtra("movCaixaTitRec");
		
		if(tituloReceber instanceof TituloReceber){
			try {
				codigo.setText(String.valueOf(ctrlMovCaixa.buscaCodigo()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			comboMov.setSelection(1);
			comboMov.setEnabled(false);
			documento.setText(String.valueOf(tituloReceber.getDocumento()));
			valor.setText(String.valueOf(tituloReceber.getSaldo() + tituloReceber.getAcrescimo() - tituloReceber.getDesconto()));
		}
		
		
		//SALVA UMA NOVA MOVIMENTACAO      
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(String.valueOf(documento.getText()).equals("") || String.valueOf(data.getText()).equals("") || String.valueOf(valor.getText()).equals("") ||listaCaixa.size() == 0){
						validaCadastro();
					}
					else{
						if (novo){
							movimentoCaixa = new MovimentoCaixa(Integer.parseInt(codigo.getText().toString()), listaCaixa.get(comboCaixa.getSelectedItemPosition()), listaTipoMov.get(comboMov.getSelectedItemPosition()), data.getText().toString(), documento.getText().toString(), Float.parseFloat(valor.getText().toString()));
							if (validaTela()){
								setResult(2,null);
								finish();							
								}
							}else{
								editarMovCaixa();
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

		comboCaixa.setOnItemSelectedListener(new OnItemSelectedListener() {
			

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Alisson","clicado na posicao = " + position);
				comboCaixa.setSelection(position);
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
					i.setClass(getApplicationContext(), MovCaixaTitPag.class);
					finish();
					startActivityForResult(i, 10);
				}
				if (String.valueOf(listaTipoMov.get(comboMov.getSelectedItemPosition()).getTipo()).equals("RT")){
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovCaixaTitReceb.class);
					finish();
					startActivityForResult(i, 10);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
		


		public List<Caixa> loadCaixa(){
			try {
				return listaCaixa = (ArrayList<Caixa>) ctrlCaixa.buscaCaixa();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public List<TipoMovimentoCaixa> loadTipoMov(){
			try {
				return listaTipoMov = (ArrayList<TipoMovimentoCaixa>) ctrlTipoMovCaixa.buscar();
			} catch (Exception e) {
				//TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		public void gravarMovCaixa(){
			movimentoCaixa = new MovimentoCaixa(Integer.parseInt(codigo.getText().toString()), listaCaixa.get(comboCaixa.getSelectedItemPosition()), listaTipoMov.get(comboMov.getSelectedItemPosition()), data.getText().toString(), documento.getText().toString(), Float.parseFloat(valor.getText().toString()));
			try {
				
				ctrlMovCaixa.gravarMovCaixa(movimentoCaixa);
				Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tituloPagar instanceof TituloPagar){
				try {
					ctrlMovCaixa.baixaTituloPagar(tituloPagar, movimentoCaixa);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(tituloReceber instanceof TituloReceber){
				try {
					ctrlMovCaixa.baixaTituloReceber(tituloReceber, movimentoCaixa);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void editarMovCaixa(){
			movimentoCaixa = new MovimentoCaixa(Integer.parseInt(codigo.getText().toString()), listaCaixa.get(comboCaixa.getSelectedItemPosition()), listaTipoMov.get(comboMov.getSelectedItemPosition()), data.getText().toString(), documento.getText().toString(), Float.parseFloat(valor.getText().toString()));
			try {
				ctrlMovCaixa.editaMovCaixa(movimentoCaixa);
				Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean validaTela(){
			try {
				if (ctrlMovCaixa.validaCodigo(movimentoCaixa)){
					Toast.makeText(this, "Movimentação já cadastrado \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
					documento.setText("");
					return false;
				}else{
					gravarMovCaixa();
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
