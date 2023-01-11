package com.app.proyecto_citamedica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
String usuario,password;
public static String URL_LOGIN="https://appcolegiophp.herokuapp.com/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializar();
        recuperarPreferencias();
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario=edtUsuario.getText().toString();
                password=edtPassword.getText().toString();
                if(!usuario.isEmpty() && !password.isEmpty()){
                    servicioLogin();
                }else{
                    Toast.makeText(MainActivity.this,"No se permiten campos vacios",Toast.LENGTH_SHORT).show();
                }
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
                        guardarPreferencias();
                        Toast.makeText(MainActivity.this, Message, Toast.LENGTH_SHORT).show();
                        Log.i("response-1", "ID - " + response.toString()+Nombres.toString()+Apellidos.toString()+Pid.toString());
                        Intent i = new Intent(MainActivity.this, Menu_Activity.class);
                        util.nombre=Nombres;
                        util.apellido=Apellidos;
                        util.pId=Pid;
                        util.sesion=true;
                       /* i.putExtra("Nombres",Nombres);
                        i.putExtra("Apellidos",Apellidos);
                        i.putExtra("Pid",Pid);
                        i.putExtra("login",util.sesion);
                        */startActivity(i);
                        finish();
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
                param.put("usuario",usuario);
                param.put("password",password);
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("usuario",usuario);
        editor.putString("password",password);
        editor.putBoolean("sesion",util.sesion);
        editor.commit();
    }
    public void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        edtUsuario.setText(preferences.getString("usuario",""));
        edtPassword.setText(preferences.getString("password",""));
    }
    public void inicializar(){
        btnIngresar=findViewById(R.id.btnIngresar);
        btnAgregarUsuario=findViewById(R.id.btnAgregarUsuario);
        edtUsuario=findViewById(R.id.txtUsuarioDNI);
        edtPassword=findViewById(R.id.txtLContrasena);
    }
}