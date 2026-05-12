package com.skoolage.xapxzapnator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skoolage.xapxzapnator.R;
import com.skoolage.xapxzapnator.models.Conversante;

import java.util.List;

public class ConversanteAdapter extends ArrayAdapter<Conversante> {

    private final Context context;
    private final List<Conversante> lista;

    public ConversanteAdapter(Context context, List<Conversante> lista) {
        super(context, R.layout.item_detalhe, lista);
        this.context = context;
        this.lista = lista;
    }

    private class ViewHolder {
        TextView txtNome;
        TextView txtCelular;
        TextView txtEmail;
    }

    public View getView(int pos, View linha, ViewGroup parent) {
        ViewHolder holder;
        if (linha == null) {
            linha = LayoutInflater.from(context).inflate(R.layout.item_detalhe, parent, false);
            holder = new ViewHolder();
            holder.txtNome = linha.findViewById(R.id.txtNome);
            holder.txtCelular = linha.findViewById(R.id.txtCelular);
            holder.txtEmail = linha.findViewById(R.id.txtEmail);
            linha.setTag(holder);
        } else{
            holder = (ViewHolder) linha.getTag();
        }
        Conversante c = lista.get(pos);
        holder.txtNome.setText(c.getNome());
        holder.txtCelular.setText(c.getCelular());
        holder.txtEmail.setText(c.getEmail());

        return linha;
    }
}
