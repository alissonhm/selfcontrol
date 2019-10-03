package com.packageComponents.selfcontrol;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.selfcontrol.R;
import com.packagemodel.selfcontrol.Conta;

public class MinhaClasseAdapterConta extends BaseAdapter implements SpinnerAdapter{
	
	public Context context;
	public ArrayList<Conta> itens;
	
	
	public MinhaClasseAdapterConta(Context context, ArrayList<Conta> itens ) {
		this.context = context; 
		this.itens   = itens;
	}
	
	@Override
	// Esse mÃ©todo Ã© executado para preencher cada linha da listas
	// Ã‰ invocado automaticamente 
	public View getView( int       position
			           , View      convertView
			           , ViewGroup parent ) 
	{
		// Criando um CRIADOR de linhas para poder personalizar as mesmas.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE );
		
		// Criado uma view do tipo da linha feita no XML
		View linhaAdaptada = inflater.inflate( R.layout.linha_para_listview, parent, false);
		
		// Depois da linha criada, podemos arrecadar suas caracteristicas.
		TextView nome  = (TextView) linhaAdaptada.findViewById(R.id.textView1 );
				
		nome.setTextColor(Color.BLACK);
		nome.setText("Código: " + itens.get(position).getCodigo() + " - " + "Descrição: " + itens.get(position).getDescricao() ); 


		return linhaAdaptada; 
	}
	
	@Override
	public int getCount() {
		return itens.size();
	}
	@Override
	public Object getItem(int arg0) {
		return itens.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		return itens.hashCode();
	}
	
}
