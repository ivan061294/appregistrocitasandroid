package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Entidades.Citas;
import Entidades.ClassCboModel;
import Entidades.util;

public class LCita_Activity extends AppCompatActivity {
Button btnECita,btnSalir;
public static String URL_LCitas="https://appcolegiophp.herokuapp.com/obtenercitaIdPaciente.php?";
    public static String URL_BCitas="https://appcolegiophp.herokuapp.com/obtenerCitaId.php?";
EditText edtTHorario, edtEspecialidad,edtMDisponible,edtFAtencion;
Spinner spCita;
TextView  tvPacienteL;
public static String cid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcita);
        //HOLA
        inicializar();
        edtTHorario.setEnabled(false);
        edtMDisponible.setEnabled(false);
        edtEspecialidad.setEnabled(false);
        edtFAtencion.setEnabled(false);
        btnECita.setEnabled(false);
        String Nombre=util.nombre;
        String Apellido=util.apellido;
        String Pid=util.pId;
        tvPacienteL.setText("Paciente : "+Nombre+"  "+Apellido);
        obtenerCitas(Integer.parseInt(Pid));
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent i=new Intent(LCita_Activity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnECita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValueCbo=spCita.getSelectedItem().toString();
                if (nameValueCbo != null && !(nameValueCbo.equalsIgnoreCase("Seleccione"))) {
                    Intent i=new Intent(LCita_Activity.this,NCita_Activity.class);
                    i.putExtra("envEditar","Envio");
                    i.putExtra("envIdCbo",nameValueCbo);
                    startActivity(i);
                }

            }
        });
        spCita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spCita.getSelectedItem().toString();
                Log.i("nameValueCbo",nameValueCbo);
                edtTHorario.setText("");
                edtEspecialidad.setText("");
                edtMDisponible.setText("");
                edtFAtencion.setText("");
                btnECita.setEnabled(false);
                for(Citas objeto :util.lstCitas1){
                    if (objeto.getcIdCbo().equalsIgnoreCase(nameValueCbo)) {
                        Log.i("objeto encontrado",objeto.toString2());
                        edtTHorario.setText(objeto.getNomHorario());
                        edtEspecialidad.setText(objeto.getNomEspecialidad());
                        edtMDisponible.setText(objeto.getNomMedico());
                        edtFAtencion.setText(objeto.getFechaAtencion());
                        btnECita.setEnabled(true);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void obtenerCitas(int PId){
        Log.i("obteniendo CITAS","service");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_LCitas+PId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objSon = new JSONObject(response);
                    JSONArray arr = objSon.getJSONArray("data");
                    ArrayList<Citas> lstCitas1 = new ArrayList<Citas>();
                    Citas cita;
                    cita = new Citas();
                    cita.setcId("0");
                    cita.setcIdCbo("Seleccione");
                    lstCitas1.add(cita);
                    for(int i=0;i<arr.length();i++){
                        cita = new Citas();
                        JSONObject objCitas=arr.getJSONObject(i);
                        cita.setcId(objCitas.getString("Cid"));
                        cita.setcIdCbo("Cita n.º :"+objCitas.getString("Cid"));
                        cita.setMedicoId(objCitas.getString("Mid"));
                        cita.setNomMedico(objCitas.getString("nombreapellidomedico"));
                        cita.setEspecialidadId(objCitas.getString("Eid"));
                        cita.setNomEspecialidad(objCitas.getString("nombre"));
                        cita.setFechaAtencion(objCitas.getString("FechaAtencion"));
                        cita.setHorarioId(objCitas.getString("Hid"));
                        cita.setNomHorario(objCitas.getString("tipoHorario"));
                        cita.setObservaciones(objCitas.getString("observaciones"));
                        cita.setEstado(objCitas.getString("estado"));
                        lstCitas1.add(cita);
                    }
                    cargarSpinnerCitas(lstCitas1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error servicio",error.getMessage());
            }
        });

        requestQueue.add(stringRequest);


    }
    public void cargarSpinnerCitas(ArrayList<Citas>lstCitas2){
        util.lstCitas1.clear();
        util.lstCitas1=lstCitas2;
        ArrayAdapter<Citas> modelo = new ArrayAdapter<Citas>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstCitas2);
        spCita.setAdapter(modelo);
    }
    public void inicializar(){
        tvPacienteL=findViewById(R.id.tvPacienteL);
        btnECita=findViewById(R.id.btnEditarL);
        edtTHorario=findViewById(R.id.txtTHorario);
        edtEspecialidad=findViewById(R.id.txtTEspecialidad);
        edtMDisponible=findViewById(R.id.txtMDisponible);
        edtFAtencion=findViewById(R.id.txtFAtencion);
        spCita=findViewById(R.id.cboCita);
        btnSalir=findViewById(R.id.btnSalir);
    }
}