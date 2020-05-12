package com.brasma.sistemajuntas;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.brasma.sistemajuntas.entidades.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Procesos extends AppCompatActivity {
    public static String id;
    static ProgressDialog cargando;
    static Usuario user;
    static int i = 0;
    private static DatabaseReference fireReference;


    public Procesos() {

    }

    public static Usuario getUsuario(final Context context) {
        fireReference = FirebaseDatabase.getInstance().getReference();
        user = new Usuario();
        Toast.makeText(context, "id:  " + id, Toast.LENGTH_LONG).show();
        fireReference.child("Usuarios").child("u7AlaXh8x5YBKvcVAlqrqZZs7jM2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user.setCedula(dataSnapshot.child("cedula").getValue().toString());
                    user.setNombre(dataSnapshot.child("nombre").getValue().toString());
                    user.setContraseña(dataSnapshot.child("contraseña").getValue().toString());
                    user.setNumero(dataSnapshot.child("numero").getValue().toString());
                    user.setUsuario(dataSnapshot.child("usuario").getValue().toString());
                    i = 1;
                } else {
                    Toast.makeText(context, "usuario no exite", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (i == 1) {
            Toast.makeText(context, "entro", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No entro", Toast.LENGTH_SHORT).show();
        }

        return user;
    }

    public static void cargandoIniciar(Context context) {
        //inicializamos progres dialog
        cargando = new ProgressDialog(context);
        //mostramos progres
        cargando.show();
        //no se puede salir
        cargando.setCancelable(false);
        // enviamos el contenido del dilogo
        cargando.setContentView(R.layout.dialog_activity_cargando);
        //transparente
        cargando.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void cargandoDetener() {
        cargando.dismiss();
    }

    public static String controlarDecimales(double numDecimal) {
        DecimalFormat df = new DecimalFormat("#.00");
        String deci = df.format(numDecimal);
        String str = String.valueOf(numDecimal);
        String[] vector = deci.split(".");
        String decimales;
        try {
            decimales = str.substring(str.indexOf('.') + 1);
        } catch (Exception e) {
            decimales = str.substring(str.indexOf(',') + 1);
        }
        if (decimales.length() > 2) {
            return df.format(numDecimal);
        }
        return numDecimal + "";
    }

    private static boolean esNumero(String numero) {
        int num = 0;
        try {
            num = Integer.parseInt(numero);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String controlarEnteros(String numDecimal) {
        String str = numDecimal;
        String decimales;
        try {
            decimales = str.substring(str.indexOf('.') + 1);
        } catch (Exception e) {
            decimales = str.substring(str.indexOf(',') + 1);
        }
        if (decimales.equals("0")) {
            return cogerSoloEnteros(Double.parseDouble(numDecimal)) + "";
        } else {
            return numDecimal;
        }
    }

    public static int cogerSoloEnteros(double numero) {
        String str = String.valueOf(numero);
        int intNumber = Integer.parseInt(str.substring(0, str.indexOf('.')));
        return intNumber;
    }
}
