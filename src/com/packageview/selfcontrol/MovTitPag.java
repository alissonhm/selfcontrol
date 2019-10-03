package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterBanco;
import com.packageComponents.selfcontrol.MinhaClasseAdapterTitPag;
import com.packagecontroller.selfcontrol.CtrlTitPag;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.TituloPagar;

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



public class MovTitPag extends Activity{
	private Button btnNovo;
	private ArrayList<TituloPagar> listaTitPag;
	private MinhaClasseAdapterTitPag mca;
	private ListView minhaListView;
	private int index;
	private TituloPagar tituloPagar;
	private CtrlTitPag ctrlTitPag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_titulopagar);
		
		ctrlTitPag = new CtrlTitPag(this.getApplicationContext());
		listaTitPag = new ArrayList<TituloPagar>();
		loadTitPag();
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterTitPag(this.getApplicationContext(), listaTitPag);
		
		minhaListView.setAdapter(mca);

		//CADASTRA UM NOVO TÍTULO A PAGAR       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MovTitPagReg.class);
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
				loadTitPag();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE BANCOS DO BANCO DE DADOS
	public List<TituloPagar> loadTitPag(){
		try {
			return listaTitPag = (ArrayList<TituloPagar>) ctrlTitPag.buscaTitPag();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterTitPag(this.getApplicationContext(), listaTitPag);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA O BANCO
	public boolean deletarTitPag(){
		try {
			return ctrlTitPag.deletarTitPag(listaTitPag.get(index));
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
				tituloPagar = listaTitPag.get(index);
				Intent intent = new Intent(getApplicationContext(), MovTitPagReg.class);
				intent.putExtra("tituloPagar", tituloPagar);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarTitPag()){
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
		listaTitPag.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}


	
}