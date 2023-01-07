package com.app.proyecto_citamedica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_Activity extends AppCompatActivity {
Button btnNCita,btnVerC,btnQuienesSomos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        inicializar();
        Intent intent=getIntent();
        String Nombre=intent.getStringExtra("Nombres");
        Intent intent1=getIntent();
        String Apellido=intent1.getStringExtra("Apellidos");
        Intent intent2=getIntent();
        String Pid=intent2.getStringExtra("Pid");

        btnVerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Menu_Activity.this,LCita_Activity.class);
                i.putExtra("Nombres",Nombre);
                i.putExtra("Apellidos",Apellido);
                i.putExtra("Pid",Pid);
                startActivity(i);
            }
        });
        btnNCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Menu_Activity.this,NCita_Activity.class);
                i.putExtra("Nombres",Nombre);
                i.putExtra("Apellidos",Apellido);
                i.putExtra("Pid",Pid);
                startActivity(i);
            }
        });
        btnQuienesSomos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Menu_Activity.this,QuienesS_Activity.class);
                startActivity(i);
            }
        });
    }
    public void inicializar(){
        btnNCita=findViewById(R.id.btnNuevaC);
        btnVerC=findViewById(R.id.btnVerC);
        btnQuienesSomos=findViewById(R.id.btnQuienesS);
    }
}