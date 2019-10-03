package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packageComponents.selfcontrol.MinhaClasseAdapterVenda;
import com.packagecontroller.selfcontrol.CtrlVenda;
import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.Venda;

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



public class MovVenda extends Activity{
	private Button btnNovo;
	private CtrlVenda ctrlVenda;
	private ArrayList<Venda> listaVenda;
	private ListView minhaListView;
	private MinhaClasseAdapterVenda mca;
	private int index;
	private Venda venda;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movvenda);
		
		ctrlVenda = new CtrlVenda(this.getApplicationContext());
		listaVenda = new ArrayList<Venda>();
		loadVenda();
		
		minhaListView = (ListView) findViewById(R.id.listView1);
		mca = new MinhaClasseAdapterVenda(this.getApplicationContext(), listaVenda);
		
		minhaListView.setAdapter(mca);
		
		//CADASTRA UMA NOVA VENDA       
		btnNovo = (Button) findViewById(R.id.button1);		
		btnNovo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MovVendaReg.class);
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
				loadVenda();
				refreshListView();
			}
		}
	}
	
	//CARREGA A LISTA DE VENDAS DO BANCO DE DADOS
	public List<Venda> loadVenda(){
		try {
			return listaVenda = (ArrayList<Venda>) ctrlVenda.buscaVenda();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	//ATUALIZA A LISTA EM TELA
	public void refreshListView(){
		mca = new MinhaClasseAdapterVenda(this.getApplicationContext(), listaVenda);
		minhaListView.setAdapter(mca);
		minhaListView.invalidateViews();
	}
	
	//DELETA A VENDA
	public boolean deletarVenda(){
		try {
			return ctrlVenda.deletarVenda(listaVenda.get(index));
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
				venda = listaVenda.get(index);
				Intent intent = new Intent(getApplicationContext(), MovVendaReg.class);
				intent.putExtra("venda", venda);
                startActivityForResult(intent, 10);
			}
		})
		.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					if (deletarVenda()){
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
		listaVenda.remove(index);
		refreshListView();
		index = -1;
		Toast.makeText(this, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
	}
	
	public void erroExclusao(){
		Toast.makeText(this, "O registro já foi utilizado", Toast.LENGTH_SHORT).show();
	}
}
