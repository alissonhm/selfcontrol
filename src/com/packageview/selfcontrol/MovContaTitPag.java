package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterTitPag;
import com.packagecontroller.selfcontrol.CtrlTitPag;
import com.packagemodel.selfcontrol.TituloPagar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MovContaTitPag extends Activity{
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

		btnNovo = (Button) findViewById(R.id.button1);		
        btnNovo.setVisibility(View.INVISIBLE);
		
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
	
		
	//DIALOG DE OPÇAO DE BAIXA
	public void alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Ações")
		.setMessage("Deseja baixar o título?")
		.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tituloPagar = listaTitPag.get(index);
				Intent intent = new Intent(getApplicationContext(), MovContaReg.class);
				intent.putExtra("movContaTitPag", tituloPagar);
				finish();
                startActivity(intent);
			}
		})
		.setPositiveButton("Não", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).create().show();
	}
}