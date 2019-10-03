package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterCaixa;
import com.packagecontroller.selfcontrol.CtrlCaixa;
import com.packagemodel.selfcontrol.Caixa;

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


public class CadCaixa extends Activity {
	
	private Button btnNovo;
	private ListView minhaListView;
	private ArrayList<Caixa> listaCaixa;
	private CtrlCaixa ctrlCaixa;
	private MinhaClasseAdapterCaixa mca;
	private Caixa caixa;
	private int index, codigo;
	private String descricao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcaixa);
				
		ctrlCaixa = new CtrlCaixa(this.getApplicationContext());
		listaCaixa = new ArrayList<Caixa>();
		loadCaixa();
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterCaixa(this.getApplicationContext(), listaCaixa);
		
		minhaListView.setAdapter(mca);
		

	//CADASTRA UM NOVO CAIXA       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), CadCaixaReg.class);
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
				loadCaixa();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE CAIXAS DO BANCO DE DADOS
	public List<Caixa> loadCaixa(){
		try {
			return listaCaixa = (ArrayList<Caixa>) ctrlCaixa.buscaCaixa();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterCaixa(this.getApplicationContext(), listaCaixa);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA O CAIXA
	public boolean deletarCaixa(){
		try {
			return ctrlCaixa.deletarCaixa(listaCaixa.get(index));
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
				caixa = listaCaixa.get(index);
				Intent intent = new Intent(getApplicationContext(), CadCaixaReg.class);
				intent.putExtra("caixa", caixa);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarCaixa()){
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
		listaCaixa.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}
}
