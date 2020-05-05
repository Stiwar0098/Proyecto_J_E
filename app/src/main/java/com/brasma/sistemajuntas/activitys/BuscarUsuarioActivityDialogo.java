package com.brasma.sistemajuntas.activitys;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brasma.sistemajuntas.Procesos;
import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.adaptadores.AdapterItemBuscarUsuario;
import com.brasma.sistemajuntas.entidades.Usuario;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuscarUsuarioActivityDialogo {

    final Context context;
    List<Usuario> listaUsuarios;
    Dialog dialogo;
    CardView cardViewBuscarUsuario;
    AdapterItemBuscarUsuario adaptadorItemBuscarUsuario;
    RecyclerView recyclerViewBuscarUsuario;
    DatabaseReference fireReference;
    private finalizoDialogBuscarUsuario interfaz;

    public BuscarUsuarioActivityDialogo(Context context1, finalizoDialogBuscarUsuario actividad) {
        interfaz = actividad;
        context = context1;
        dialogo = new Dialog(context);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);//false
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.dialogo_activity_buscar_usuario);
        fireReference = FirebaseDatabase.getInstance().getReference();
        cardViewBuscarUsuario = (CardView) dialogo.findViewById(R.id.cardview_BuscarUsuario);
        TextInputLayout textInputBuscarUsuario = (TextInputLayout) dialogo.findViewById(R.id.txtImputBuscarUsuario_DialogBuscarUsuario);
        ImageButton btnBuscarUuario = (ImageButton) dialogo.findViewById(R.id.btnBuscarUsuario_DialogBuscarUsuario);
        btnBuscarUuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatos("");
            }
        });
        dialogo.show();
        mostrarDatos("");
    }

    public void mostrarDatos(String buscar) {
        Procesos.cargandoIniciar(dialogo.getContext());
        listaUsuarios = new ArrayList<>();
        fireReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuarios.clear();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Usuario user = snapShot.getValue(Usuario.class);
                    listaUsuarios.add(user);
                }
                cardViewBuscarUsuario.setVisibility(View.VISIBLE);
                Procesos.cargandoDetener();
                adaptadorItemBuscarUsuario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        // crear lista de carview dentro del recycleview
        recyclerViewBuscarUsuario = (RecyclerView) dialogo.findViewById(R.id.recyclerView_DialogBuscarUsuario);
        recyclerViewBuscarUsuario.setLayoutManager(new LinearLayoutManager(context));

        adaptadorItemBuscarUsuario = new AdapterItemBuscarUsuario(listaUsuarios);
        recyclerViewBuscarUsuario.setAdapter(adaptadorItemBuscarUsuario);

        adaptadorItemBuscarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario us = listaUsuarios.get(recyclerViewBuscarUsuario.getChildAdapterPosition(v));
                interfaz.UsuarioSelecionado(us);
                dialogo.dismiss();
            }
        });
    }

    public interface finalizoDialogBuscarUsuario {
        void UsuarioSelecionado(Usuario user);
    }
}


