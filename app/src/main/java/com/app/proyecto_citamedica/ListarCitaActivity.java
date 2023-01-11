package com.app.proyecto_citamedica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import Entidades.Citas;
import Entidades.util;

public class ListarCitaActivity extends AppCompatActivity {
private TextInputLayout tvPaciente,txtInputCita,txtInputTipoHorarioL,txtInputTipoEspecialidad,txtInputMedicoDisponible,txtInputFechaAtencion;
private EditText txtPacienteL,txtTHorario,txtTEspecialidad,txtMDisponible,txtFAtencion;
private AutoCompleteTextView cboCita;
Button btnECita;
public static String URL_LCitas="https://appcolegiophp.herokuapp.com/obtenercitaIdPaciente.php?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cita);
        /*txtTHorario.setEnabled(false);
        txtMDisponible.setEnabled(false);
        txtTEspecialidad.setEnabled(false);
        txtFAtencion.setEnabled(false);
        txtPacienteL.setEnabled(false);
        btnECita.setEnabled(false);
        String Nombre= util.nombre;
        String Apellido=util.apellido;
        String Pid=util.pId;
        txtPacienteL.setText("Paciente : "+Nombre+"  "+Apellido);
        obtenerCitas(Integer.parseInt(Pid));
        btnECita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValueCbo=cboCita.toString();
                if (nameValueCbo != null && !(nameValueCbo.equalsIgnoreCase("Seleccione"))) {
                    Intent i=new Intent(ListarCitaActivity.this,NCita_Activity.class);
                    i.putExtra("envEditar","Envio");
                    i.putExtra("envIdCbo",nameValueCbo);
                    startActivity(i);
                }

            }
        });
        cboCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo = cboCita.toString();
                Log.i("nameValueCbo", nameValueCbo);
                txtTHorario.setText("");
                txtTEspecialidad.setText("");
                txtMDisponible.setText("");
                txtFAtencion.setText("");
                btnECita.setEnabled(false);
                for (Citas objeto : util.lstCitas1) {
                    if (objeto.getcIdCbo().equalsIgnoreCase(nameValueCbo)) {
                        Log.i("objeto encontrado", objeto.toString2());
                        txtTHorario.setText(objeto.getNomHorario());
                        txtTEspecialidad.setText(objeto.getNomEspecialidad());
                        txtMDisponible.setText(objeto.getNomMedico());
                        txtFAtencion.setText(objeto.getFechaAtencion());
                        btnECita.setEnabled(true);
                    }

                }
            }*/
    }
}