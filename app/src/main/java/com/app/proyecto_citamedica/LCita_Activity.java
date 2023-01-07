package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import Entidades.ClassCboModel;
import Entidades.util;

public class LCita_Activity extends AppCompatActivity {
Button btnECita;
public static String URL_LCitas="https://appcolegiophp.herokuapp.com/obtenercitaIdPaciente.php";
EditText edtTHorario, edtEspecialidad,edtMDisponible,edtFAtencion;
Spinner spCita;
TextView  tvPacienteL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcita);
        //HOLA
        inicializar();
        Intent intent=getIntent();
        String Nombre=intent.getStringExtra("Nombres");
        Intent intent1=getIntent();
        String Apellido=intent1.getStringExtra("Apellidos");
        Intent intent2=getIntent();
        String Pid=intent2.getStringExtra("Pid");
        tvPacienteL.setText("Paciente : "+Nombre+"  "+Apellido);
        //obtenerCitas(54);
        btnECita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LCita_Activity.this,NCita_Activity.class);
                i.putExtra("envEditar","Envio");
                startActivity(i);
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
                    ArrayList<ClassCboModel> lstModelCbo = new ArrayList<ClassCboModel>();
                    lstModelCbo.add(new ClassCboModel("0","Seleccione"));
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject citasObject=arr.getJSONObject(i);
                        lstModelCbo.add(new ClassCboModel(citasObject.getString("Cid"),citasObject.getString("nombreapellidomedico")));

                    }
                    cargarSpinnerCitas(lstModelCbo);

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
    public void cargarSpinnerCitas(ArrayList<ClassCboModel>lstModelCbo){
        util.lstCitas.clear();
        util.lstCitas=lstModelCbo;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstModelCbo);
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
    }
}