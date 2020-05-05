package com.brasma.sistemajuntas.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brasma.sistemajuntas.Procesos;
import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.adaptadores.AdapterItemPrincipal;
import com.brasma.sistemajuntas.entidades.PrestamoPrincipal;
import com.brasma.sistemajuntas.entidades.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String id, nombreTitulo = "";
    Usuario user;
    private RecyclerView recyclerViewPrestamo;
    private AdapterItemPrincipal adaptadorPrueba;
    private double totalPrestado = 0, totalPagado = 0, totalPendiente = 0;
    private TextView totalPrestadotxt, totalPagadotxt, totalPendientetxt;
    private Button usuariobtn, prestamobtn;
    private DatabaseReference fireReference;
    private FirebaseAuth fireInstancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarPreferencias();
        //forzar icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mostrar icono
        getSupportActionBar().setIcon(R.drawable.ic_home);
        getSupportActionBar().setTitle(nombreTitulo);
        getUsuario();


        usuariobtn = (Button) findViewById(R.id.btnUsuario);
        prestamobtn = (Button) findViewById(R.id.btnPrestamo);
        usuariobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UsuarioActivity.class));
            }
        });
        prestamobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, prestamoActivity.class));
            }
        });
        // crear lista de carview dentro del recycleview
        recyclerViewPrestamo = (RecyclerView) findViewById(R.id.recyclerPrestamosPrincipal);
        recyclerViewPrestamo.setLayoutManager(new LinearLayoutManager(this));
        adaptadorPrueba = new AdapterItemPrincipal(obtenerPrestamos());
        recyclerViewPrestamo.setAdapter(adaptadorPrueba);

        totalPrestadotxt = (TextView) findViewById(R.id.txtTotalPrestadoPrincipal);
        totalPagadotxt = (TextView) findViewById(R.id.txtTotalPagadoPrincipal);
        totalPendientetxt = (TextView) findViewById(R.id.txtTotalPendientePrincipal);
        totalPrestadotxt.setText(totalPrestado + " $");
        totalPagadotxt.setText(totalPagado + " $");
        totalPendiente = totalPrestado - totalPagado;
        totalPendientetxt.setText(totalPendiente + " $");
    }

    public void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid", null);
        editor.commit();
    }

    public void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        id = preferences.getString("uid", null);
        nombreTitulo = preferences.getString("nombreTitulo", null);
    }


    // metodo para ocultar y mostrar el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    // metodo para asignar lo que va a realizar cada opción
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemOpcion1:
                Procesos.cargandoIniciar(this);
                fireInstancia = FirebaseAuth.getInstance();
                fireInstancia.signOut();
                guardarPreferencias();
                Toast.makeText(this, "Seción finalizada", Toast.LENGTH_LONG).show();
                Procesos.cargandoDetener();
                startActivity(new Intent(MainActivity.this, loginActivity.class));
                break;
            case R.id.itemOpcion2:
                Toast.makeText(this, "Opcion 2", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUsuario() {
        fireReference = FirebaseDatabase.getInstance().getReference();
        user = new Usuario();
        fireReference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user.setCedula(dataSnapshot.child("cedula").getValue().toString());
                    user.setNombre(dataSnapshot.child("nombre").getValue().toString());
                    user.setContraseña(dataSnapshot.child("contraseña").getValue().toString());
                    user.setNumero(dataSnapshot.child("numero").getValue().toString());
                    user.setUsuario(dataSnapshot.child("usuario").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<PrestamoPrincipal> obtenerPrestamos() {
        List<PrestamoPrincipal> prestamo = new ArrayList<>();
        prestamo.add(new PrestamoPrincipal("Brayan", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Cristina", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Piedra", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Lucia", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Stiwar", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Macas", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Loaiza", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Cristina", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Piedra", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Lucia", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Stiwar", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Macas", "0705820694", 200, 50));
        prestamo.add(new PrestamoPrincipal("Loaiza", "0705820694", 200, 50));
        for (int i = 0; i < prestamo.size(); i++) {
            totalPrestado = totalPrestado + prestamo.get(i).getTotalPrestado();
            totalPagado = totalPagado + prestamo.get(i).getTotalPagado();
        }
        return prestamo;
    }
}
