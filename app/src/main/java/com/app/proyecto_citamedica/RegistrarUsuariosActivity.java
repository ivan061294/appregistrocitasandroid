package com.app.proyecto_citamedica;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrarUsuariosActivity extends AppCompatActivity {

    private Button btnSubirImagen, btnGuardarDatos;
    private AutoCompleteTextView dropdownTipoDoc, dropdownSexo, dropdownDistrito;
    private EditText edtNameUser, edtApellidoPaternoU, edtApellidoMaternoU, edtNumDocU, edtTelefonoU,
            edtDireccionU, edtPasswordUser, edtEmailUser,edtFecha;
    private TextInputLayout txtInputNameUser, txtInputApellidoPaternoU, txtInputApellidoMaternoU,
            txtInputTipoDoc, txtInputNumeroDocU, txtInputFecha, txtInputProvincia,txtInputSexo,
            txtInputDistrito, txtInputTelefonoU, txtInputDireccionU, txtInputEmailUser, txtInputPasswordUser;
    private final static int LOCATION_REQUEST_CODE = 23;
    public static String URL_Registro_Paciente="https://appcolegiophp.herokuapp.com/registrar.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuarios);
        this.init();
        this.spinners();
    }
    private void spinners() {
        //LISTA DE TIPOS DE DOCUMENTOS
        String[] tipoDoc = getResources().getStringArray(R.array.tipoDoc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        dropdownTipoDoc.setAdapter(arrayTipoDoc);
        //LISTA DE SEXO
        String[] tipoSexo = getResources().getStringArray(R.array.tipoSexo);
        ArrayAdapter arrayTipoSexo = new ArrayAdapter(this, R.layout.dropdown_item, tipoSexo);
        dropdownSexo.setAdapter(arrayTipoSexo);
    }
   /* @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    LOCATION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gracias por conceder los permisos para " +
                            "leer el almacenamiento, estos permisos son necesarios para poder " +
                            "escoger tu foto de perfil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No podemos realizar el registro si no nos concedes los permisos para leer el almacenamiento.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }*/

    private void init() {
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos);
        //btnSubirImagen = findViewById(R.id.btnSubirImagen);
        //imageUser = findViewById(R.id.imageUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtApellidoPaternoU = findViewById(R.id.edtApellidoPaternoU);
        edtApellidoMaternoU = findViewById(R.id.edtApellidoMaternoU);
        edtNumDocU = findViewById(R.id.edtNumDocU);
        edtTelefonoU = findViewById(R.id.edtTelefonoU);
        edtDireccionU = findViewById(R.id.edtDireccionU);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        edtEmailUser.setEnabled(false);
        //AutoCompleteTextView
        dropdownTipoDoc = findViewById(R.id.dropdownTipoDoc);
        edtFecha = findViewById(R.id.edtFecha);
        dropdownSexo = findViewById(R.id.dropdownSexo);
        //dropdownDistrito = findViewById(R.id.dropdownDistrito);
        //TextInputLayout
        txtInputNameUser = findViewById(R.id.txtInputNameUser);
        txtInputApellidoPaternoU = findViewById(R.id.txtInputApellidoPaternoU);
        txtInputApellidoMaternoU = findViewById(R.id.txtInputApellidoMaternoU);
        txtInputTipoDoc = findViewById(R.id.txtInputTipoDoc);
        txtInputNumeroDocU = findViewById(R.id.txtInputNumeroDocU);
        txtInputFecha = findViewById(R.id.txtInputFecha);
        txtInputProvincia = findViewById(R.id.txtInputProvincia);
        txtInputDistrito = findViewById(R.id.txtInputDistrito);
        txtInputTelefonoU = findViewById(R.id.txtInputTelefonoU);
        txtInputDireccionU = findViewById(R.id.txtInputDireccionU);
        txtInputEmailUser = findViewById(R.id.txtInputEmailUser);
        txtInputPasswordUser = findViewById(R.id.txtInputPasswordUser);
        txtInputSexo = findViewById(R.id.txtInputSexo);

        btnGuardarDatos.setOnClickListener(v -> {
            this.guardarDatos();
        });
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().
                setTitleText("Seleccione Fecha").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
        edtFecha.setOnClickListener(v->{
            datePicker.show(getSupportFragmentManager(),"Material_Date_picker");
            datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate  = format.format(calendar.getTime());
                    edtFecha.setText(formattedDate);

                  //edtFecha.setText(String.format(datePicker.getHeaderText(), "yyyy-mm-dd"));
                }
            });

        });
        ///ONCHANGE LISTENEER A LOS EDITEXT
        edtNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNameUser.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtApellidoPaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoPaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtApellidoMaternoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputApellidoMaternoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtNumDocU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNumeroDocU.setErrorEnabled(false);
                edtEmailUser.setText(edtNumDocU.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtTelefonoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTelefonoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDireccionU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDireccionU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownSexo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSexo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*
        dropdownDepartamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDepartamento.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownProvincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputProvincia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownDistrito.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDistrito.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
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
                        successMessage("Estupendo! " + "Su información ha sido guardada con éxito en el sistema.");
                        Intent i = new Intent(RegistrarUsuariosActivity.this,MainActivity.class);
                        startActivity(i);
                    }else {
                        toastIncorrecto("No se ha podido guardar los datos, intentelo de nuevo");
                    }
                } catch (JSONException ex) {
                    warningMessage("Se ha producido un error : " + ex.getMessage());
                    Log.i("error","Llego aca catch");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrarUsuariosActivity.this,"Error"+ error.toString(),Toast.LENGTH_SHORT).show();
                Log.i("error","Llego aca Response");
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("nombres",edtNameUser.getText().toString());
                param.put("apellidos",edtApellidoPaternoU.getText().toString() + " "+edtApellidoMaternoU.getText().toString());
                param.put("direccion",edtDireccionU.getText().toString());
                param.put("contrasena",edtPasswordUser.getText().toString());
                param.put("telefono",edtTelefonoU.getText().toString());
                param.put("dni",edtNumDocU.getText().toString());
                param.put("sexo",dropdownSexo.getText().toString());
                param.put("fechaNacimiento",edtFecha.getText().toString());
                return param;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarDatos(){
        if (validar()) {
            RegistrarP();
        } else {
            errorMessage("Por favor, complete todos los campos del formulario");
        }
    }
    private boolean validar() {
        boolean retorno = true;
        String nombres, apellidoPaterno, apellidoMaterno, numDoc, telefono, direccion,  clave,
                fecha, sexo, tipoidentificacion, dropDistrito;
        nombres = edtNameUser.getText().toString();
        apellidoPaterno = edtApellidoPaternoU.getText().toString();
        apellidoMaterno = edtApellidoMaternoU.getText().toString();
        numDoc = edtNumDocU.getText().toString();
        telefono = edtTelefonoU.getText().toString();
        direccion = edtDireccionU.getText().toString();
        clave = edtPasswordUser.getText().toString();
        fecha=edtFecha.getText().toString();
        sexo=dropdownSexo.getText().toString();

        if (fecha.isEmpty()) {
            txtInputFecha.setError("Ingresar Fecha de Nacimiento");
            retorno = false;
        } else {
            txtInputFecha.setErrorEnabled(false);
        }if (sexo.isEmpty()) {
            txtInputSexo.setError("Ingresar Sexo");
            retorno = false;
        } else {
            txtInputSexo.setErrorEnabled(false);
        }
        if (nombres.isEmpty()) {
            txtInputNameUser.setError("Ingresar nombres");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        if (apellidoPaterno.isEmpty()) {
            txtInputApellidoPaternoU.setError("Ingresar apellido paterno");
            retorno = false;
        } else {
            txtInputApellidoPaternoU.setErrorEnabled(false);
        }
        if (telefono.isEmpty()) {
            txtInputTelefonoU.setError("Ingresar telefono");
            retorno = false;
        } else {
            txtInputTelefonoU.setErrorEnabled(false);
        }
        if (apellidoMaterno.isEmpty()) {
            txtInputApellidoMaternoU.setError("Ingresar apellido materno");
            retorno = false;
        } else {
            txtInputApellidoMaternoU.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingresar número documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (direccion.isEmpty()) {
            txtInputDireccionU.setError("Ingresar dirección de su casa");
            retorno = false;
        } else {
            txtInputDireccionU.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingresar clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        return retorno;
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


}