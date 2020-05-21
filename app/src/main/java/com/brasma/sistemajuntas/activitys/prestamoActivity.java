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
import java.util.Date;
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
            txtInteresPorcentaje_Prestamo,
            txtNumeroSemanas;
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
    int diaFin, mesFin, anoFin;
    private String usuarioSelecionado,
            numeroJunta,
            cantidadPrestada,
            FechaFin,
            descripcion,
            fechaInicio,
            numeroSemanas,
            interesDolar,
            interesPorciento,
            fechaFin,
            fechaInicioInteres;
    private TextInputLayout
            txtInputFechaFin,
            txtInputUsuarioSelecionado,
            txtImputNumeroJunta,
            txtInputCantidadPrestada,
            txtInputFechaInicio,
            txtInputFechaInicioInteres,
            txtImputDescripcion;

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
        txtNumeroSemanas = (EditText) findViewById(R.id.txtNumeroSemanas_Prestamo);
        txtInputFechaFin = (TextInputLayout) findViewById(R.id.txtImputFechaFin_Prestamo);
        txtInteresPorcentaje_Prestamo.setSelectAllOnFocus(true);
        txtNumeroSemanas.setSelectAllOnFocus(true);
        txtInteresPorcentaje_Prestamo.setSelectAllOnFocus(true);
        txtInteresDolar_Prestamo.setSelectAllOnFocus(true);
        txtInteresDolar_Prestamo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty())) {
                    if (spinnerFormaPago.getSelectedItemPosition() == 1) {
                        double cantidadPrestada = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim());
                        double interes = 0;
                        if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
                            interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
                        }
                        int numSemanas = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
                        double cantidadPorDia = (cantidadPrestada + interes) / convertirSemanasADias();
                        descricionValor = "$" + Procesos.controlarEnteros(Procesos.controlarDecimales(cantidadPorDia));
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                    }
                    if (spinnerFormaPago.getSelectedItemPosition() == 2) {
                        double cantidadPrestada = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim());
                        double interes = 0;
                        if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
                            interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
                        }
                        int numSemanas = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
                        double cantidadPorDia = (cantidadPrestada + interes) / (numSemanas);
                        descricionValor = "$" + Procesos.controlarEnteros(Procesos.controlarDecimales(cantidadPorDia));
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                    }
                }
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
        txtNumeroSemanas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // descripccion automatica
                if (txtNumeroSemanas.hasFocus()) {
                    if (s.toString().isEmpty()) {
                        descricionValor = "";
                        if (spinnerFormaPago.getSelectedItemPosition() == 0) {
                            descripcioTiempo = "";
                        }
                        if (!descricionValor.isEmpty() && !descripcionDia.isEmpty()) {
                            txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                        }
                    } else {
                        if (!(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty())) {
                            if (spinnerFormaPago.getSelectedItemPosition() == 1) {
                                double cantidadPrestada = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim());
                                double interes = 0;
                                if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
                                    interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
                                }
                                int numSemanas = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
                                double cantidadPorDia = (cantidadPrestada + interes) / convertirSemanasADias();
                                descricionValor = "$" + Procesos.controlarEnteros(Procesos.controlarDecimales(cantidadPorDia));
                                txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                            }
                            if (spinnerFormaPago.getSelectedItemPosition() == 2) {
                                double cantidadPrestada = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim());
                                double interes = 0;
                                if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
                                    interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
                                }
                                int numSemanas = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
                                double cantidadPorDia = (cantidadPrestada + interes) / (numSemanas);
                                descricionValor = "$" + Procesos.controlarEnteros(Procesos.controlarDecimales(cantidadPorDia));
                                txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                            }
                        }
                        // fecha fin automatica
                        if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty()) && !(txtInputFechaFin.getEditText().getText().toString().trim().isEmpty())) {
                            Date fechaFin;
                            if (spinnerFormaPago.getSelectedItemPosition() == 1) {
                                fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, convertirSemanasADias());
                                txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                            } else if (spinnerFormaPago.getSelectedItemPosition() != 0) {
                                int sema = Integer.parseInt(txtNumeroSemanas.getText().toString());
                                fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, sema * 7);
                                txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                            }
                        }
                    }
                }
            }
        });
        txtInputCantidadPrestada.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtInputCantidadPrestada.hasFocus()) {
                    //se calcular el interes
                    if (!(s.toString().isEmpty())) {
                        generarNumeroSemanas(s);
                        if (!(txtInteresPorcentaje_Prestamo.getText().toString().isEmpty())) {
                            double valorInteresPorciento = Double.parseDouble(txtInteresPorcentaje_Prestamo.getText().toString().trim());
                            double valorPrestado = Double.parseDouble(s.toString());
                            double auxDolar = (valorInteresPorciento * valorPrestado) / 100;
                            txtInteresDolar_Prestamo.setText(Procesos.controlarEnteros(Procesos.controlarDecimales(auxDolar)));
                        }
                        //fecha automatica
                        if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputFechaInicio.getEditText().getText().toString().trim().isEmpty())) {
                            Date fechaFin;
                            if (spinnerFormaPago.getSelectedItemPosition() == 1) {
                                fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, convertirSemanasADias());
                                txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                            } else if (spinnerFormaPago.getSelectedItemPosition() != 0) {
                                int sema = Integer.parseInt(txtNumeroSemanas.getText().toString());
                                fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, sema * 7);
                                txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                            }
                        }
                    } else {
                        descricionValor = "";
                        if (spinnerFormaPago.getSelectedItemPosition() == 0) {
                            descripcioTiempo = "";
                        }
                        if (!descricionValor.isEmpty() && !descripcionDia.isEmpty()) {
                            txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                        }
                        txtInteresDolar_Prestamo.setText("");
                        txtNumeroSemanas.setText("");
                    }
                }
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Usuario user = (Usuario) extras.getSerializable("usuarioSeleccionado");
            if (user == null) {
                Toast.makeText(this, "No se a podido leer el usuario", Toast.LENGTH_SHORT).show();
            } else {
                Procesos.cargandoDetener();
                btnBuscarUsuario.setVisibility(View.GONE);
                UsuarioSelecionado(user);
            }

        }
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

    public void generarNumeroSemanas(Editable s) {
        int can = Integer.parseInt(s.toString());
        if (can < 100) {
            txtNumeroSemanas.setText("4");
        } else if (can >= 100 && can < 300) {
            txtNumeroSemanas.setText("6");
        } else if (can >= 300) {
            txtNumeroSemanas.setText("8");
        }
    }

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
                if (validarCampos()) {
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
                }
                break;
            case R.id.btnCalendario_Prestamo:
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtInputFechaInicio.getEditText().setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        diaFin = dayOfMonth;
                        mesFin = month;
                        anoFin = year;
                        //descripcion automatica
                        if (spinnerFormaPago.getSelectedItemPosition() == 2) {
                            descripcionDia = "(" + Procesos.diaSemana(Procesos.sumarRestarDiasAFecha(dayOfMonth, (month + 1), year, -1)) + ")";
                            txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                        } else if (spinnerFormaPago.getSelectedItemPosition() != 0) {
                            txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor);
                        }
                        //fecha automatica
                        if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty())) {
                            Date fechaFin;
                            if (spinnerFormaPago.getSelectedItemPosition() == 1) {
                                fechaFin = Procesos.sumarRestarDiasAFecha(dayOfMonth, month, year, convertirSemanasADias());
                                txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                            } else if (spinnerFormaPago.getSelectedItemPosition() != 0) {
                                int sema = Integer.parseInt(txtNumeroSemanas.getText().toString());
                                fechaFin = Procesos.sumarRestarDiasAFecha(dayOfMonth, month, year, sema * 7);
                                txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                            }
                        }
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
                        if (interesDolar.isEmpty()) {
                            interesDolar = "0";
                        }
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



    void setOnTouch() {
        spinnerFormaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                descripcioTiempo = "Paga " + parent.getItemAtPosition(position).toString();
                double cantidadPrestada;
                double interes = 0;
                int numSemanas;
                double cantidadPorDia;
                switch (position) {
                    case 0:
                        txtImputDescripcion.getEditText().setText("");
                        txtInputFechaFin.getEditText().setText("");
                        break;
                    case 1:
                        //descripccion automatica
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor);
                        if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty())) {
                            cantidadPrestada = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim());
                            interes = 0;
                            if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
                                interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
                            }
                            numSemanas = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
                            cantidadPorDia = (cantidadPrestada + interes) / convertirSemanasADias();
                            descricionValor = "$" + Procesos.controlarEnteros(Procesos.controlarDecimales(cantidadPorDia));
                            txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor);
                        }
                        //fecha automatica
                        if (!(txtNumeroSemanas.getText().toString().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().isEmpty()) && !(txtInputFechaInicio.getEditText().getText().toString().isEmpty())) {
                            Date fechaFin;
                            fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, convertirSemanasADias());
                            txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                        }
                        break;
                    case 2:
                        if (!txtInputFechaInicio.getEditText().getText().toString().isEmpty()) {
                            descripcionDia = "(" + Procesos.diaSemana(Procesos.sumarRestarDiasAFecha(diaFin, (mesFin + 1), anoFin, -1)) + ")";
                        }
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                        if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty())) {
                            cantidadPrestada = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim());
                            interes = 0;
                            if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
                                interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
                            }
                            numSemanas = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
                            cantidadPorDia = (cantidadPrestada + interes) / (numSemanas);
                            descricionValor = "$" + Procesos.controlarEnteros(Procesos.controlarDecimales(cantidadPorDia));
                            txtImputDescripcion.getEditText().setText(descripcioTiempo + " " + descricionValor + " " + descripcionDia);
                        }
                        //fecha automatica
                        if (!(txtNumeroSemanas.getText().toString().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().isEmpty()) && !(txtInputFechaInicio.getEditText().getText().toString().isEmpty())) {
                            Date fechaFin;
                            int sema = Integer.parseInt(txtNumeroSemanas.getText().toString());
                            fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, sema * 7);
                            txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                        }
                        break;
                    case 3:
                        txtImputDescripcion.getEditText().setText(descripcioTiempo + " todo");
                        //fecha automatica
                        if (!(txtNumeroSemanas.getText().toString().trim().isEmpty()) && !(txtInputCantidadPrestada.getEditText().getText().toString().trim().isEmpty()) && !(txtInputFechaInicio.getEditText().getText().toString().trim().isEmpty())) {
                            Date fechaFin;
                            int sema = Integer.parseInt(txtNumeroSemanas.getText().toString());
                            fechaFin = Procesos.sumarRestarDiasAFecha(diaFin, mesFin, anoFin, sema * 7);
                            txtInputFechaFin.getEditText().setText(fechaFin.getDate() + "-" + (fechaFin.getMonth() + 1) + "-" + (fechaFin.getYear() + 1900));
                        }
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

    public int convertirSemanasADias() {
        int numSemanasAux = Integer.parseInt(txtNumeroSemanas.getText().toString().trim());
        double interes = 0;
        if (!(txtInteresDolar_Prestamo.getText().toString().trim().isEmpty())) {
            interes = Double.parseDouble(txtInteresDolar_Prestamo.getText().toString().trim());
        }
        double cantidadPrestadaAux = Double.parseDouble(txtInputCantidadPrestada.getEditText().getText().toString().trim()) + interes;
        int dias = (numSemanasAux * 7);
        if (cantidadPrestadaAux % dias != 0) {
            dias = dias - 4;
            for (int i = 1; i <= 8; i++) {
                if (cantidadPrestadaAux > 60) {
                    String div = (cantidadPrestadaAux / dias) + "";
                    if (cantidadPrestadaAux % dias == 0 || div.length() <= 3) {
                        return dias;
                    } else {
                        dias = dias + 1;
                    }
                } else {
                    if (cantidadPrestadaAux % dias == 0) {
                        return dias;
                    } else {
                        dias = dias + 1;
                    }
                }
            }
        }
        return dias;
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

    public boolean validarCampos() {
        boolean validado = true;
        usuarioSelecionado = txtInputUsuarioSelecionado.getEditText().getText().toString().trim();
        numeroJunta = txtImputNumeroJunta.getEditText().getText().toString().trim();
        cantidadPrestada = txtInputCantidadPrestada.getEditText().getText().toString().trim();
        interesDolar = txtInteresDolar_Prestamo.getText().toString().trim();
        interesPorciento = txtInteresPorcentaje_Prestamo.getText().toString().trim();
        descripcion = txtImputDescripcion.getEditText().getText().toString().trim();
        fechaInicio = txtInputFechaInicio.getEditText().getText().toString().trim();
        fechaFin = txtInputFechaFin.getEditText().getText().toString().trim();
        numeroSemanas = txtNumeroSemanas.getText().toString().trim();
        if (usuarioSelecionado.isEmpty()) {
            btnBuscarUsuario.requestFocusFromTouch();
            //txtInputUsuarioSelecionado.getEditText().setError("Seleccione un usuario");
            Toast.makeText(this, "Porfavor seleccione un usuario.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cantidadPrestada.isEmpty()) {
            txtInputCantidadPrestada.getEditText().requestFocusFromTouch();
            Toast.makeText(this, "Porfavor ingrese la cantidad prestada.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (fechaInicio.isEmpty()) {
            btnCalendario.requestFocusFromTouch();
            Toast.makeText(this, "Porfavor seleccione una fecha de inicio.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (numeroSemanas.isEmpty()) {
            txtNumeroSemanas.requestFocusFromTouch();
            Toast.makeText(this, "Porfavor ingrese el numero de semanas.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spinnerFormaPago.getSelectedItemPosition() == 0) {
            spinnerFormaPago.requestFocusFromTouch();
            Toast.makeText(this, "Porfavor seleccione la forma de pago.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (switchIntereses.isChecked()) {
            if (interesDolar.isEmpty()) {
                txtInteresDolar_Prestamo.requestFocusFromTouch();
                Toast.makeText(this, "Porfavor ingrese el interés en dolares.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (interesPorciento.isEmpty()) {
                txtInteresPorcentaje_Prestamo.requestFocusFromTouch();
                Toast.makeText(this, "Porfavor ingrese el interés en porcentaje.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (!(radioButtonUnaVez.isChecked()) && !(radioButtonPeriodo.isChecked())) {
                Toast.makeText(this, "Porfavor selecione el tipo de interés UnaVez/Periodo.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return validado;
    }
}
