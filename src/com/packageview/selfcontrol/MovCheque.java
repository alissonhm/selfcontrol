package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterMovCheque;
import com.packagecontroller.selfcontrol.CtrlMovCheque;
import com.packagemodel.selfcontrol.MovimentoCheque;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MovCheque extends Activity {
	
	private Button btnNovo;
	private ListView minhaListView;
	private ArrayList<MovimentoCheque> listaMovCheque;
	private CtrlMovCheque ctrlMovCheque;
	private MinhaClasseAdapterMovCheque mca;
	private MovimentoCheque movCheque;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcheque);
				
		ctrlMovCheque = new CtrlMovCheque(this.getApplicationContext());
		listaMovCheque = new ArrayList<MovimentoCheque>();
		loadMovCheque();
		
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterMovCheque(this.getApplicationContext(), listaMovCheque);
		
		minhaListView.setAdapter(mca);
		

	//CADASTRA UMA NOVA MOVIMENTACAO
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovChequeReg.class);
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
				loadMovCheque();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE MOVIMENTAÇÕES DO BANCO DE DADOS
	public List<MovimentoCheque> loadMovCheque(){
		try {
			return listaMovCheque = (ArrayList<MovimentoCheque>) ctrlMovCheque.buscaMovCheque();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterMovCheque(this.getApplicationContext(), listaMovCheque);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA A MOVIMENTAÇÃO
	public boolean deletarMovCheque(){
		try {
			return ctrlMovCheque.deletarMovCheque(listaMovCheque.get(index));
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
				movCheque = listaMovCheque.get(index);
				Intent intent = new Intent(getApplicationContext(), MovChequeReg.class);
				intent.putExtra("movCheque", movCheque);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarMovCheque()){
						atualizaListaRem();
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
		listaMovCheque.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}
}
