package com.example.appbiblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.Modelos.Libro;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Portal_libros extends AppCompatActivity implements Asynchtask {

    private TextView lblUsuario;
    private PlaceHolderView phv_libros;
    private FloatingActionButton buttonBar;
    private Intent changeActivity;
    private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_libros);
        lblUsuario = (TextView) findViewById(R.id.lblUsuario);
        phv_libros = (PlaceHolderView)findViewById(R.id.phv_libros);
        buttonBar = (FloatingActionButton)findViewById(R.id.btnRegistrarLib);
        try {
            if(MainActivity.getUsuario().getString("rol").equals("AD")){
                buttonBar.setVisibility(View.VISIBLE);
                lblUsuario.setText("Administrador - " + MainActivity.getUsuario().getString("nombre"));
            }else{
                buttonBar.setVisibility(View.INVISIBLE);
                lblUsuario.setText("Usuario - " + MainActivity.getUsuario().getString("nombre"));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        ServicioTask servicioTask = new ServicioTask(this, "GET","https://bibliotecajacoh.herokuapp.com/api-libro/libro/", this);
        servicioTask.execute();
    }

    public void getLibros(View view){
        ServicioTask servicioTask = new ServicioTask(this, "GET","https://bibliotecajacoh.herokuapp.com/api-libro/libro/", this);
        servicioTask.execute();
    }

    public void registrar(View view){
        changeActivity = new Intent(getApplicationContext(), formulario_libro.class);
        //changeActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        b = new Bundle();
        b.putString("accion", "registrar");
        changeActivity.putExtras(b);
        getBaseContext().startActivity(changeActivity);
    }
    @Override
    public void processFinish(String result) throws JSONException {
        try {
            this.phv_libros.removeAllViews();
            JSONObject json_data = new JSONObject(result);
            JSONArray json_libro = json_data.getJSONArray("libro");
            for(int i = 0; i < json_libro.length(); i++){
                JSONObject un_libro = json_libro.getJSONObject(i);
                this.phv_libros.addView(new Libro(getApplicationContext(), un_libro));
            }
        }catch (JSONException ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}