package com.app.proyecto_citamedica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class QuienesS_Activity extends AppCompatActivity {
EditText edtSomos,edtInformacion,edtVision,edtMision;
Button btnSalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quienes_sactivity);
        inicializar();
        edtSomos.setEnabled(false);
        edtInformacion.setEnabled(false);
        edtVision.setEnabled(false);
        edtMision.setEnabled(false);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent i=new Intent(QuienesS_Activity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void inicializar(){
        edtSomos=findViewById(R.id.edtSomos);
        edtInformacion=findViewById(R.id.edtInformacion);
        edtVision=findViewById(R.id.edtVision);
        edtMision=findViewById(R.id.edtMision);
        btnSalir=findViewById(R.id.btnSalir);
    }
}