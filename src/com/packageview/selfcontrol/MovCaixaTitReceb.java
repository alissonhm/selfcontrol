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

public class MovCaixaTitReceb extends Activity{
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
	
	//DIALOG DE OPÇAO DE BAIXA
		public void alertDialog(){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Ações")
			.setMessage("Deseja baixar o título?")
			.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tituloReceber = listaTitRec.get(index);
					Intent intent = new Intent(getApplicationContext(), MovCaixaReg.class);
					intent.putExtra("movCaixaTitRec", tituloReceber);
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