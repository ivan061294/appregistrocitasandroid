package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

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
        tvPacienteL.setText(Nombre+"  "+Apellido);
        BuscarCita(Pid);
        btnECita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LCita_Activity.this,NCita_Activity.class);
                i.putExtra("envEditar","Envio");
                startActivity(i);
            }
        });
    }
    public void BuscarCita(String InputCodigo){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL_LCitas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("idpaciente");
                        int descripcion=jsonObject.getInt("Descripcion");
                        Log.i("ONRESPONSE",":"+id);
                        spCita.setId(id);
                        spCita.setSelection(descripcion);
                    }
                } catch (JSONException e) {
                    Toast.makeText(LCita_Activity.this,"Error"+e.toString(),Toast.LENGTH_SHORT).show();
                    Log.i("ONERROR","json ex:" +e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LCita_Activity.this, "ERROR"+error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("ONERROR","json ex:"+error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("Cid",InputCodigo);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public void inicializar(){
        tvPacienteL=findViewById(R.id.txtPacienteL);
        btnECita=findViewById(R.id.btnEditarL);
        edtTHorario=findViewById(R.id.txtTHorario);
        edtEspecialidad=findViewById(R.id.txtTEspecialidad);
        edtMDisponible=findViewById(R.id.txtMDisponible);
        edtFAtencion=findViewById(R.id.txtFAtencion);
        spCita=findViewById(R.id.cboCita);
    }
}