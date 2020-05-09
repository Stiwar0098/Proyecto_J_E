package com.brasma.sistemajuntas.activitys;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.brasma.sistemajuntas.Procesos;
import com.brasma.sistemajuntas.R;
import com.brasma.sistemajuntas.entidades.PrestamoPrincipal;
import com.brasma.sistemajuntas.entidades.Usuario;
import com.brasma.sistemajuntas.entidades.UsuariosCantidadPrestamos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;


public class prestamoActivity extends AppCompatActivity implements View.OnTouchListener, BuscarUsuarioActivityDialogo.finalizoDialogBuscarUsuario {

    Calendar calendar = Calendar.getInstance();
    DatePickerDialog
            datePickerDialog;
    Usuario usuario;
    int aux = 0; // variable declarada para deterner el bucle de guardado en UsuariosPrestamoPrincipal
    private Spinner
            spinnerFormaPago,
            spinnerPeriodo;
    private CardView
            cardViewIntereses;
    private Switch
            switchIntereses;
    private LinearLayout
            linearLayoutIntereses;
    private RadioButton
            radioButtonPeriodo,
            radioButtonUnaVez;
    private Button
            btnBuscarUsuario,
            btnGuardarPrestamo;
    String descripcioTiempo = "", descricionValor = "", descripcionDia = "";
    private EditText
            txtInteresDolar_Prestamo,
            txtInteresPorcentaje_Prestamo;
    private ImageButton btnCalendario,
            btnCalendarioIntereses;
    private int
            dia,
            mes,
            ano,
            sum;
    private DatabaseReference
            fireReference;
    private double
            cantidadPrestadaAnterior = 0,
            cantidadPagadaAnterior = 0,
            pagado = 0,
            prestado = 0;
    private String
            idPrestamo;
    private String usuarioSelecionado,
            numeroJunta,
            cantidadPrestada,
            descripcion,
            fechaInicio,
            numeroSemanas,
            interesDolar,
            interesPorciento,
            fechaInicioInteres;
    private TextInputLayout
            txtInputUsuarioSelecionado,
            txtImputNumeroJunta,
            txtInputCantidadPrestada,
            txtInputFechaInicio,
            txtInputFechaInicioInteres,
            txtImputDescripcion;

    public void onClickSwitch(View view) {
        if (view.getId() == R.id.switchCalcularInteres_Prestamo) {
            if (switchIntereses.isChecked()) {
                cerrarTeclado();
                cardViewIntereses.setVisibility(View.VISIBLE);
            } else {
                cerrarTeclado();
                cardViewIntereses.setVisibility(View.GONE);
            }
        }
    }

    public void onClickRadioButton(View view) {
        switch (view.getId()) {
            case R.id.radioButtonPeriodo_Prestamo:
                if (radioButtonPeriodo.isChecked()) {
                    cerrarTeclado();
                    linearLayoutIntereses.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radioButtonUnaVez_Prestamo:
                if (radioButtonUnaVez.isChecked()) {
                    cerrarTeclado();
                    linearLayoutIntereses.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void onClickButton(View view) {
        switch (view.getId()) {

            case R.id.btnBuscarUsuario_Prestamo:
                new BuscarUsuarioActivityDialogo(this, prestamoActivity.this);
                break;

            case R.id.btnGuardar_Prestamo:
                cerrarTeclado();
                validarCampos();
                Procesos.cargandoIniciar(prestamoActivity.this);
                idPrestamo = (UUID.randomUUID().toString());
                fireReference.child("UsuariosCantidadPrestamos").child(usuario.getUid()).setValue(new UsuariosCantidadPrestamos(sum)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if ((sum) == 2) {
                                prestado = Double.parseDouble(cantidadPrestada) + Double.parseDouble(interesDolar);
                                fireReference.child("UsuariosPrestamoPrincipal").child(usuario.getUid()).setValue(new PrestamoPrincipal(usuario.getNombre(), usuario.getCedula(), prestado, 0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(prestamoActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                            Procesos.cargandoDetener();
                                            startActivity(new Intent(prestamoActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                aux = 0;
                                obternerPrestamosPrincipal();
                            }
                        }
                    }
                });
                break;

            case R.id.btnCalendario_Prestamo:
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtInputFechaInicio.getEditText().setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
                break;

            case R.id.btnCalendarioInteres_Prestamo:
                cerrarTeclado();
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtInputFechaInicioInteres.getEditText().setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
                break;
        }

    }

    public void obternerPrestamosPrincipal() {
        fireReference.child("UsuariosPrestamoPrincipal").child(usuario.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (aux == 0) {
                        aux = 1;
                        cantidadPrestadaAnterior = Double.parseDouble(dataSnapshot.child("totalPrestado").getValue().toString());
                        cantidadPagadaAnterior = Double.parseDouble(dataSnapshot.child("totalPagado").getValue().toString());
                        prestado = Double.parseDouble(cantidadPrestada) + Double.parseDouble(interesDolar) + cantidadPrestadaAnterior;
                        pagado = cantidadPagadaAnterior;
                        fireReference.child("UsuariosPrestamoPrincipal").child(usuario.getUid()).setValue(new PrestamoPrincipal(usuario.getNombre(), usuario.getCedula(), prestado, pagado)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(prestamoActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    Procesos.cargandoDetener();
                                    startActivity(new Intent(prestamoActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamo);
        fireReference = FirebaseDatabase.getInstance().getReference();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        ano = calendar.get(Calendar.YEAR);
        // activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // asignamos los valores a las variables
        spinnerFormaPago = (Spinner) findViewById(R.id.spinnerFormaPago_Prestamo);
        spinnerPeriodo = (Spinner) findViewById(R.id.spinnerPeriodo_Prestamo);
        cardViewIntereses = (CardView) findViewById(R.id.cardviewInteres_Prestamo);
        switchIntereses = (Switch) findViewById(R.id.switchCalcularInteres_Prestamo);
        linearLayoutIntereses = (LinearLayout) findViewById(R.id.linearLayoutPeriodo_Prestamo);
        radioButtonPeriodo = (RadioButton) findViewById(R.id.radioButtonPeriodo_Prestamo);
        radioButtonUnaVez = (RadioButton) findViewById(R.id.radioButtonUnaVez_Prestamo);
        btnBuscarUsuario = (Button) findViewById(R.id.btnBuscarUsuario_Prestamo);
        btnGuardarPrestamo = (Button) findViewById(R.id.btnGuardar_Prestamo);
        txtInputCantidadPrestada = (TextInputLayout) findViewById(R.id.txtImputCantidadPrestada_Prestamo);
        btnCalendario = (ImageButton) findViewById(R.id.btnCalendario_Prestamo);
        txtInputFechaInicio = (TextInputLayout) findViewById(R.id.txtImputFechaInicio_Prestamo);
        txtInputFechaInicioInteres = (TextInputLayout) findViewById(R.id.txtImputFechaInicioInteres_Prestamo);
        btnCalendarioIntereses = (ImageButton) findViewById(R.id.btnCalendarioInteres_Prestamo);
        txtInputUsuarioSelecionado = (TextInputLayout) findViewById(R.id.txtImputUsuarioSelecionado_Prestamo);
        txtImputNumeroJunta = (TextInputLayout) findViewById(R.id.txtImputNumeroJunta_Prestamo);
        txtInputCantidadPrestada = (TextInputLayout) findViewById(R.id.txtImputCantidadPrestada_Prestamo);
        txtInteresDolar_Prestamo = (EditText) findViewById(R.id.txtInteresDolar_Prestamo);
        txtImputDescripcion = (TextInputLayout) findViewById(R.id.txtImputDescripcion_Prestamo);
        txtInteresPorcentaje_Prestamo = (EditText) findViewById(R.id.txtInteresPorcentaje_Prestamo);
        txtInteresPorcentaje_Prestamo.setSelectAllOnFocus(true);
        txtInteresDolar_Prestamo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtInteresDolar_Prestamo.hasFocus()) {
                    if (!(txtInputCantidadPrestada.getEditText().getText().toString().isEmpty())) {
                        if (s.toString().isEmpty()) {
                            txtInteresPorcentaje_Prestamo.setText("");
                        } else {
                            double valorInteresDolar = Double.parseDouble(s.toString());
                            double valorPrestado = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString());
                            double auxPorciento = (valorInteresDolar * 100) / valorPrestado;
                            txtInteresPorcentaje_Prestamo.setText(Procesos.controlarEnteros(Procesos.controlarDecimales(auxPorciento)));
                        }
                    }
                }
            }
        }); //calcular % de intereses automaticamente al poner el interes en $
        txtInteresPorcentaje_Prestamo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtInteresPorcentaje_Prestamo.hasFocus()) {
                    if (!(txtInputCantidadPrestada.getEditText().getText().toString().isEmpty())) {
                        if (s.toString().isEmpty()) {
                            txtInteresDolar_Prestamo.setText("");
                        } else {
                            double valorInteresPorciento = Double.parseDouble(s.toString());
                            double valorPrestado = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString());
                            double auxDolar = (valorInteresPorciento * valorPrestado) / 100;
                            txtInteresDolar_Prestamo.setText(Procesos.controlarEnteros(Procesos.controlarDecimales(auxDolar)));
                        }
                    }
                }
            }
        }); //calcular $ de intereses automaticamente al poner el interes en %
        //spinner opciones
        String[] spinnerOpcionesFormaPago = {"<Seleccione>", "Diario", "Semanal", "Al final"};
        String[] spinnerOpcionesPeriodo = {"<Seleccione>", "Diario"};
        ArrayAdapter<String> spinnerAdapterFormaPago = new ArrayAdapter<String>(this, R.layout.item_spinner, spinnerOpcionesFormaPago);
        spinnerFormaPago.setAdapter(spinnerAdapterFormaPago);
        ArrayAdapter<String> spinnerAdaptePeriodo = new ArrayAdapter<String>(this, R.layout.item_spinner, spinnerOpcionesPeriodo);
        spinnerPeriodo.setAdapter(spinnerAdaptePeriodo);
        setOnTouch();
        //ocultamos intereses
        cardViewIntereses.setVisibility(View.GONE);
        linearLayoutIntereses.setVisibility(View.GONE);

    }

    void setOnTouch() {
        spinnerFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                descripcioTiempo = parent.getItemAtPosition(position).toString();
                switch (position) {
                    case 0:
                        txtImputDescripcion.getEditText().setText(" " + descricionValor + " " + descripcionDia);
                        break;
                    case 1:
                    case 2:
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                        break;
                    case 3:
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " paga todo");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerFormaPago.setOnTouchListener(this);
        spinnerPeriodo.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.spinnerFormaPago_Prestamo:
            case R.id.spinnerPeriodo_Prestamo:
                cerrarTeclado();
                break;
        }
        return false;
    }

    @Override
    public void UsuarioSelecionado(Usuario user) {
        Procesos.cargandoIniciar(this);
        usuario = user;
        fireReference.child("UsuariosCantidadPrestamos").child(usuario.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int cantidadAnterior = Integer.parseInt((dataSnapshot.child("cantidad").getValue()).toString());
                    sum = cantidadAnterior + 1;
                    txtInputUsuarioSelecionado.getEditText().setText(usuario.getNombre());
                    txtImputNumeroJunta.getEditText().setText("Junta " + sum);
                    Procesos.cargandoDetener();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void validarCampos() {
        usuarioSelecionado = txtInputUsuarioSelecionado.getEditText().getText().toString().trim();
        numeroJunta = txtImputNumeroJunta.getEditText().getText().toString().trim();
        cantidadPrestada = txtInputCantidadPrestada.getEditText().getText().toString().trim();
        interesDolar = txtInteresDolar_Prestamo.getText().toString().trim();
    }
}
