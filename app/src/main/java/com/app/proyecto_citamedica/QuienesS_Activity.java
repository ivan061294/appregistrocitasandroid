package com.app.proyecto_citamedica;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class QuienesS_Activity extends AppCompatActivity {
EditText edtSomos,edtInformacion,edtVision,edtMision;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quienes_sactivity);
        inicializar();
        edtSomos.setEnabled(false);
        edtInformacion.setEnabled(false);
        edtVision.setEnabled(false);
        edtMision.setEnabled(false);
    }
    public void inicializar(){
        edtSomos=findViewById(R.id.edtSomos);
        edtInformacion=findViewById(R.id.edtInformacion);
        edtVision=findViewById(R.id.edtVision);
        edtMision=findViewById(R.id.edtMision);
    }
}