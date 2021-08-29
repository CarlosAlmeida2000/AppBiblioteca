package com.example.appbiblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Asynchtask {

    private TextView txtUsuario;
    private TextView txtClave;
    private ProgressDialog progDailog;
    private  JSONObject usuario;
    private JSONArray json_array;
    private Intent newActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        this.txtClave = (TextView) findViewById(R.id.txtClave);
    }

    public void login(View view) {
        try {
            JSONObject json_mensaje = new JSONObject();
            json_mensaje.put("usuario", txtUsuario.getText());
            json_mensaje.put("clave", txtClave.getText());
            ServicioTask servicioTask = new ServicioTask(this, "POST","https://wssecurity.herokuapp.com/api-usuario/login/", json_mensaje.toString(), this);
            txtUsuario.setText("");
            txtClave.setText("");
            servicioTask.execute();
            progDailog = new ProgressDialog(this);
            progDailog.setTitle("Verificando usuario");
            progDailog.setMessage("por favor, espere...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void processFinish(String result) throws JSONException {
        try {
            JSONObject json_response = new JSONObject(result);
            progDailog.dismiss();
            if(json_response.has("usuario")){
                newActivity = new Intent(this, Portal_libros.class);
                json_array = json_response.getJSONArray("usuario");
                usuario = json_array.getJSONObject(0);
                Toast.makeText(this, "Bienvenido (a) " + usuario.get("nombre").toString(), Toast.LENGTH_LONG).show();
                startActivity(newActivity);
            }else{
                Toast.makeText(this, json_response.get("mensaje").toString(), Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            progDailog.dismiss();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}