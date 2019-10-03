package com.packageComponents.selfcontrol;

import java.util.ArrayList;

import com.example.selfcontrol.R;
import com.packagemodel.selfcontrol.TituloPagar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


public class MinhaClasseAdapterTitPag extends BaseAdapter implements SpinnerAdapter{
	
	public Context context;
	public ArrayList<TituloPagar> itens;
	
	
	public MinhaClasseAdapterTitPag(Context context, ArrayList<TituloPagar> itens ) {
		this.context = context; 
		this.itens   = itens;
	}
	
	@Override
	// Esse método é executado para preencher cada linha da listas
	// É invocado automaticamente 
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
		nome.setText("C�digo: " + itens.get(position).getCodigo() + " - " + "Data: " + itens.get(position).getVencimento() + " - " +
					 "Saldo: " + itens.get(position).getSaldo()); 


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
