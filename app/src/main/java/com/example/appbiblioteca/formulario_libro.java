package com.example.appbiblioteca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class formulario_libro extends AppCompatActivity {

    private TextView lblFormulario;
    private String accion;
    private ImageView imgPortadaL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_libro);
        lblFormulario = (TextView) findViewById(R.id.lblFormulario);
        Bundle b = this.getIntent().getExtras();
        this.accion = b.getString("accion");
        lblFormulario.setText( accion + " libro");
        imgPortadaL = (ImageView) findViewById(R.id.imgPortadaLibro);
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
                System.out.println(data.getData().toString());
                imgPortadaL.setImageURI(data.getData());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lo lamento no se a cargado una imagen " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void guardar(View view){
        if(this.accion.equals("Registrar")){

        }else{

        }
    }
}