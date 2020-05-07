package com.brasma.sistemajuntas.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.entidades.PrestamoPrincipal;

public class deudasActivity extends AppCompatActivity {

    PrestamoPrincipal prestamoPrincipal = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas);
        // activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            prestamoPrincipal = (PrestamoPrincipal) extras.getSerializable("prestamoPrincipal");
            TextView t = findViewById(R.id.nombre_deudas);
            t.setText(prestamoPrincipal.getNombreUsuario());
        }

    }

}
