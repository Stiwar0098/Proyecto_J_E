package com.brasma.sistemajuntas.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.entidades.Usuario;

import java.util.List;

public class AdapterItemBuscarUsuario extends RecyclerView.Adapter<AdapterItemBuscarUsuario.ViewHolder> implements View.OnClickListener {
    static List<Usuario> listaUsuarios;
    private View.OnClickListener listener;

    public AdapterItemBuscarUsuario(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_buscar_usuario, null, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterItemBuscarUsuario.ViewHolder holder, int position) {
        holder.txtnombreUsuario.setText(listaUsuarios.get(position).getNombre());
        holder.txtCedula.setText(listaUsuarios.get(position).getCedula());
        holder.txtNumero.setText(listaUsuarios.get(position).getNumero());
        holder.txtEmail.setText("Email: " + listaUsuarios.get(position).getUsuario());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtnombreUsuario, txtCedula, txtNumero, txtEmail;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txtnombreUsuario = (TextView) itemView.findViewById(R.id.txtNombre_ItemBuscarUsuario);
            txtCedula = (TextView) itemView.findViewById(R.id.txtCedula__ItemBuscarUsuario);
            txtNumero = (TextView) itemView.findViewById(R.id.txtNumero_ItemBuscarUsuario);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail_ItemBuscarUsuario);
        }
    }

}