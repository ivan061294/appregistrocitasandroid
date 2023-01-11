package com.app.proyecto_citamedica;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.google.android.material.textfield.TextInputLayout;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

        import Entidades.util;
        import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText edtMail, edtPassword;
    private Button btnIniciarSesion;
    //private UsuarioViewModel viewModel;
    private TextInputLayout txtInputUsuario, txtInputPassword;
    public static String URL_LOGIN="https://appcolegiophp.herokuapp.com/login.php";
    private TextView txtNuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();
        recuperarPreferencias();
    }

    private void initViewModel() {
       // viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {
        edtMail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        txtNuevoUsuario = findViewById(R.id.txtNuevoUsuario);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(v -> {
            try {
                if (validar()) {
                    servicioLogin();
                    /*viewModel.login(edtMail.getText().toString(), edtPassword.getText().toString()).observe(this, response -> {
                        if (response.getRpta() == 1) {
                            //Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                            toastCorrecto(response.getMessage());
                            Usuario u = response.getBody();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = preferences.edit();
                            final Gson g = new GsonBuilder()
                                    .registerTypeAdapter(Date.class, new DateSerializer())
                                    .registerTypeAdapter(Time.class, new TimeSerializer())
                                    .create();
                            editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>() {
                            }.getType()));
                            editor.apply();
                            edtMail.setText("");
                            edtPassword.setText("");
                            startActivity(new Intent(this, InicioActivity.class));
                        } else {
                            toastIncorrecto("Credenciales Inválidas");
                        }
                    });*/
                } else {
                    toastIncorrecto("Por favor, complete todos los campos.");
                }
            } catch (Exception e) {
                toastIncorrecto("Se ha producido un error al intentar loguearte : " + e.getMessage());
            }
        });
        txtNuevoUsuario.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrarUsuariosActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
        edtMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
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

    private boolean validar() {
        boolean retorno = true;
        String usuario, password;
        usuario = edtMail.getText().toString();
        password = edtPassword.getText().toString();
        if (usuario.isEmpty()) {
            txtInputUsuario.setError("Ingrese su usario y/o correo electrónico");
            retorno = false;
        } else {
            txtInputUsuario.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            txtInputPassword.setError("Ingrese su contraseña");
            retorno = false;
        } else {
            txtInputPassword.setErrorEnabled(false);
        }
        return retorno;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = preferences.getString("UsuarioJson", "");
        if(!pref.equals("")){
            toastCorrecto("Se detecto una sesión activa, el login será omitido!");
            this.startActivity(new Intent(this, Menu_Activity.class));
            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("has oprimido el botón atrás")
                .setContentText("¿Quieres cerrar la aplicación?")
                .setCancelText("No, Cancelar!").setConfirmText("Sí, Cerrar")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                            .setContentText("No saliste de la app")
                            .show();
                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    System.exit(0);
                }).show();
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
                param.put("usuario",edtMail.getText().toString());
                param.put("password",edtPassword.getText().toString());
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void guardarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("usuario",edtMail.getText().toString());
        editor.putString("password",edtPassword.getText().toString());
        editor.putBoolean("sesion", util.sesion);
        editor.commit();
    }
    public void recuperarPreferencias(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        edtMail.setText(preferences.getString("usuario",""));
        edtPassword.setText(preferences.getString("password",""));
    }
}