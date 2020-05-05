package com.brasma.sistemajuntas.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.brasma.sistemajuntas.Procesos;
import com.brasma.sistemajuntas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences.Editor editor;
    private Button btnIngresar;
    private FirebaseAuth mAuth;
    private TextInputLayout txtUsuario, txtContrasena;
    private String usuario, contraseña;
    private ConnectivityManager con;
    private NetworkInfo networkInfo;
    private TextView txtCorreo;
    private DatabaseReference fireReference;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        btnIngresar = (Button) findViewById(R.id.btnIngresar_Login);
        txtUsuario = (TextInputLayout) findViewById(R.id.txtImputUsuario_Login);
        txtContrasena = (TextInputLayout) findViewById(R.id.txtImputContraseña_Login);
        txtCorreo = (TextView) findViewById(R.id.txtCorreo);
        txtCorreo.setText("@juntasexpress.com");
        btnIngresar.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (cargarPreferencias() != null) {
            Intent intent = new Intent(loginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = con.getActiveNetworkInfo();
        if (validarText()) {
            if (networkInfo != null && networkInfo.isConnected()) {
                Procesos.cargandoIniciar(loginActivity.this);
                usuario = usuario + txtCorreo.getText();
                validarUsuarioContraseña(usuario, contraseña);
            } else {
                Toast.makeText(this, "Debe estar conectado a internet.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void validarUsuarioContraseña(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            guardarPreferencias();// y ejecutar main
                        } else {
                            Procesos.cargandoDetener();
                            Toast.makeText(loginActivity.this, "Authentication failed. " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean validarText() {
        usuario = txtUsuario.getEditText().getText().toString().trim();
        contraseña = txtContrasena.getEditText().getText().toString().trim();
        int valor = 0;
        if (usuario.isEmpty()) {
            txtUsuario.setError("El campo no puede estar vacío");
            valor = 1;
        } else {
            txtUsuario.setError(null);
        }
        if (contraseña.isEmpty()) {
            txtContrasena.setError("El campo no puede estar vacío");
            valor = 1;
        } else {
            txtContrasena.setError(null);
        }
        if (valor == 0) {
            return true;
        }
        return false;
    }

    public void guardarPreferencias() {
        obtenerUsuario();
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("uid", mAuth.getCurrentUser().getUid());
    }

    public void obtenerUsuario() {
        fireReference = FirebaseDatabase.getInstance().getReference();
        fireReference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nombre = (dataSnapshot.child("nombre").getValue().toString());
                    editor.putString("nombreTitulo", nombre);
                    editor.commit();
                    Toast.makeText(loginActivity.this, "Authentication success", Toast.LENGTH_SHORT).show();
                    Procesos.cargandoDetener();
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        return preferences.getString("uid", null);
    }
}
