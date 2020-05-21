package com.brasma.sistemajuntas.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brasma.sistemajuntas.Procesos;
import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.activitys.prestamoActivity;
import com.brasma.sistemajuntas.entidades.PrestamoPrincipal;
import com.brasma.sistemajuntas.entidades.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterItemPrincipal extends RecyclerView.Adapter<AdapterItemPrincipal.ViewHolder> implements View.OnClickListener {

    public static List<PrestamoPrincipal> listaPrestamo;
    private View.OnClickListener listener;

    public AdapterItemPrincipal(List<PrestamoPrincipal> listaPrestamo) {
        this.listaPrestamo = listaPrestamo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_principal, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    public static int i = 0;
    public static Context context;

    @Override
    public int getItemCount() {
        return listaPrestamo.size();
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombreUsuario.setText(listaPrestamo.get(position).getNombreUsuario());
        holder.totalPrestado.setText(listaPrestamo.get(position).getTotalPrestado() + " $");
        holder.totalPagado.setText(listaPrestamo.get(position).getTotalPagado() + " $");
        holder.pendiente.setText(listaPrestamo.get(position).getPendiente() + " $");
        holder.cedula.setText(listaPrestamo.get(position).getCedula());
        holder.setOnClickListener();
        i = position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombreUsuario, totalPrestado, totalPagado, pendiente, cedula;
        private static ImageButton agregarPrestamobtn;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            nombreUsuario = (TextView) itemView.findViewById(R.id.txtNombreUsuario);
            totalPrestado = (TextView) itemView.findViewById(R.id.txtTotalPrestado);
            totalPagado = (TextView) itemView.findViewById(R.id.txtTotalPagado);
            pendiente = (TextView) itemView.findViewById(R.id.txtPendiente);
            cedula = (TextView) itemView.findViewById(R.id.txtCedula);
            agregarPrestamobtn = (ImageButton) itemView.findViewById(R.id.btnAgregar);
        }

        void setOnClickListener() {
            agregarPrestamobtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAgregar:
                    Procesos.cargandoIniciar(context);
                    DatabaseReference fireReference = null;
                    fireReference = FirebaseDatabase.getInstance().getReference();
                    Query q = fireReference.child("Usuarios").orderByChild("cedula").equalTo(listaPrestamo.get(i).getCedula());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuario us = null;
                            for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                us = snapShot.getValue(Usuario.class);
                            }
                            Intent intentPrestamo = new Intent(context, prestamoActivity.class);
                            intentPrestamo.putExtra("usuarioSeleccionado", us);
                            context.startActivity(intentPrestamo);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
            }
        }
    }

}
