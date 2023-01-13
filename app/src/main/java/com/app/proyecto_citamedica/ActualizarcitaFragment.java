package com.app.proyecto_citamedica;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import Entidades.Citas;
import Entidades.ClassCboModel;
import Entidades.util;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class ActualizarcitaFragment extends Fragment {
    private Button btnGuardarC;
    private AutoCompleteTextView dropdownEspecialidad,dropdownHorario,dropdownDisponible,dropdownEstados;
    private EditText edtComentario,edtFAtencion,edtPaciente;
    private TextInputLayout txtInputTipoHorario,txtInputTipoEspecialidad,txtInputMedicoDisponible,txtInputComentario,txtInputFAtencion,txtInputEstado;
    private static String idhorario,idespecialidad,idmedico,estado,citaid;
    private static String URL_BASE="https://appcolegiophp.herokuapp.com";
    private static String OBTENER_HORARIO="/obtenerHorario.php";
    private static String OBTENER_ESPECIALIDAD="/obtenerEspecialidad.php";
    private static String OBTENER_MEDICOS="/obtenerMedicos.php?";
    public static String REGSISTRAR_CITA="/registrarCita.php";
    public static String ACTUALIZAR_CITA="/actualizarCita.php";

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            setearConponentes(datosRecuperados.getString("citasdrown"));
            obtenerHorarios();
            obtenerEspecialidad();
            cargarSpinnerEstdo();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_actualizarcita, container, false);
    }
    public void setearConponentes(String idCitaCbo){

        if (idCitaCbo != null) {
            for (Citas cita :util.lstCitas1){
                if (cita.getcIdCbo().equalsIgnoreCase(idCitaCbo)) {
                    Log.i("cita encontrada!!",cita.toString2());
                    citaid=cita.getcId();
                    edtComentario.setText(cita.getObservaciones());
                    edtFAtencion.setText(cita.getFechaAtencion());
                    dropdownEstados.setText(cita.getEstado());
                    dropdownHorario.setText(cita.getNomHorario());
                    dropdownEspecialidad.setText(cita.getNomEspecialidad());
                    dropdownDisponible.setText(cita.getNomMedico());
                    obtenerMedicos(cita.getEspecialidadId());
                    idhorario=cita.getHorarioId();
                    idespecialidad=cita.getEspecialidadId();
                    idmedico=cita.getMedicoId();
                    estado=cita.getEstado();
                    dropdownEspecialidad.setEnabled(true);
                    dropdownDisponible.setEnabled(true);

                }
            }

        }
    }

    private void init(View v){
        dropdownEspecialidad = v.findViewById(R.id.dropdownEspecialidad);
        dropdownHorario = v.findViewById(R.id.dropdownHorario);
        dropdownDisponible = v.findViewById(R.id.dropdownDisponible);
        dropdownEstados = v.findViewById(R.id.dropdownEstados);
        btnGuardarC = v.findViewById(R.id.btnGuardarC);
        edtComentario = v.findViewById(R.id.edtComentario);
        edtFAtencion = v.findViewById(R.id.edtFAtencionU);
        txtInputTipoHorario = v.findViewById(R.id.txtInputTipoHorario);
        txtInputTipoEspecialidad = v.findViewById(R.id.txtInputTipoEspecialidad);
        txtInputMedicoDisponible = v.findViewById(R.id.txtInputMedicoDisponible);
        txtInputFAtencion =v.findViewById(R.id.txtInputFechaAtencionU);
        txtInputComentario = v.findViewById(R.id.txtInputComentario);
        txtInputEstado = v.findViewById(R.id.txtInputTipoEstado);
        edtPaciente=v.findViewById(R.id.edtPaciente);

        edtPaciente.setEnabled(false);

        edtPaciente.setText(util.nombre+" "+util.apellido );
        btnGuardarC.setOnClickListener(vv -> {
            if (validar()) {
                Citas cita = new Citas();
                cita.setcId(citaid);
                cita.setHorarioId(idhorario);
                cita.setEspecialidadId(idespecialidad);
                cita.setMedicoId(idmedico);
                cita.setObservaciones(edtComentario.getText().toString());
                cita.setFechaAtencion(edtFAtencion.getText().toString());
                cita.setPacienteId(util.pId);
                cita.setUsuarioCreacion(util.nombre);
                cita.setEstado(estado);
                Log.i("citas update",cita.toString2());
                Actualizarrcita(cita);

            }else{
                errorMessage("Por favor complete los campos!");
            }
        });

        dropdownEstados.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                estado=dropdownEstados.getText().toString();
                txtInputEstado.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownHorario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameValueCbo=dropdownHorario.getText().toString();
                Log.i("nameValueCbo",nameValueCbo);
                boolean isSelected=false;
                for(ClassCboModel objeto : util.lsthorarios){
                    Log.i("Horiofor",objeto.toString());
                    Log.i("namevalucbo",nameValueCbo);
                    if (!(objeto.valor.equals("Seleccione")) && objeto.valor.equals(nameValueCbo)) {
                        isSelected=true;
                        idhorario= objeto.getId();
                        txtInputTipoHorario.setErrorEnabled(false);
                    }

                }
                if (isSelected) {
                    dropdownEspecialidad.setEnabled(true);
                    String selectEspecialidad=dropdownEspecialidad.getText().toString();
                    if (!(selectEspecialidad.isEmpty())  && !(selectEspecialidad.equals("Seleccione"))) {

                        for(ClassCboModel objeto : util.lstEspecialidad){

                            if (!(objeto.valor.equals("Seleccione")) && objeto.getValor().equals(selectEspecialidad)) {

                                idespecialidad= objeto.getId();

                                return;
                            }
                        }
                    }

                }else{
                    dropdownEspecialidad.setEnabled(false);
                    dropdownDisponible.setEnabled(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownEspecialidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameValueCbo=dropdownEspecialidad.getText().toString();
                Log.i("nameValueCboespecialidadS",nameValueCbo);
                dropdownDisponible.setEnabled(false);
                for(ClassCboModel objeto : util.lstEspecialidad){
                    if (!(objeto.valor.equals("Seleccione")) && objeto.valor.equals(nameValueCbo)) {
                        idespecialidad= objeto.getId();
                        dropdownDisponible.setText("");
                        obtenerMedicos(objeto.getId());
                        dropdownDisponible.setEnabled(true);
                        txtInputTipoEspecialidad.setErrorEnabled(false);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownDisponible.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameValueCbo=dropdownDisponible.getText().toString();
                Log.i("nameValueCboMedico",nameValueCbo);
                for(ClassCboModel objeto : util.lstMedicos){
                    if (!(objeto.valor.equalsIgnoreCase("Seleccione")) && objeto.valor.equals(nameValueCbo)) {
                        idmedico= objeto.getId();
                        txtInputMedicoDisponible.setErrorEnabled(false);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtComentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputComentario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().
                setTitleText("Seleccione Fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        edtFAtencion.setOnClickListener(vv->{
            Dialog dialog= datePicker.getDialog();
            if (!(dialog!=null&&dialog.isShowing())) {
                datePicker.show(getActivity().getSupportFragmentManager(), "Material_Date_picker20");
            }
            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate  = format.format(calendar.getTime());
                    edtFAtencion.setText(formattedDate);
                    txtInputFAtencion.setErrorEnabled(false);

                }
            });

        });


    }
    private boolean validar() {
        boolean retorno = true;
        String horario,especialidad,mDisponible,comentario,fAtencion;

        horario = dropdownHorario.getText().toString();
        especialidad = dropdownEspecialidad.getText().toString();
        mDisponible = dropdownDisponible.getText().toString();
        comentario = edtComentario.getText().toString();
        fAtencion = edtFAtencion.getText().toString();

        if (horario.isEmpty()||horario.equalsIgnoreCase("")) {
            txtInputTipoHorario.setError("Seleccione un horario");
            retorno = false;
        } else {
            txtInputTipoHorario.setErrorEnabled(false);
        }if (especialidad.isEmpty()||especialidad.equalsIgnoreCase("")) {
            txtInputTipoEspecialidad.setError("Seleccione una Especialidad");
            retorno = false;
        } else {
            txtInputTipoEspecialidad.setErrorEnabled(false);
        }
        if (mDisponible.isEmpty()||mDisponible.equalsIgnoreCase("")) {
            txtInputMedicoDisponible.setError("Seleccione un Medico ");
            retorno = false;
        } else {
            txtInputMedicoDisponible.setErrorEnabled(false);
        }
        if (comentario.isEmpty()||comentario.equalsIgnoreCase("")) {
            txtInputComentario.setError("Escriba un Comentario");
            retorno = false;
        } else {
            txtInputComentario.setErrorEnabled(false);
        }
        if (fAtencion.isEmpty()) {
            txtInputFAtencion.setError("Seleccione una Fecha de Atencion");
            retorno = false;
        } else {
            txtInputFAtencion.setErrorEnabled(false);
        }
        return retorno;
    }
    public void successMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }
    public void errorMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }
    private void obtenerHorarios(){
        Log.i("obteniendo horarios","service");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                        lstModelCbo.add(new ClassCboModel(horarioObject.getString("Mid"),
                                horarioObject.getString("nombres")+" "+horarioObject.getString("apellidos")));

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

    public void Actualizarrcita(Citas cita){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_BASE+ACTUALIZAR_CITA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")) {
                        successMessage(message);
                        inicioapp fragmentcita = new inicioapp();
                        FragmentTransaction fragmentTransaction;
                        FragmentManager fragmentManager;
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.nav_host_fragment_content_inicio, fragmentcita)
                                .addToBackStack(null)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

                    }
                } catch (JSONException ex) {
                    errorMessage("Error en Registrar");
                    Log.i("error",ex.toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorMessage("Error en Servicio Externo");
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
                param.put("Cid",cita.getcId());
                param.put("estado",cita.getEstado());
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    public void cargarSpinnerEspecialidad(ArrayList<ClassCboModel> lstModelCbo){
        util.lstEspecialidad.clear();
        util.lstEspecialidad=lstModelCbo;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(getContext(), R.layout.dropdown_item,
                lstModelCbo);
        dropdownEspecialidad.setAdapter(modelo);
    }
    public void cargarSpinnerMedicos(ArrayList<ClassCboModel> lstModelCbo) {
        util.lstMedicos.clear();
        util.lstMedicos = lstModelCbo;
        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(getContext(), R.layout.dropdown_item,
                lstModelCbo);
        dropdownDisponible.setAdapter(modelo);
    }
    public void cargarSpinnerhorario(ArrayList<ClassCboModel> lstModelCbo){
        util.lsthorarios.clear();
        util.lsthorarios=lstModelCbo;

        ArrayAdapter<ClassCboModel> modelo = new ArrayAdapter<ClassCboModel>(getContext(), R.layout.dropdown_item,
                lstModelCbo);
        dropdownHorario.setAdapter(modelo);
    }
    public void cargarSpinnerEstdo(){
        util.lstEstados.clear();
        ArrayList<ClassCboModel> lstestados = new ArrayList<>();
        lstestados.add(new ClassCboModel("01","Pendiente"));
        lstestados.add(new ClassCboModel("02","Terminado"));
        lstestados.add(new ClassCboModel("03","Rechazado"));
        util.lstEstados=lstestados;
        ArrayAdapter<ClassCboModel> modelo2 = new ArrayAdapter<ClassCboModel>(getContext(), R.layout.dropdown_item,
                lstestados);
        dropdownEstados.setAdapter(modelo2);
    }

}