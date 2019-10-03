package com.packageview.selfcontrol;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterMovConta;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagecontroller.selfcontrol.CtrlMovConta;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.MovimentoConta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MovConta extends Activity{
	private Button btnNovo;
	private Spinner combo;
	private CtrlConta ctrlConta;
	private CtrlMovConta ctrlMovConta;
	private ArrayList<Conta> listaConta;
	private MovimentoConta movConta;
	private TextView saldo;
	private ArrayList<MovimentoConta> listaMovConta;
	private ListView minhaListView;
	private MinhaClasseAdapterMovConta mca;
	private int index;
	private float saldoFloat;
	private String saldoString;
	private Conta conta;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movconta);
		
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		//RECEBE O SPINNER
		ctrlConta = new CtrlConta(this.getApplicationContext());
		listaConta = new ArrayList<Conta>();
		loadConta();
		combo = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<Conta> adapterConta = new ArrayAdapter<Conta>(this, android.R.layout.simple_spinner_item, listaConta);  
		combo.setAdapter(adapterConta);
		
		//RECEBE O SALDO
		saldo = (TextView) findViewById(R.id.editText1);
		try {
			ctrlMovConta = new CtrlMovConta(this.getApplicationContext());
			conta = new Conta();
			conta = listaConta.get(combo.getSelectedItemPosition());
			saldoFloat = ctrlMovConta.calculaSaldo(conta);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("0.00");
		saldoString = df.format(saldoFloat);
		saldo.setText("R$ " + String.valueOf(saldoString));
		saldo.setEnabled(false);
		
		
		listaMovConta = new ArrayList<MovimentoConta>();
		loadMovConta();
		
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterMovConta(this.getApplicationContext(), listaMovConta);
		
		minhaListView.setAdapter(mca);
		
		//CADASTRA UMA NOVO MOVIMENTACAO DE CONTA       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovContaReg.class);
					startActivityForResult(i, 10);
				}
		});
		
		minhaListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				index = position;				
				alertDialog();
				
				return false;
			}
		});
		
		//ATUALIZA O SALDO E A LISTA CONFORME O CAIXA SELECIONADO
		combo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				refreshSaldo();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
	}
	
	
	
	//RECARREGA A LISTA EM TELA
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 10){
				loadMovConta();
				refreshListView();
				refreshSaldo();
		}
	}
	
	//CARREGA O SPINNER DO CAIXA
	public List<Conta> loadConta(){
		try {
			return listaConta = (ArrayList<Conta>) ctrlConta.buscaConta();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//CARREGA A LISTA DE MOVIMENTAÇÕES DO BANCO DE DADOS
		public List<MovimentoConta> loadMovConta(){
			try {
				return listaMovConta = (ArrayList<MovimentoConta>) ctrlMovConta.buscaMovConta(listaConta.get(combo.getSelectedItemPosition()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		//ATUALIZA A LISTA EM TELA
		public void refreshListView(){
			mca = new MinhaClasseAdapterMovConta(this.getApplicationContext(), listaMovConta);
			minhaListView.setAdapter(mca);
			minhaListView.invalidateViews();
		}
		
		//ATUALIZA O SALDO DO CAIXA SELECIONADO
		public void refreshSaldo(){
			listaMovConta = new ArrayList<MovimentoConta>();
			loadMovConta();
			
			
			minhaListView = (ListView) findViewById(R.id.listView1);
			mca = new MinhaClasseAdapterMovConta(this.getApplicationContext(), listaMovConta);
			
			minhaListView.setAdapter(mca);
			try {
				conta = new Conta();
				conta = listaConta.get(combo.getSelectedItemPosition());
				saldoFloat = ctrlMovConta.calculaSaldo(conta);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DecimalFormat df = new DecimalFormat("0.00");
			saldoString = df.format(saldoFloat);
			saldo.setText("R$ " + String.valueOf(saldoString));
		}
		
		//DELETA A MOVIMENTAÇÃO
		public boolean deletarMovConta(){
			try {
				return ctrlMovConta.deletarMovConta(listaMovConta.get(index));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return false;
		}
		
		//DIALOG DE OPÇAO DE EDIÇÃO E DELEÇÃO DO BANCO
		public void alertDialog(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Ações")
			.setMessage("Escolha uma opção")
			.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					movConta = listaMovConta.get(index);
					Intent intent = new Intent(getApplicationContext(), MovContaReg.class);
					intent.putExtra("movConta", movConta);
	                startActivityForResult(intent, 10);
				}
			})
			.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						if (deletarMovConta()){
							atualizaListaRem();
							refreshSaldo();
						}else{
							erroExclusao();
							}
						}
					catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}).create().show();
		}
		
		//ATUALIZA A LISTA APÓS REMOVER O REGISTRO
		public void atualizaListaRem(){
			listaMovConta.remove(index);
			refreshListView();
			index = -1;
			Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
		}
		
		public void erroExclusao(){
			Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
		}
		
		
	}
