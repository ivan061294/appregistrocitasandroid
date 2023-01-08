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

import Entidades.Citas;
import Entidades.ClassCboModel;
import Entidades.util;

public class LCita_Activity extends AppCompatActivity {
Button btnECita;
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
        Intent intent=getIntent();
        String Nombre=intent.getStringExtra("Nombres");
        Intent intent1=getIntent();
        String Apellido=intent1.getStringExtra("Apellidos");
        Intent intent2=getIntent();
        String Pid=intent2.getStringExtra("Pid");
        tvPacienteL.setText("Paciente : "+Nombre+"  "+Apellido);
        obtenerCitas(Integer.parseInt(Pid));
        btnECita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LCita_Activity.this,NCita_Activity.class);
                i.putExtra("envEditar","Envio");
                startActivity(i);
            }
        });
    }
    public static ArrayList<ClassCboModel> lstCitas1 = new ArrayList<ClassCboModel>();
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
                    ArrayList<ClassCboModel> lstCitas1 = new ArrayList<ClassCboModel>();
                    lstCitas1.add(new ClassCboModel("0","Seleccionar"));
                    for(int i=0;i<arr.length();i++){
                        JSONObject objCitas=arr.getJSONObject(i);
                        lstCitas1.add(new ClassCboModel(objCitas.getString("Pid"), objCitas.getString("Cid")));
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
    public void cargarSpinnerCitas(ArrayList<ClassCboModel>lstCitas2){
        lstCitas1.clear();
        lstCitas1=lstCitas2;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
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
    }
}