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

import Entidades.Citas;
import Entidades.ClassCboModel;
import Entidades.Horarios;
import Entidades.util;

public class NCita_Activity extends AppCompatActivity {
EditText edtPaciente,edtComentario,edtFAtencionN;

Spinner spTHorario,spTEspecialidad,spMDisponible,spEstado;
Button btnGuardarC;
TextView tvEstado,tvPaciente,tvTitulo;
private static String idhorario,idespecialidad,idmedico,estado,citaid;

private static String URL_BASE="https://appcolegiophp.herokuapp.com";
    private static String OBTENER_HORARIO="/obtenerHorario.php";
    private static String OBTENER_ESPECIALIDAD="/obtenerEspecialidad.php";
    private static String OBTENER_MEDICOS="/obtenerMedicos.php?";
    public static String REGSISTRAR_CITA="/registrarCita.php";
    public static String ACTUALIZAR_CITA="/actualizarCita.php";
    Citas citaUpdate = new Citas();
    boolean isCboHorario=false;
    boolean isCboEstado=false;
    boolean isCboEspecialidad=false;
    boolean isCboMedico=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ncita);
        //hola test cambio
        //cambio
        inicializar();
        Intent intent=getIntent();
        String idCitaCbo = intent.getStringExtra("envIdCbo");
        Log.i("tags",idCitaCbo!=null?idCitaCbo:"sin idcita XD");
        //bolleanos para update


        //String pacienteId=util.pId;
        spEstado.setEnabled(false);
        tvEstado.setVisibility(View.INVISIBLE);
        spEstado.setVisibility(View.INVISIBLE);
        ActivarSpEstado();
        tvPaciente.setText("Paciente:  "+util.nombre+"  "+util.apellido);
        obtenerHorarios();
        obtenerEspecialidad();
        cargarSpinnerEstdo();

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spEstado.getSelectedItem().toString();
                Log.i("nameValueCboMedico",nameValueCbo);
                for(ClassCboModel objeto : util.lstEstados){
                    if (!(objeto.valor.equals("Seleccione")) && objeto.getValor().equals(nameValueCbo)) {
                        estado=objeto.getValor();
                    }else
                    if (!(objeto.valor.equals("Seleccione")) && isCboEstado && objeto.getValor().equalsIgnoreCase(citaUpdate.getEstado())) {
                        isCboEstado=false;
                        spEstado.setSelection(util.lstEstados.indexOf(objeto));
                        spEstado.setEnabled(true);
                        estado=objeto.getValor();
                        return;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spTHorario.getSelectedItem().toString();
                Log.i("nameValueCbo",nameValueCbo);
                boolean isSelected=false;
                idhorario="";
                for(ClassCboModel objeto : util.lsthorarios){
                    Log.i("Horiofor",objeto.toString());
                    Log.i("namevalucbo",nameValueCbo);
                    if (!(objeto.valor.equals("Seleccione")) && objeto.valor.equals(nameValueCbo)) {
                        isSelected=true;
                        idhorario= objeto.getId();
                    }else
                    if (!(objeto.valor.equals("Seleccione")) && isCboHorario && objeto.getId().equals(citaUpdate.getHorarioId())) {
                        isCboHorario=false;
                        spTHorario.setSelection(util.lsthorarios.indexOf(objeto));
                        isSelected=true;
                        idhorario= objeto.getId();
                    }
                }
                if (isSelected) {
                    spTEspecialidad.setEnabled(true);
                    String selectEspecialidad=spTEspecialidad.getSelectedItem()!=null?spTEspecialidad.getSelectedItem().toString():"";
                    if (!(selectEspecialidad.isEmpty()) && !(selectEspecialidad.equals("Seleccione"))) {
                        idespecialidad=obtenerIdEspecialidad(selectEspecialidad);
                        obtenerMedicos(obtenerIdEspecialidad(selectEspecialidad));
                        spMDisponible.setEnabled(true);
                    }
                }else{
                    idespecialidad="";
                    spTEspecialidad.setEnabled(false);
                    spMDisponible.setEnabled(false);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spTEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spTEspecialidad.getSelectedItem().toString();
                Log.i("nameValueCboespecialidadS",nameValueCbo);
                spMDisponible.setEnabled(false);
                idespecialidad="";
                for(ClassCboModel objeto : util.lstEspecialidad){
                    if (!(objeto.valor.equals("Seleccione")) && objeto.valor.equals(nameValueCbo)) {
                        idespecialidad=objeto.getId();
                        obtenerMedicos(objeto.getId());
                        spMDisponible.setEnabled(true);
                    }else
                    if (!(objeto.valor.equals("Seleccione")) && isCboEspecialidad && objeto.getId().equals(citaUpdate.getEspecialidadId())) {
                        isCboEspecialidad=false;
                        idespecialidad=objeto.getId();
                        spTEspecialidad.setSelection(util.lstEspecialidad.indexOf(objeto));
                        obtenerMedicos(objeto.getId());
                        spMDisponible.setEnabled(true);
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMDisponible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spMDisponible.getSelectedItem().toString();
                Log.i("nameValueCboMedico",nameValueCbo);
                for(ClassCboModel objeto : util.lstMedicos){
                    if (!(objeto.valor.equals("Seleccione")) && objeto.valor.equals(nameValueCbo)) {

                        idmedico=objeto.getId();
                    }else
                    if (!(objeto.valor.equals("Seleccione")) && isCboMedico && objeto.getId().equals(citaUpdate.getMedicoId())) {
                        isCboMedico=false;
                        spMDisponible.setSelection(util.lstMedicos.indexOf(objeto));
                        spMDisponible.setEnabled(true);
                        idmedico=objeto.getId();
                        return;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnGuardarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Citas cita = new Citas();
                cita.setHorarioId(idhorario);
                cita.setEspecialidadId(idespecialidad);
                cita.setMedicoId(idmedico);
                cita.setObservaciones(edtComentario.getText().toString());
                cita.setFechaAtencion(edtFAtencionN.getText().toString());
                cita.setPacienteId(util.pId);
                cita.setUsuarioCreacion(util.nombre);
                cita.setEstado(estado);
                if(btnGuardarC.getText().toString().equalsIgnoreCase("Registrar")){
                    Registrarcita(cita);
                }
                if(btnGuardarC.getText().toString().equalsIgnoreCase("Actualizar")){
                    Actualizarrcita(cita);
                }
            }
        });
        setearConponentes(idCitaCbo);
    }
    public void setearConponentes(String idCitaCbo){

        if (idCitaCbo != null) {
            for (Citas cita :util.lstCitas1){
                if (cita.getcIdCbo().equalsIgnoreCase(idCitaCbo)) {
                    Log.i("cita encontrada!!",cita.toString2());
                    citaUpdate=cita;
                    edtComentario.setText(cita.getObservaciones());
                    edtFAtencionN.setText(cita.getFechaAtencion());
                    isCboEstado=true;
                    isCboHorario=true;
                    isCboEspecialidad=true;
                    isCboMedico=true;
                    tvTitulo.setText("Actualizacion de Citas");
                    btnGuardarC.setText("Actualizar");
                }
            }

        }
    }

    public void Registrarcita(Citas cita){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_BASE+REGSISTRAR_CITA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")) {
                        mensajeToast(message);
                        Intent i = new Intent(NCita_Activity.this,Menu_Activity.class);
                        startActivity(i);
                    }
                } catch (JSONException ex) {
                    mensajeToast("Error en Registrar");
                    Log.i("error",ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error en Servicio Externo");
                Log.i("error",error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("Mid",cita.getMedicoId());
                param.put("Pid",cita.getPacienteId());
                param.put("Eid",cita.getEspecialidadId());
                param.put("FechaAtencion",cita.getFechaAtencion());
                param.put("Hid",cita.getHorarioId());
                param.put("observaciones",cita.getObservaciones());
                param.put("usuarioRegistro",cita.getUsuarioCreacion());
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void Actualizarrcita(Citas cita){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_BASE+ACTUALIZAR_CITA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")) {
                        mensajeToast(message);
                        Intent i = new Intent(NCita_Activity.this,Menu_Activity.class);
                        startActivity(i);
                    }
                } catch (JSONException ex) {
                    mensajeToast("Error en Registrar");
                    Log.i("error",ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mensajeToast("Error en Servicio Externo");
                Log.i("error",error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("Mid",cita.getMedicoId());
                param.put("Pid",cita.getPacienteId());
                param.put("Eid",cita.getEspecialidadId());
                param.put("FechaAtencion",cita.getFechaAtencion());
                param.put("Hid",cita.getHorarioId());
                param.put("observaciones",cita.getObservaciones());
                param.put("usuarioRegistro",cita.getUsuarioCreacion());
                param.put("Cid",citaUpdate.getcId());
                param.put("estado",cita.getEstado());
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String obtenerIdEspecialidad(String nombreEspecialidad){
        String idEspecialidad="";
        for(ClassCboModel objeto : util.lstEspecialidad){

            if (!(objeto.getValor().equals("Seleccione")) && objeto.getValor().equals(nombreEspecialidad)) {
                idEspecialidad= objeto.getId();
            }
        }
        return idEspecialidad;
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
        edtFAtencionN=findViewById(R.id.txtFAtencionN);
        edtComentario=findViewById(R.id.txtComentario);
        spTEspecialidad=findViewById(R.id.cboTEspecialidad);
        spTHorario=findViewById(R.id.cboTHorario);
        spMDisponible=findViewById(R.id.cboMDisponible);
        spTEspecialidad.setEnabled(false);
        spMDisponible.setEnabled(false);
        tvTitulo=findViewById(R.id.tvTitulo);
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
                    lstModelCbo.add(new ClassCboModel("0","Seleccione"));
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
                    lstModelCbo.add(new ClassCboModel("0","Seleccione"));
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
                    lstModelCbo.add(new ClassCboModel("0","Seleccione"));
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
    public void cargarSpinnerEstdo(){
        util.lstEstados.clear();
        ArrayList<ClassCboModel> lstHorario = new ArrayList<ClassCboModel>();
        lstHorario.add(new ClassCboModel("1","Pendiente"));
        lstHorario.add(new ClassCboModel("2","Terminado"));
        lstHorario.add(new ClassCboModel("3","Rechazado"));
        util.lstEstados=lstHorario;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstHorario);
        spEstado.setAdapter(modelo);
    }


    }

