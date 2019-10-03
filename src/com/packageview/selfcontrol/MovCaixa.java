package com.packageview.selfcontrol;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterMovCaixa;
import com.packageComponents.selfcontrol.MinhaClasseAdapterMovCheque;
import com.packagecontroller.selfcontrol.CtrlCaixa;
import com.packagecontroller.selfcontrol.CtrlMovCaixa;
import com.packagecontroller.selfcontrol.CtrlMovCheque;
import com.packagecontroller.selfcontrol.CtrlPagto;
import com.packagemodel.selfcontrol.Caixa;
import com.packagemodel.selfcontrol.MovimentoCaixa;
import com.packagemodel.selfcontrol.MovimentoCheque;
import com.packagemodel.selfcontrol.Pagamento;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MovCaixa extends Activity{
	private Button btnNovo;
	private Spinner combo;
	private CtrlCaixa ctrlCaixa;
	private CtrlMovCaixa ctrlMovCaixa;
	private ArrayList<Caixa> listaCaixa;
	private MovimentoCaixa movCaixa;
	private TextView saldo;
	private ArrayList<MovimentoCaixa> listaMovCaixa;
	private ListView minhaListView;
	private MinhaClasseAdapterMovCaixa mca;
	private int index;
	private float saldoFloat;
	private String saldoString;
	private Caixa caixa;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcaixa);
		
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		//RECEBE O SPINNER
		ctrlCaixa = new CtrlCaixa(this.getApplicationContext());
		listaCaixa = new ArrayList<Caixa>();
		loadCaixa();
		combo = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<Caixa> adapterCaixa = new ArrayAdapter<Caixa>(this, android.R.layout.simple_spinner_item, listaCaixa);  
		combo.setAdapter(adapterCaixa);
		
		//RECEBE O SALDO
		saldo = (TextView) findViewById(R.id.editText1);
		try {
			ctrlMovCaixa = new CtrlMovCaixa(this.getApplicationContext());
			caixa = new Caixa();
			caixa = listaCaixa.get(combo.getSelectedItemPosition());
			saldoFloat = ctrlMovCaixa.calculaSaldo(caixa);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("0.00");
		saldoString = df.format(saldoFloat);
		saldo.setText("R$ " + String.valueOf(saldoString));
		saldo.setEnabled(false);
		
		
		listaMovCaixa = new ArrayList<MovimentoCaixa>();
		loadMovCaixa();
		
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterMovCaixa(this.getApplicationContext(), listaMovCaixa);
		
		minhaListView.setAdapter(mca);
		
		//CADASTRA UMA NOVO MOVIMENTACAO DE CAIXA       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovCaixaReg.class);
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
				loadMovCaixa();
				refreshListView();
				refreshSaldo();
		}
	}
	
	//CARREGA O SPINNER DO CAIXA
	public List<Caixa> loadCaixa(){
		try {
			return listaCaixa = (ArrayList<Caixa>) ctrlCaixa.buscaCaixa();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//CARREGA A LISTA DE MOVIMENTAÇÕES DO BANCO DE DADOS
		public List<MovimentoCaixa> loadMovCaixa(){
			try {
				return listaMovCaixa = (ArrayList<MovimentoCaixa>) ctrlMovCaixa.buscaMovCaixa(listaCaixa.get(combo.getSelectedItemPosition()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		//ATUALIZA A LISTA EM TELA
		public void refreshListView(){
			mca = new MinhaClasseAdapterMovCaixa(this.getApplicationContext(), listaMovCaixa);
			minhaListView.setAdapter(mca);
			minhaListView.invalidateViews();
		}
		
		//ATUALIZA O SALDO DO CAIXA SELECIONADO
		public void refreshSaldo(){
			listaMovCaixa = new ArrayList<MovimentoCaixa>();
			loadMovCaixa();
			
			
			minhaListView = (ListView) findViewById(R.id.listView1);
			mca = new MinhaClasseAdapterMovCaixa(this.getApplicationContext(), listaMovCaixa);
			
			minhaListView.setAdapter(mca);
			try {
				caixa = new Caixa();
				caixa = listaCaixa.get(combo.getSelectedItemPosition());
				saldoFloat = ctrlMovCaixa.calculaSaldo(caixa);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DecimalFormat df = new DecimalFormat("0.00");
			saldoString = df.format(saldoFloat);
			saldo.setText("R$ " + String.valueOf(saldoString));
		}
		
		//DELETA A MOVIMENTAÇÃO
		public boolean deletarMovCaixa(){
			try {
				return ctrlMovCaixa.deletarMovCaixa(listaMovCaixa.get(index));
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
					movCaixa = listaMovCaixa.get(index);
					Intent intent = new Intent(getApplicationContext(), MovCaixaReg.class);
					intent.putExtra("movCaixa", movCaixa);
	                startActivityForResult(intent, 10);
				}
			})
			.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						if (deletarMovCaixa()){
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
			listaMovCaixa.remove(index);
			refreshListView();
			index = -1;
			Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
		}
		
		public void erroExclusao(){
			Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
		}
		
		
	}
