package com.app.proyecto_citamedica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Entidades.Citas;
import Entidades.util;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListarCitaFragment extends Fragment {

    private Button btnEditarL;
    private AutoCompleteTextView dropdowncitas;
    private EditText edtPaciente, edtHorario, edtEspecialidad, edtMDisponible, edtFAtencion;
    private TextInputLayout txtInputCita;
    private ScrollView scroolLcitas;
    private final static int LOCATION_REQUEST_CODE = 23;
    public static String URL_LCitas="https://appcolegiophp.herokuapp.com/obtenercitaIdPaciente.php?";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listar_cita, container, false);
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadData();
    }
    private void init(View v){
        dropdowncitas = v.findViewById(R.id.dropdowncitas);
        edtPaciente = v.findViewById(R.id.edtPaciente);
        edtHorario = v.findViewById(R.id.edtHorario);
        edtEspecialidad = v.findViewById(R.id.edtEspecialidad);
        edtMDisponible = v.findViewById(R.id.edtMDisponible);
        edtFAtencion = v.findViewById(R.id.edtFAtencion);
        txtInputCita = v.findViewById(R.id.txtInputCita);
        btnEditarL = v.findViewById(R.id.btnEditarL);
        scroolLcitas= v.findViewById(R.id.scroolLcitas);
        edtPaciente.setEnabled(false);
        edtHorario.setEnabled(false);
        edtEspecialidad.setEnabled(false);
        edtFAtencion.setEnabled(false);
        edtMDisponible.setEnabled(false);
        edtPaciente.setText(util.nombre+" "+util.apellido );
        btnEditarL.setOnClickListener(vv -> {
            if (validar()) {
                scroolLcitas.setVisibility(View.INVISIBLE);
                ActualizarcitaFragment fragmentcita = new ActualizarcitaFragment();
                FragmentTransaction fragmentTransaction;
                FragmentManager fragmentManager;
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.nav_host_fragment_content_inicio, fragmentcita)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

            }else{
                errorMessage("Por favor complete los campos!");
            }
        });
        dropdowncitas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameValueCbo=dropdowncitas.getText().toString();
                Log.i("nameValueCbo",nameValueCbo);
                edtHorario.setText("");
                edtEspecialidad.setText("");
                edtMDisponible.setText("");
                edtFAtencion.setText("");
                //btnECita.setEnabled(false);
                for(Citas objeto :util.lstCitas1){
                    if (objeto.getcIdCbo().equalsIgnoreCase(nameValueCbo)) {
                        txtInputCita.setErrorEnabled(false);
                        Log.i("objeto encontrado",objeto.toString2());
                        edtHorario.setText(objeto.getNomHorario());
                        edtEspecialidad.setText(objeto.getNomEspecialidad());
                        edtMDisponible.setText(objeto.getNomMedico());
                        edtFAtencion.setText(objeto.getFechaAtencion());
                        //btnECita.setEnabled(true);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    public void errorMessage(String message) {
        new SweetAlertDialog(getContext(),
                SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(message).show();
    }
    private boolean validar() {
        boolean retorno = true;
        String citas;

        citas = dropdowncitas.getText().toString();

        if (citas.isEmpty()||citas.equalsIgnoreCase("Seleccione")) {
            txtInputCita.setError("Seleccione una cita");
            retorno = false;
        } else {
            txtInputCita.setErrorEnabled(false);
        }
        return retorno;
    }
    private void loadData(){
        Log.i("Util.pid",util.pId);
        obtenerCitas(Integer.valueOf(util.pId));
    }

    private void obtenerCitas(int PId){
        Log.i("obteniendo CITAS","service");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, URL_LCitas+PId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objSon = new JSONObject(response);
                    JSONArray arr = objSon.getJSONArray("data");
                    ArrayList<Citas> lstCitas1 = new ArrayList<Citas>();
                    Citas cita;
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
        ArrayAdapter<Citas> modelo = new ArrayAdapter<Citas>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                lstCitas2);
        dropdowncitas.setAdapter(modelo);
    }

}