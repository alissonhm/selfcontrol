package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterCartDeb;
import com.packageComponents.selfcontrol.MinhaClasseAdapterCheque;
import com.packagecontroller.selfcontrol.CtrlCartDeb;
import com.packagemodel.selfcontrol.CartDeb;
import com.packagemodel.selfcontrol.Cheque;
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

public class CadCartDeb extends Activity{
	private Button btnCadCartDeb;
	private CtrlCartDeb ctrlCartDeb;
	private ArrayList<CartDeb> listaCartDeb;
	private ListView minhaListView;
	private MinhaClasseAdapterCartDeb mca;
	private int index;
	private CartDeb cartDeb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cadcartdeb);
		
		ctrlCartDeb = new CtrlCartDeb(this.getApplicationContext());
		listaCartDeb = new ArrayList<CartDeb>();
		loadCartDeb();
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterCartDeb(this.getApplicationContext(), listaCartDeb);
		
		minhaListView.setAdapter(mca);
		
		//ACESSA A TELA DE CADASTRO DE CARTOES DE DEBITO       
		btnCadCartDeb = (Button) findViewById(R.id.button1);		
		btnCadCartDeb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), CadCartDebReg.class);
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
				loadCartDeb();
				refreshListView();
			}
		}
	}

	//CARREGA A LISTA DE CARTAO DO BANCO DE DADOS
	public List<CartDeb> loadCartDeb(){
		try {
			return listaCartDeb = (ArrayList<CartDeb>) ctrlCartDeb.buscaCartDeb();
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterCartDeb(this.getApplicationContext(), listaCartDeb);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}

	//DELETA O CARTAO
	public boolean deletarCartao(){
		try {
			return ctrlCartDeb.deletarCartDeb(listaCartDeb.get(index));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	//DIALOG DE OPÇAO DE EDIÇÃO E DELEÇÃO DO CHEQUE
	public void alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Ações")
		.setMessage("Escolha uma opção")
		.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
		

			@Override
			public void onClick(DialogInterface dialog, int which) {
				cartDeb = listaCartDeb.get(index);
				Intent intent = new Intent(getApplicationContext(), CadCartDebReg.class);
				intent.putExtra("cartDeb", cartDeb);
				startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarCartao()){
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
		listaCartDeb.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}
}
