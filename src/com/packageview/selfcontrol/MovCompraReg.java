package com.packageview.selfcontrol;

import java.util.ArrayList;
import java.util.List;

import com.example.selfcontrol.R;
import com.packagecontroller.selfcontrol.CtrlBanco;
import com.packagecontroller.selfcontrol.CtrlCompra;
import com.packagecontroller.selfcontrol.CtrlConta;
import com.packagecontroller.selfcontrol.CtrlPagto;
import com.packagemodel.selfcontrol.Banco;
import com.packagemodel.selfcontrol.Cheque;
import com.packagemodel.selfcontrol.Compra;
import com.packagemodel.selfcontrol.Conta;
import com.packagemodel.selfcontrol.Pagamento;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MovCompraReg extends Activity{
	private Button btnSalvar;
	private Button btnCanc;
	private Button btnCalendario;
	private Compra compra;
	private Pagamento pagamento;
	private TextView codigo, fornecedor, produto, data, valor, qtdParcela, dtParcela;
	private CtrlPagto ctrlPagto;
	private ArrayList<Pagamento> listaPagto;
	private Spinner combo;
	private boolean novo;
	private CtrlCompra ctrlCompra;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movcomprareg);
		
		pagamento = new Pagamento();
		compra = new Compra();
		
		ctrlCompra = new CtrlCompra(this.getApplicationContext());
		novo = true;
		
		//RECEBE O CODIGO
		codigo = (TextView) findViewById(R.id.EditText01);
		codigo.setEnabled(false);
		
		// RECEBE O FORNECEDOR
		fornecedor = (TextView) findViewById(R.id.editText4);
		fornecedor.requestFocus();
		
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
		
		compra = (Compra) getIntent().getSerializableExtra("compra");
		
		try {
			codigo.setText(String.valueOf(ctrlCompra.buscaCodigo()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(compra instanceof Compra){
			novo = false;
			codigo.setText(String.valueOf(compra.getCodigo()));
			fornecedor.setText(compra.getFornecedor());
			produto.setText(compra.getProduto());
			data.setText(compra.getData());
			valor.setText(String.valueOf(compra.getValor()));
			int i = 0;
			for (Pagamento pagamento: listaPagto){
				if (pagamento.getCodigo() == compra.getPagamento().getCodigo()){
					combo.setSelection(i);
					break;
				}
				i++;
			}			
			qtdParcela.setText(String.valueOf(compra.getQtdParcela()));
			dtParcela.setText(String.valueOf(compra.getDtParcela()));
			valor.setEnabled(false);
			combo.setEnabled(false);
			qtdParcela.setEnabled(false);
			dtParcela.setEnabled(false);
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
			
	
		
		//SALVA UMA NOVA COMPRA
		btnSalvar = (Button) findViewById(R.id.button1);		
		btnSalvar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listaPagto.size() == 0 || String.valueOf(fornecedor.getText()).equals("") || String.valueOf(produto.getText()).equals("") || String.valueOf(data.getText()).equals("") || String.valueOf(valor.getText()).equals("") || String.valueOf(dtParcela.getText()).equals("") || String.valueOf(qtdParcela.getText()).equals("")){
						validaDados();
					}
					else if (Integer.parseInt(String.valueOf(dtParcela.getText())) > 31)
						dataInvalida();
					else{
						if (novo){
							compra = new Compra(Integer.parseInt(codigo.getText().toString()), fornecedor.getText().toString(), produto.getText().toString(), data.getText().toString(), Float.parseFloat(valor.getText().toString()), listaPagto.get(combo.getSelectedItemPosition()), Integer.parseInt(qtdParcela.getText().toString()), Integer.parseInt(dtParcela.getText().toString()));
							if (validaTela()){
								setResult(2,null);
								finish();							
							}
						}
						else{
							editarCompra();
							setResult(2,null);
							finish();
						}
					}
			}
	});
		
		//CANCELA UMA NOVA COMPRA      
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
			return listaPagto = (ArrayList<Pagamento>) ctrlPagto.buscaPagamentosPagar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void gravarCompra(){
		compra = new Compra(Integer.parseInt(codigo.getText().toString()), fornecedor.getText().toString(), produto.getText().toString(), data.getText().toString(), Float.parseFloat(valor.getText().toString()),listaPagto.get(combo.getSelectedItemPosition()), Integer.parseInt(qtdParcela.getText().toString()), Integer.parseInt(dtParcela.getText().toString()));
		try {
			
			ctrlCompra.gravarCompra(compra);
			Toast.makeText(this, "Registro inserido com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editarCompra(){
		compra = new Compra(Integer.parseInt(codigo.getText().toString()), fornecedor.getText().toString(), produto.getText().toString(), data.getText().toString(), Float.parseFloat(valor.getText().toString()),listaPagto.get(combo.getSelectedItemPosition()), Integer.parseInt(qtdParcela.getText().toString()), Integer.parseInt(dtParcela.getText().toString()));
		try {
			ctrlCompra.editaCompra(compra);
			Toast.makeText(this, "Registro atualizado com sucesso", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validaTela(){
		try {
			if (ctrlCompra.validaDados(compra)){
				Toast.makeText(this, "Compra já cadastrada \nFavor inserir dados válidos", Toast.LENGTH_SHORT).show();
				fornecedor.setText("");
				produto.setText("");
				data.setText("");
				valor.setText("");
				qtdParcela.setText("");
				dtParcela.setText("");
				fornecedor.requestFocus();
				return false;
			}if (listaPagto.get(combo.getSelectedItemPosition()).getTipo().equals("CH")){
				if (ctrlCompra.verificaFolhasCheque(compra.getPagamento())){
					gravarCompra();
					return true;
				}
				else{
					Toast.makeText(this, "Esse talão não possui mais folhas disponíveis", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else{
				gravarCompra();
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