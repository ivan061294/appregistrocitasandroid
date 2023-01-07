package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.CredentialProtectedWhileLockedViolation;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NPaciente_Activity extends AppCompatActivity {
EditText edtDni,edtNombre,edtApellido,edtContrasena,edtDirreccion,edtTelefono,edtFNacimiento;
public static String URL_Registro_Paciente="https://appcolegiophp.herokuapp.com/registrar.php";
Spinner spSexo;
    String[]lstSexo={"Seleccionar","Masculino","Femenino"};
Button btnGuardarP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npaciente);
        inicializar();
        getSexo();
        btnGuardarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarP();
            }
        });
    }
    public void RegistrarP(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_Registro_Paciente, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if (success.equals("1")) {
                        Toast.makeText(NPaciente_Activity.this, message, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NPaciente_Activity.this,MainActivity.class);
                        startActivity(i);
                    }
                } catch (JSONException ex) {
                    Toast.makeText(NPaciente_Activity.this, "Error"+ex.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("error","Llego aca catch");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NPaciente_Activity.this,"Error"+ error.toString(),Toast.LENGTH_SHORT).show();
                Log.i("error","Llego aca Response");
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                    param.put("nombres",edtNombre.getText().toString());
                    param.put("apellidos",edtApellido.getText().toString());
                    param.put("direccion",edtDirreccion.getText().toString());
                    param.put("contrasena",edtContrasena.getText().toString());
                    param.put("telefono",edtTelefono.getText().toString());
                    param.put("dni",edtDni.getText().toString());
                    param.put("sexo",spSexo.getSelectedItem().toString());
                    param.put("fechaNacimiento",edtFNacimiento.getText().toString());
                    return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void getSexo(){
        spSexo.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lstSexo));
    }
    public void inicializar(){
        edtDni=findViewById(R.id.txtDNI);
        edtNombre=findViewById(R.id.txtNombre);
        edtApellido=findViewById(R.id.txtApellido);
        edtContrasena=findViewById(R.id.txtContrasena);
        edtDirreccion=findViewById(R.id.txtDireccion);
        edtTelefono=findViewById(R.id.txtTelefono);
        edtFNacimiento=findViewById(R.id.txtFNacimiento);
        spSexo=findViewById(R.id.cboSexo);
        btnGuardarP=findViewById(R.id.btnGuardarP);
    }
}