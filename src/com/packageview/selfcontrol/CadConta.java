package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterConta;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Conta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class CadConta extends Activity{
	private Button btnNovo;
	private CtrlConta ctrlConta;
	private ArrayList<Conta> listaConta;
	private ListView minhaListView;
	private MinhaClasseAdapterConta mca;
	private int index;
	private Conta conta;
	private Banco banco;
	private String agencia, operacao, nroConta, descricao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadconta);
		
		ctrlConta = new CtrlConta(this.getApplicationContext());
		listaConta = new ArrayList<Conta>();
		loadConta();
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterConta(this.getApplicationContext(), listaConta);
		
		minhaListView.setAdapter(mca);
		
		//CADASTRA UMA NOVA CONTA
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadContaReg.class);
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
	}
	
	//RECARREGA A LISTA EM TELA
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 10){
			if(resultCode == 2){
				loadConta();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE CONTA DO BANCO DE DADOS
	public List<Conta> loadConta(){
		try {
			return listaConta = (ArrayList<Conta>) ctrlConta.buscaConta();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterConta(this.getApplicationContext(), listaConta);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA A CONTA
	public boolean deletarConta(){
		try {
			return ctrlConta.deletarConta(listaConta.get(index));
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
				conta = listaConta.get(index);
				Intent intent = new Intent(getApplicationContext(), CadContaReg.class);
                intent.putExtra("conta", conta);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarConta()){
						atualizaListaRem();
					}else{
						erroExclusao();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}).create().show();
	}
	
	//ATUALIZA A LISTA APÓS REMOVER O REGISTRO
	public void atualizaListaRem(){
		listaConta.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}
}
