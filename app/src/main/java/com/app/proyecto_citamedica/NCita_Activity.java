package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Entidades.ClassCboModel;
import Entidades.Horarios;
import Entidades.util;

public class NCita_Activity extends AppCompatActivity {
EditText edtPaciente,edtComentario;
public static String URL_Registro_Cita="";
Spinner spTHorario,spTEspecialidad,spMDisponible,spEstado;
Button btnGuardarC;
TextView tvEstado,tvPaciente;

private static String URL_BASE="https://appcolegiophp.herokuapp.com";
    private static String OBTENER_HORARIO="/obtenerHorario.php";
    private static String OBTENER_ESPECIALIDAD="/obtenerEspecialidad.php";
    private static String OBTENER_MEDICOS="/obtenerMedicos.php?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncita);
        //hola test cambio
        inicializar();
        Intent intent=getIntent();
        String Nombre=intent.getStringExtra("Nombres");
        Intent intent1=getIntent();
        String Apellido=intent1.getStringExtra("Apellidos");
        Intent intent2=getIntent();
        String Pid=intent2.getStringExtra("Pid");
        spEstado.setEnabled(false);
        tvEstado.setVisibility(View.INVISIBLE);
        spEstado.setVisibility(View.INVISIBLE);
        ActivarSpEstado();
        tvPaciente.setText("Paciente:  "+Nombre+"  "+Apellido);
        obtenerHorarios();
        obtenerEspecialidad();
        obtenerMedicos("4");

        spTHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spTHorario.getSelectedItem().toString();
                Log.i("nameValueCbo",nameValueCbo);
                for(ClassCboModel objeto : util.lsthorarios){
                    if (objeto.valor.equals(nameValueCbo)) {
                        spTEspecialidad.setEnabled(true);
                        spMDisponible.setEnabled(true);
                    }else{
                        spTEspecialidad.setEnabled(false);
                        spMDisponible.setEnabled(false);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spTHorario.getSelectedItem().toString();
                Log.i("nameValueCbo",nameValueCbo);
                for(ClassCboModel objeto : util.lsthorarios){
                    if (objeto != null && !(objeto.valor.equals(nameValueCbo))) {
                        spMDisponible.setEnabled(false);
                    }else{
                        obtenerMedicos(objeto.getId());
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void ActivarSpEstado(){

        Intent intent=getIntent();
        String mensaje=intent.getStringExtra("envEditar");
        if (mensaje!=null && mensaje.equals("Envio")){
            spEstado.setEnabled(true);
            spEstado.setVisibility(View.VISIBLE);
            tvEstado.setVisibility(View.VISIBLE);
        }
    }

    public void inicializar(){
        tvEstado=findViewById(R.id.tvEstado);
        spEstado=findViewById(R.id.spEstado);
        btnGuardarC=findViewById(R.id.btnGuardarC);
        tvPaciente=findViewById(R.id.tvPaciente);
        //edtPaciente=
        edtComentario=findViewById(R.id.txtComentario);
        spTEspecialidad=findViewById(R.id.cboTEspecialidad);
        spTHorario=findViewById(R.id.cboTHorario);
        spMDisponible=findViewById(R.id.cboMDisponible);
        spTEspecialidad.setEnabled(false);
        spMDisponible.setEnabled(false);
    }

    private void obtenerHorarios(){
        Log.i("obteniendo alumnos","service");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_BASE+OBTENER_HORARIO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject objSon = new JSONObject(response);

                    JSONArray arr = objSon.getJSONArray("data");
                     ArrayList<ClassCboModel> lstModelCbo = new ArrayList<ClassCboModel>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject horarioObject=arr.getJSONObject(i);
                        lstModelCbo.add(new ClassCboModel(horarioObject.getString("Hid"),horarioObject.getString("tipoHorario")));

                    }
                    cargarSpinnerhorario(lstModelCbo);

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

    private void obtenerEspecialidad(){
        Log.i("obteniendo especialidades","service");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_BASE+OBTENER_ESPECIALIDAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objSon = new JSONObject(response);
                    JSONArray arr = objSon.getJSONArray("data");
                    ArrayList<ClassCboModel> lstModelCbo = new ArrayList<ClassCboModel>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject horarioObject=arr.getJSONObject(i);
                        lstModelCbo.add(new ClassCboModel(horarioObject.getString("Eid"),horarioObject.getString("nombre")));
                    }
                    cargarSpinnerEspecialidad(lstModelCbo);

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

    private void obtenerMedicos(String especialidadId){
        Log.i("obteniendo MEDICOS","service");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_BASE+OBTENER_MEDICOS+especialidadId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objSon = new JSONObject(response);

                    JSONArray arr = objSon.getJSONArray("data");
                    ArrayList<ClassCboModel> lstModelCbo = new ArrayList<ClassCboModel>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject horarioObject=arr.getJSONObject(i);
                        lstModelCbo.add(new ClassCboModel(horarioObject.getString("Mid"),horarioObject.getString("nombres")));

                    }
                    cargarSpinnerMedicos(lstModelCbo);

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


    public void mensajeToast(String texto){
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        texto, Toast.LENGTH_SHORT);
        toast1.show();
    }
    public void cargarSpinnerEspecialidad(ArrayList<ClassCboModel> lstModelCbo){
        util.lstEspecialidad.clear();
        util.lstEspecialidad=lstModelCbo;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstModelCbo);
        spTEspecialidad.setAdapter(modelo);
    }
    public void cargarSpinnerMedicos(ArrayList<ClassCboModel> lstModelCbo) {
        util.lstMedicos.clear();
        util.lstMedicos = lstModelCbo;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstModelCbo);
        spMDisponible.setAdapter(modelo);
    }
    public void cargarSpinnerhorario(ArrayList<ClassCboModel> lstModelCbo){
        util.lsthorarios.clear();
        util.lsthorarios=lstModelCbo;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstModelCbo);
        spTHorario.setAdapter(modelo);
    }

    }

