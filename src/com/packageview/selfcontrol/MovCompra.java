package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterBanco;
import com.packageComponents.selfcontrol.MinhaClasseAdapterCompra;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagecontroller.selfcontrol.CtrlCompra;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Compra;
import com.packagemodel.selfcontrol.Pagamento;

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

public class MovCompra extends Activity{
	private Button btnNovo;
	private CtrlCompra ctrlCompra;
	private ArrayList<Compra> listaCompra;
	private ListView minhaListView;
	private MinhaClasseAdapterCompra mca;
	private int index;
	private Compra compra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcompra);
		
		ctrlCompra = new CtrlCompra(this.getApplicationContext());
		listaCompra = new ArrayList<Compra>();
		loadCompra();
		
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterCompra(this.getApplicationContext(), listaCompra);
		
		minhaListView.setAdapter(mca);
		
		//CADASTRA UMA NOVA COMPRA       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovCompraReg.class);
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
				loadCompra();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE COMPRAS DO BANCO DE DADOS
	public List<Compra> loadCompra(){
		try {
			return listaCompra = (ArrayList<Compra>) ctrlCompra.buscaCompra();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterCompra(this.getApplicationContext(), listaCompra);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA A COMPRA
	public boolean deletarCompra(){
		try {
			return ctrlCompra.deletarCompra(listaCompra.get(index));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	//DIALOG DE OPÇAO DE EDIÇÃO E DELEÇÃO DO BANCO
	public void alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Ações")
		.setMessage("ATENÇÃO: A EDIÇÃO E/OU EXCLUSÃO NÃO ALTERARÁ TÍTULOS, CAIXAS E CONTAS BANCÁRIAS. CORRIJA ESSES VALORES MANUALMENTE! \n"
				+ "Escolha uma opção.")
		.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				compra = listaCompra.get(index);
				Intent intent = new Intent(getApplicationContext(), MovCompraReg.class);
				intent.putExtra("compra", compra);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarCompra()){
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
		listaCompra.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}
}
