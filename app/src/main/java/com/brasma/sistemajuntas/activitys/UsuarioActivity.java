package com.brasma.sistemajuntas.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.brasma.sistemajuntas.Procesos;
import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.entidades.Usuario;
import com.brasma.sistemajuntas.entidades.UsuariosCantidadPrestamos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import static com.brasma.sistemajuntas.R.layout.activity_usuario;

public class UsuarioActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    FirebaseAuth fireInstancia;
    DatabaseReference fireReference;
    String id, idAux;
    Usuario us;
    ConnectivityManager con;
    NetworkInfo networkInfo;
    private TextInputLayout txtImputNombre, txtImputNumero, txtImputUsuario, txtImputContraseña, txtImputConfirmarContraseña, txtImputCedula;
    private Button btnGuardar;
    private String nombre, numero, usuario, contraseña, cedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_usuario);
        final Context context = this;
        fireInstancia = FirebaseAuth.getInstance();
        fireReference = FirebaseDatabase.getInstance().getReference();

        txtImputNombre = findViewById(R.id.txtImputNombre);
        txtImputNumero = findViewById(R.id.txtImputNumero);
        txtImputUsuario = findViewById(R.id.txtImputUsuario);
        txtImputContraseña = findViewById(R.id.txtImputContraseña);
        txtImputConfirmarContraseña = findViewById(R.id.txtImputConfirmarContraseña);
        btnGuardar = findViewById(R.id.btnGuardarUsuario);
        txtImputCedula = (TextInputLayout) findViewById(R.id.txtInputCedula_Usuario);
        txtImputCedula.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtImputUsuario.getEditText().setText(s + "@juntasexpress.com");
                txtImputContraseña.getEditText().setText(s);
                txtImputConfirmarContraseña.getEditText().setText(s);
                txtImputUsuario.setError(null);
                txtImputContraseña.setError(null);
                txtImputConfirmarContraseña.setError(null);

            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtImputNombre.getEditText().requestFocusFromTouch();
                if (!validarTexts()) {
                    btnGuardar.requestFocusFromTouch();
                    cerrarTeclado();
                    return;
                }
                Procesos.cargandoIniciar(UsuarioActivity.this);
                btnGuardar.requestFocusFromTouch();
                registrarUsuario();
            }
        });
        // activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //forzar icono en el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mostrar icono
        getSupportActionBar().setIcon(R.drawable.ic_add_user);
        getSupportActionBar().setTitle("Crear Usuario");
        validarfocusError();
        txtImputNombre.getEditText().requestFocusFromTouch();
    }

    private void registrarUsuario() {
        id = (UUID.randomUUID().toString());
        us = new Usuario();
        us.setNombre(nombre);
        us.setCedula(cedula);
        us.setNumero(numero);
        us.setUsuario(usuario);
        us.setContraseña(contraseña);
        us.setUid(id);
        con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = con.getActiveNetworkInfo();

        if (!(networkInfo != null && networkInfo.isConnected())) {
            Toast.makeText(this, "El registro se actualizará cuando tenga internet", Toast.LENGTH_LONG).show();
            Procesos.cargandoDetener();
            startActivity(new Intent(UsuarioActivity.this, MainActivity.class));
            finish();
        }
        fireReference.child("Usuarios").child(id).setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        crearAutentication();
                        Toast.makeText(UsuarioActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Procesos.cargandoDetener();
                        startActivity(new Intent(UsuarioActivity.this, MainActivity.class));
                        finish();
                    } else {
                        crearAutentication();
                        Toast.makeText(UsuarioActivity.this, "Actualización de registros exitosa", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void crearAutentication() {
        fireInstancia.createUserWithEmailAndPassword(usuario, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    idAux = fireInstancia.getCurrentUser().getUid();
                    fireReference.child("Usuarios").child(id).removeValue();
                    us.setUid(idAux);
                    fireReference.child("Usuarios").child(idAux).setValue(us);
                    fireReference.child("UsuariosCantidadPrestamos").child(idAux).setValue(new UsuariosCantidadPrestamos(0));
                } else {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si el usuario ya xiste
                            Toast.makeText(UsuarioActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(UsuarioActivity.this, "No se a podido generar el correo", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UsuarioActivity.this, "Correo se guardara despues", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void cerrarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void validarfocusError() {
        txtImputNombre.getEditText().setOnFocusChangeListener(this);
        txtImputNumero.getEditText().setOnFocusChangeListener(this);
        txtImputUsuario.getEditText().setOnFocusChangeListener(this);
        txtImputContraseña.getEditText().setOnFocusChangeListener(this);
        txtImputConfirmarContraseña.getEditText().setOnFocusChangeListener(this);
        txtImputCedula.getEditText().setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.txtEditNombre:
                if (hasFocus) {
                    txtImputNombre.setError(null);
                }
                break;
            case R.id.txtEditNumero:
                if (hasFocus) {
                    txtImputNumero.setError(null);
                }
                break;
            case R.id.txtEditUsuario:
                if (hasFocus) {
                    txtImputUsuario.setError(null);
                }
                break;
            case R.id.txtEditContraseña:
                if (hasFocus) {
                    txtImputContraseña.setError(null);
                }
                break;
            case R.id.txtEditConfirmarContraseña:
                if (hasFocus) {
                    txtImputConfirmarContraseña.setError(null);
                }
                break;
            case R.id.txtEditCedula_Usuario:
                if (hasFocus) {
                    txtImputCedula.setError(null);
                }
                break;
        }

    }

    private boolean validarTexts() {
        int valor = 0; // 0 es true 1 es false
        nombre = txtImputNombre.getEditText().getText().toString().trim();
        numero = txtImputNumero.getEditText().getText().toString().trim();
        usuario = txtImputUsuario.getEditText().getText().toString().trim();
        contraseña = txtImputContraseña.getEditText().getText().toString().trim();
        String confirmarContraseña = txtImputConfirmarContraseña.getEditText().getText().toString().trim();
        cedula = txtImputCedula.getEditText().getText().toString().trim();

        // validar nombre
        if (nombre.isEmpty()) {
            txtImputNombre.setError("El campo no puede estar vacío");
            valor = 1;
        } else {
            txtImputNombre.setError(null);
        }
        //validar cedula
        if (cedula.isEmpty()) {
            txtImputCedula.setError("El campo no puede estar vacío");
            valor = 1;
        } else if (cedula.length() > 10 | cedula.length() < 10) {
            txtImputCedula.setError("Cédula debe tener 10 caracteres");
            valor = 1;
        } else {
            txtImputCedula.setError(null);
        }
        //validar numero
        if (numero.isEmpty()) {
            txtImputNumero.setError("El campo no puede estar vacío");
            valor = 1;
        } else if (numero.length() > 10) {
            txtImputNumero.setError("Número demasiado largo");
            valor = 1;
        } else {
            txtImputNumero.setError(null);
        }
        //validar usuario
        if (usuario.isEmpty()) {
            txtImputUsuario.setError("El campo no puede estar vacío");
            valor = 1;
        } else if (usuario.length() > 28) {
            txtImputUsuario.setError("Usuario demasiado largo");
            valor = 1;
        } else {
            txtImputUsuario.setError(null);
        }
        //validar contraseña
        if (contraseña.isEmpty()) {
            txtImputContraseña.setError("El campo no puede estar vacío");
            valor = 1;
        } else if (contraseña.length() > 10) {
            txtImputContraseña.setError("Contraseña demasiada larga");
            valor = 1;
        } else if (!contraseña.equals(confirmarContraseña)) {
            txtImputContraseña.setError("Contraseñas no coinciden");
            valor = 1;
        } else {
            txtImputContraseña.setError(null);
        }
        //validar confirmar contraseña
        if (confirmarContraseña.isEmpty()) {
            txtImputConfirmarContraseña.setError("El campo no puede estar vacío");
            valor = 1;
        } else if (confirmarContraseña.length() > 10) {
            txtImputConfirmarContraseña.setError("Contraseña demasiada larga");
            valor = 1;
        } else if (!contraseña.equals(confirmarContraseña)) {
            txtImputConfirmarContraseña.setError("Contraseñas no coinciden");
            valor = 1;
        } else {
            txtImputConfirmarContraseña.setError(null);
        }
        if (valor == 0) {
            return true;
        }
        return false;
    }


}
