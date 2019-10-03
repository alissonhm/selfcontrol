package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlPagto;
import com.packagecontroller.selfcontrol.CtrlVenda;
import com.packagemodel.selfcontrol.Pagamento;
import com.packagemodel.selfcontrol.Venda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class MovVendaReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Button btnCalendario;
	private Venda venda;
	private Pagamento pagamento;
	private TextView codigo, cliente, produto, data, valor, qtdParcela, dtParcela;
	private CtrlPagto ctrlPagto;
	private ArrayList<Pagamento> listaPagto;
	private Spinner combo;
	private boolean novo;
	private CtrlVenda ctrlVenda;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movvendareg);
		
		pagamento = new Pagamento();
		venda = new Venda();
		
		ctrlVenda = new CtrlVenda(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText01);
		codigo.setEnabled(false);
		
		// RECEBE O CLIENTE
		cliente = (TextView) findViewById(R.id.editText4);
		cliente.requestFocus();
		
		// RECEBE O PRODUTO
		produto = (TextView) findViewById(R.id.editText3);
		
		// RECEBE A DATA
		data = (TextView) findViewById(R.id.editText2);
		data.setEnabled(false);
		
		//RECEBE O BOTÃO CALENDÁRIO
		btnCalendario = (Button) findViewById(R.id.button2);		
		btnCalendario.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), Calendario.class);
					startActivityForResult(i, 10);
				}
		});
		
		// RECEBE O VALOR
		valor = (TextView) findViewById(R.id.editText1);
		
		//RECEBE O SPINNER
		ctrlPagto = new CtrlPagto(this.getApplicationContext());
		listaPagto = new ArrayList<Pagamento>();
		loadPagto();
		combo = (Spinner) findViewById(R.id.spinner1);
		
		ArrayAdapter<Pagamento> adapterPagamento = new ArrayAdapter<Pagamento>(this, android.R.layout.simple_spinner_item, listaPagto);  
		combo.setAdapter(adapterPagamento);		
		
		// RECEBE A QUANTIDADE DE PARCELAS
		qtdParcela = (TextView) findViewById(R.id.editText5);
		
		// RECEBE A DATA DE PARCELAS
		dtParcela = (TextView) findViewById(R.id.EditText02);	
		
		venda = (Venda) getIntent().getSerializableExtra("venda");
		
		try {
			codigo.setText(String.valueOf(ctrlVenda.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(venda instanceof Venda){
			novo = false;
			codigo.setText(String.valueOf(venda.getCodigo()));
			cliente.setText(venda.getCliente());
			produto.setText(venda.getProduto());
			data.setText(venda.getData());
			valor.setText(String.valueOf(venda.getValor()));
			int i = 0;
			for (Pagamento pagamento: listaPagto){
				if (pagamento.getCodigo() == venda.getPagamento().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}			
			qtdParcela.setText(String.valueOf(venda.getQtdParcela()));
			dtParcela.setText(String.valueOf(venda.getDtParcela()));
		}
		else{
			combo.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if (String.valueOf(listaPagto.get(combo.getSelectedItemPosition()).getTipo()).equals("CX")){
						qtdParcela.setText("0");
						dtParcela.setText("0");
						qtdParcela.setEnabled(false);
						dtParcela.setEnabled(false);
					}
					if (String.valueOf(listaPagto.get(combo.getSelectedItemPosition()).getTipo()).equals("CD")){
						qtdParcela.setText("0");
						dtParcela.setText("0");
						qtdParcela.setEnabled(false);
						dtParcela.setEnabled(false);
					}
					if (String.valueOf(listaPagto.get(combo.getSelectedItemPosition()).getTipo()).equals("CH")){
						qtdParcela.setText("0");
						dtParcela.setText("0");
						qtdParcela.setEnabled(false);
						dtParcela.setEnabled(false);
					}
					if (String.valueOf(listaPagto.get(combo.getSelectedItemPosition()).getTipo()).equals("CC")){
						dtParcela.setText("0");
						qtdParcela.setEnabled(true);
						dtParcela.setEnabled(false);
					}
					if (String.valueOf(listaPagto.get(combo.getSelectedItemPosition()).getTipo()).equals("AP")){
						qtdParcela.setEnabled(true);
						dtParcela.setEnabled(true);
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
			});
		}
		
		
		//SALVA UMA NOVA VENDA
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listaPagto.size() == 0 || String.valueOf(cliente.getText()).equals("") || String.valueOf(produto.getText()).equals("") || String.valueOf(data.getText()).equals("") || String.valueOf(valor.getText()).equals("")){
						validaDados();
					}
					else if (Integer.parseInt(String.valueOf(dtParcela.getText())) > 31)
						dataInvalida();
					else{
						if (novo){
							venda = new Venda(Integer.parseInt(codigo.getText().toString()), cliente.getText().toString(), produto.getText().toString(), data.getText().toString(), Float.parseFloat(valor.getText().toString()), listaPagto.get(combo.getSelectedItemPosition()), Integer.parseInt(qtdParcela.getText().toString()), Integer.parseInt(dtParcela.getText().toString()));
							if (validaTela()){
								setResult(2,null);
								finish();							
							}
						}
						else{
							editarVenda();
							setResult(2,null);
							finish();
						}
					}
			}
	});
		
		//CANCELA UMA NOVA VENDA      
		btnCanc = (Button) findViewById(R.id.button3);		
		btnCanc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
		});
	}

	public List<Pagamento> loadPagto(){
		try {
			return listaPagto = (ArrayList<Pagamento>) ctrlPagto.buscaPagamentosReceber();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void gravarVenda(){
		venda = new Venda(Integer.parseInt(codigo.getText().toString()), cliente.getText().toString(), produto.getText().toString(), data.getText().toString(), Float.parseFloat(valor.getText().toString()),listaPagto.get(combo.getSelectedItemPosition()), Integer.parseInt(qtdParcela.getText().toString()), Integer.parseInt(dtParcela.getText().toString()));
		try {
			
			ctrlVenda.gravarVenda(venda);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarVenda(){
		venda = new Venda(Integer.parseInt(codigo.getText().toString()), cliente.getText().toString(), produto.getText().toString(), data.getText().toString(), Float.parseFloat(valor.getText().toString()),listaPagto.get(combo.getSelectedItemPosition()), Integer.parseInt(qtdParcela.getText().toString()), Integer.parseInt(dtParcela.getText().toString()));
		try {
			ctrlVenda.editaVenda(venda);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(){
		try {
			if (ctrlVenda.validaDados(venda)){
				Toast.makeText(this, "Venda já cadastrada \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
				cliente.setText("");
				produto.setText("");
				data.setText("");
				valor.setText("");
				qtdParcela.setText("");
				dtParcela.setText("");
				cliente.requestFocus();
				return false;
			}else{
				gravarVenda();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public void validaDados(){
		Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
	}
	
	public void dataInvalida(){
		Toast.makeText(this, "Os campos de data devem conter valores entre 1 e 31", Toast.LENGTH_SHORT).show();
		dtParcela.setText("");
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (resultCode == RESULT_OK) {
			Bundle bundle = intent.getExtras();
			data.setText(bundle.getString("data"));
		}
		
	}
	
}