package com.brasma.sistemajuntas.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brasma.sistemajuntas.R;

public class deudasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas);
        // activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("nombre");
        }
        TextView nombre;
        nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(value);
    }
}
