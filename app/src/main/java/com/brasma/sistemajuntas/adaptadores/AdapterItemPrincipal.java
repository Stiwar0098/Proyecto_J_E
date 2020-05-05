package com.brasma.sistemajuntas.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.activitys.deudasActivity;
import com.brasma.sistemajuntas.activitys.prestamoActivity;
import com.brasma.sistemajuntas.entidades.PrestamoPrincipal;

import java.util.List;

public class AdapterItemPrincipal extends RecyclerView.Adapter<AdapterItemPrincipal.ViewHolder> {

    public List<PrestamoPrincipal> listaPrestamo;

    public AdapterItemPrincipal(List<PrestamoPrincipal> listaPrestamo) {
        this.listaPrestamo = listaPrestamo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_principal, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombreUsuario.setText(listaPrestamo.get(position).getNombreUsuario());
        holder.totalPrestado.setText(listaPrestamo.get(position).getTotalPrestado() + " $");
        holder.totalPagado.setText(listaPrestamo.get(position).getTotalPagado() + " $");
        holder.pendiente.setText(listaPrestamo.get(position).getPendiente() + " $");
        holder.cedula.setText(listaPrestamo.get(position).getCedula());
        holder.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return listaPrestamo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context;
        private TextView nombreUsuario, totalPrestado, totalPagado, pendiente, cedula;
        private Button deudasbtn;
        private ImageButton agregarPrestamobtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nombreUsuario = (TextView) itemView.findViewById(R.id.txtNombreUsuario);
            totalPrestado = (TextView) itemView.findViewById(R.id.txtTotalPrestado);
            totalPagado = (TextView) itemView.findViewById(R.id.txtTotalPagado);
            pendiente = (TextView) itemView.findViewById(R.id.txtPendiente);
            cedula = (TextView) itemView.findViewById(R.id.txtCedula);
            deudasbtn = (Button) itemView.findViewById(R.id.btnDeudas);
            agregarPrestamobtn = (ImageButton) itemView.findViewById(R.id.btnAgregar);
        }

        void setOnClickListener() {
            deudasbtn.setOnClickListener(this);
            agregarPrestamobtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnDeudas:
                    Intent intentDeudas = new Intent(context, deudasActivity.class);
                    intentDeudas.putExtra("nombre", nombreUsuario.getText());
                    context.startActivity(intentDeudas);
                    break;
                case R.id.btnAgregar:
                    Intent intentPrestamo = new Intent(context, prestamoActivity.class);
                    context.startActivity(intentPrestamo);
                    break;
            }
        }
    }

}
