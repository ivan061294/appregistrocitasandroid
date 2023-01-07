package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import Entidades.util;

public class MainActivity extends AppCompatActivity {
Button btnIngresar,btnAgregarUsuario;
EditText edtUsuario,edtPassword;
public static String URL_LOGIN="https://appcolegiophp.herokuapp.com/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicioLogin();
            }
        });
        btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,NPaciente_Activity.class);
                startActivity(i);
            }
        });
    }
    public void servicioLogin(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String Message = jsonObject.getString("message");
                    String Nombres = jsonObject.getString("nombres");
                    String Apellidos = jsonObject.getString("apellidos");
                    String Pid = jsonObject.getString("pid");
                    if (success.equals("1")) {
                        Toast.makeText(MainActivity.this, Message, Toast.LENGTH_SHORT).show();
                        Log.i("response-1", "ID - " + response.toString()+Nombres.toString()+Apellidos.toString()+Pid.toString());
                        Intent i = new Intent(MainActivity.this, Menu_Activity.class);
                        util.nombre=Nombres;
                        util.apellido=Apellidos;
                        util.pId=Pid;
                        i.putExtra("Nombres",Nombres);
                        i.putExtra("Apellidos",Apellidos);
                        i.putExtra("Pid",Pid);
                        startActivity(i);
                    } else {
                        Toast.makeText(MainActivity.this, Message, Toast.LENGTH_SHORT).show();
                        Log.i("response -0", "ID- " + response.toString());
                    }
                } catch (JSONException ex) {
                    Toast.makeText(MainActivity.this, "Error" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this,"Error"+error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("usuario",edtUsuario.getText().toString());
                param.put("password",edtPassword.getText().toString());
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void inicializar(){
        btnIngresar=findViewById(R.id.btnIngresar);
        btnAgregarUsuario=findViewById(R.id.btnAgregarUsuario);
        edtUsuario=findViewById(R.id.txtUsuarioDNI);
        edtPassword=findViewById(R.id.txtLContrasena);
    }

}