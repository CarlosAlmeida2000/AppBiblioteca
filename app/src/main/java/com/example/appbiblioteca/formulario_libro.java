package com.example.appbiblioteca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.Modelos.ImagenBitmap;
import com.example.appbiblioteca.WebServices.Asynchtask;
import com.example.appbiblioteca.WebServices.ServicioTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class formulario_libro extends AppCompatActivity implements Asynchtask {

    private TextView lblFormulario;
    private String accion;
    private ImageView imgPortadaL;
    private TextView txtNombre;
    private TextView txtDescripcion;
    private ImagenBitmap decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_libro);
        lblFormulario = (TextView) findViewById(R.id.lblFormulario);
        txtNombre = (TextView)findViewById(R.id.txtNombreLib);
        txtDescripcion = (TextView)findViewById(R.id.txtDescripcionLi);
        imgPortadaL = (ImageView) findViewById(R.id.imgPortadaL);
        Bundle b = this.getIntent().getExtras();
        this.accion = b.getString("accion");
        lblFormulario.setText(accion + " libro");
        try {
        if(this.accion.equals("Editar")){
            txtNombre.setText(b.getString("libro"));
            txtDescripcion.setText(b.getString("descripcion"));
            String base = b.getString("foto");
            decoder = new ImagenBitmap(base);
            this.imgPortadaL.setImageBitmap(decoder.getImagen());
        }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void selecionar_img(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), 121);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 121) {
                Uri path = data.getData();
                imgPortadaL.setImageURI(path);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lo lamento no se a cargado una imagen " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void guardar(View view){
        try {
            if(this.accion.equals("Registrar")){
                JSONObject json_mensaje = new JSONObject();
                json_mensaje.put("nombre", txtNombre.getText());
                json_mensaje.put("descripcion", txtDescripcion.getText());
                decoder = new ImagenBitmap(this.imgPortadaL);
                json_mensaje.put("foto", decoder.getBase64());
                ServicioTask servicioTask = new ServicioTask(this, "POST","https://bibliotecajacoh.herokuapp.com/api-libro/libro/", json_mensaje.toString(), this);
                servicioTask.execute();
            }else{
                JSONObject json_mensaje = new JSONObject();
                json_mensaje.put("nombre", txtNombre.getText());
                json_mensaje.put("descripcion", txtDescripcion.getText());
                decoder = new ImagenBitmap(this.imgPortadaL);
                json_mensaje.put("foto", decoder.getBase64());
                ServicioTask servicioTask = new ServicioTask(this, "PUT","https://bibliotecajacoh.herokuapp.com/api-libro/libro/", json_mensaje.toString(), this);
                servicioTask.execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void processFinish(String result) throws JSONException {
        try {
            JSONObject json_response = new JSONObject(result);
            if(json_response.getBoolean("confirmacion")){
                if(this.accion.equals("Editar")){
                    Toast.makeText(this, "Libro modificado.", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(this, "Libro registrado.", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "Sucedió un error, por favor intente nuevamente.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}