package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.TimeZone;

import Entidades.Citas;
import Entidades.ClassCboModel;
import Entidades.util;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class NuevaCitaActivity extends AppCompatActivity {
private Button  btnGuardarC;
private TextView tvTitulo;
private EditText edtPaciente,edtComentario;
private TextInputLayout txtPaciente,txtInputEstado,txtInputTipoHorario,txtInputTipoEspecialidadR,txtInputMedicoDisponibleR,txtInputComentario,txtFAtencionN;
private static String idhorario,idespecialidad,idmedico,estado,citaid;
Citas citaUpdate=new Citas();
boolean isCboHorario=false;
boolean isCboEstado=false;
boolean isCboEspecialidad=false;
boolean isCboMedico=false;
private AutoCompleteTextView edtFAtencionN,spTHorario,spTEspecialidad,spMDisponible,spEstado;
    private static String URL_BASE="https://appcolegiophp.herokuapp.com";
    private static String OBTENER_HORARIO="/obtenerHorario.php";
    private static String OBTENER_ESPECIALIDAD="/obtenerEspecialidad.php";
    private static String OBTENER_MEDICOS="/obtenerMedicos.php?";
    public static String REGSISTRAR_CITA="/registrarCita.php";
    public static String ACTUALIZAR_CITA="/actualizarCita.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);
        this.init();
        Intent intent=getIntent();
        String idCitaCbo = intent.getStringExtra("envIdCbo");
        Log.i("tags",idCitaCbo!=null?idCitaCbo:"sin idcita XD");
        spEstado.setEnabled(false);
        txtInputEstado.setVisibility(View.INVISIBLE);
        spEstado.setVisibility(View.INVISIBLE);
        edtPaciente.setText("Paciente:  "+util.nombre+"  "+util.apellido);
        obtenerHorarios();
        obtenerEspecialidad();
        cargarSpinnerEstdo();
        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spEstado.toString();
                Log.i("nameValueCboEstados",nameValueCbo);
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
                String nameValueCbo=spTHorario.toString();
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
                        idhorario= objeto.getId();
                        return;
                    }
                }
                if (isSelected) {
                    spTEspecialidad.setEnabled(true);
                    String selectEspecialidad=spTEspecialidad!=null?spTEspecialidad.toString():"";
                    if (!(selectEspecialidad.isEmpty()) && !isCboHorario && !(selectEspecialidad.equals("Seleccione"))) {

                        for(ClassCboModel objeto : util.lstEspecialidad){

                            if (!(objeto.valor.equals("Seleccione")) && objeto.getValor().equals(selectEspecialidad)) {

                                idespecialidad=objeto.getId();
                                spTEspecialidad.setSelection(util.lstEspecialidad.indexOf(objeto));
                                isCboMedico=true;
                                return;
                            }
                        }
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
        /*spTEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spTEspecialidad.toString();
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
                        isCboMedico=true;
                        //obtenerMedicos(objeto.getId());
                        //spMDisponible.setEnabled(true);
                        return;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        spMDisponible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nameValueCbo=spMDisponible.toString();
                Log.i("nameValueCboMedico",nameValueCbo);
                for(ClassCboModel objeto : util.lstMedicos){
                    if (!(objeto.valor.equalsIgnoreCase("Seleccione")) && objeto.valor.equals(nameValueCbo)) {

                        idmedico=objeto.getId();
                    }else
                    if (!(objeto.valor.equalsIgnoreCase("Seleccione")) && isCboMedico && objeto.getId().equalsIgnoreCase(citaUpdate.getMedicoId())) {
                        Log.i("medico encontrado",objeto.toString());
                        isCboMedico=false;
                        spMDisponible.setEnabled(true);
                        spMDisponible.setSelection(util.lstMedicos.indexOf(objeto));

                        idmedico=objeto.getId();
                        return;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                    tvTitulo.setText("Actualizacion de Citas");
                    btnGuardarC.setText("Actualizar");
                }
            }

        }
    }
    private void init() {
        //Button
        btnGuardarC = findViewById(R.id.btnGuardarC);
        //EditText
        edtPaciente = findViewById(R.id.tvPaciente);
        edtComentario = findViewById(R.id.txtComentario);
        edtFAtencionN = findViewById(R.id.txtFAtencionN);
        //InputLayout
        txtPaciente = findViewById(R.id.TIlPaciente);
        txtInputEstado = findViewById(R.id.txtInputEstado);
        txtInputTipoHorario = findViewById(R.id.txtInputTipoHorario);
        txtInputTipoEspecialidadR = findViewById(R.id.txtInputTipoEspecialidadR);
        txtInputMedicoDisponibleR = findViewById(R.id.txtInputMedicoDisponibleR);
        txtInputComentario = findViewById(R.id.txtInputComentario);
        txtFAtencionN = findViewById(R.id.txtInputFechaAtencion);
        ;
        //AutoCompleteTextView
        spEstado = findViewById(R.id.spEstado);
        spTHorario = findViewById(R.id.cboTHorario);
        spTEspecialidad = findViewById(R.id.cboTEspecialidad);
        spMDisponible = findViewById(R.id.cboMDisponible);
        //TextView
        tvTitulo = findViewById(R.id.tvTitulo);
        btnGuardarC.setOnClickListener(v->
                this.btnGuardarC.setOnClickListener(new View.OnClickListener() {
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
                }));
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().
        setTitleText("Seleccione Fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        edtFAtencionN.setOnClickListener(v -> {
            datePicker.show(getSupportFragmentManager(), "Material_Date_picker");
            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = format.format(calendar.getTime());
                    edtFAtencionN.setText(formattedDate);
                }
            });
        });
        edtPaciente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spTEspecialidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameValueCbo=spTEspecialidad.getText().toString();
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
                        isCboMedico=true;
                        //obtenerMedicos(objeto.getId());
                        //spMDisponible.setEnabled(true);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtComentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtFAtencionN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spEstado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spTHorario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("mensaje","123");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spTEspecialidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spMDisponible.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private boolean validar() {
        boolean retorno = true;
        String Thorario, Especialidad, Medico, observacion, FechaA, Estado;
        Thorario = spTHorario.getOnItemSelectedListener().toString();
        Especialidad = spTEspecialidad.getOnItemSelectedListener().toString();
        Medico = spMDisponible.getOnItemSelectedListener().toString();
        observacion = edtComentario.getText().toString();
        FechaA = edtFAtencionN.getText().toString();
        Estado = spEstado.getOnItemSelectedListener().toString();
        if (Thorario.isEmpty()){
            txtInputTipoHorario.setError("Ingresar Tipo de Horario");
            retorno=false;
        }else{
            txtInputTipoHorario.setErrorEnabled(false);
        }if (Especialidad.isEmpty()){
            txtInputTipoEspecialidadR.setError("Ingresar Especialidad");
            retorno=false;
        }else{
            txtInputTipoEspecialidadR.setErrorEnabled(false);
        }if(Medico.isEmpty()){
            txtInputMedicoDisponibleR.setError("Ingresar Medico");
            retorno=false;
        }else{
            txtInputMedicoDisponibleR.setErrorEnabled(false);
        }if(observacion.isEmpty()){
            txtInputComentario.setError("Ingresar Comentario");
            retorno=false;
        }else{
            txtInputComentario.setErrorEnabled(false);
        }if (FechaA.isEmpty()){
            txtFAtencionN.setError("Ingresar Fecha Atencion");
            retorno=false;
        }else{
            txtFAtencionN.setErrorEnabled(false);
        }if(Estado.isEmpty()){
            txtInputEstado.setError("Ingresar Estado");
            retorno=false;
        }else{
            txtInputEstado.setErrorEnabled(false);
        }
        return retorno;
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
                        Intent i = new Intent(NuevaCitaActivity.this,Menu_Activity.class);
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
                        Intent i = new Intent(NuevaCitaActivity.this,Menu_Activity.class);
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
    public void mensajeToast(String texto){
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        texto, Toast.LENGTH_SHORT);
        toast1.show();
    }
    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("Ok").show();
    }
    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
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