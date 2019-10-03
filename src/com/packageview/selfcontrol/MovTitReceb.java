package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterTitPag;
import com.packageComponents.selfcontrol.MinhaClasseAdapterTitRec;
import com.packagecontroller.selfcontrol.CtrlTitRec;
import com.packagemodel.selfcontrol.TituloPagar;
import com.packagemodel.selfcontrol.TituloReceber;

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

public class MovTitReceb extends Activity{
	private Button btnNovo;
	private CtrlTitRec ctrlTitRec;
	private ArrayList<TituloReceber> listaTitRec;
	private ListView minhaListView;
	private MinhaClasseAdapterTitRec mca;
	private int index;
	private TituloReceber tituloReceber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tituloreceb);
		
		ctrlTitRec = new CtrlTitRec(this.getApplicationContext());
		listaTitRec = new ArrayList<TituloReceber>();
		loadTitRec();
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterTitRec(this.getApplicationContext(), listaTitRec);
		
		minhaListView.setAdapter(mca);
		
		//CADASTRA UMA NOVO TÍTULO A RECEBER       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MovTitRecebReg.class);
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
				loadTitRec();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE BANCOS DO BANCO DE DADOS
	public List<TituloReceber> loadTitRec(){
		try {
			return listaTitRec = (ArrayList<TituloReceber>) ctrlTitRec.buscaTitRec();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterTitRec(this.getApplicationContext(), listaTitRec);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA O TITULO
	public boolean deletarTitRec(){
		try {
			return ctrlTitRec.deletarTitRec(listaTitRec.get(index));
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
				tituloReceber = listaTitRec.get(index);
				Intent intent = new Intent(getApplicationContext(), MovTitRecebReg.class);
				intent.putExtra("tituloReceber", tituloReceber);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarTitRec()){
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
		listaTitRec.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}


	
}

