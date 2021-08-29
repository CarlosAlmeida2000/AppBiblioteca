package com.example.appbiblioteca.Modelos;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbiblioteca.MainActivity;
import com.example.appbiblioteca.R;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;

@NonReusable
@Layout(R.layout.card_libro)
public class Libro {

    @View(R.id.txtNombreLibro)
    TextView txtNombreLibro;

    @View(R.id.txtDescripcion)
    TextView txtDescripcion;

    @View(R.id.imgPortadaLibro)
    ImageView imgPortadaLibro;

    @View(R.id.btnEditar)
    Button btnEditar;

    Context contexto;
    JSONObject unaLibro;
    String base64Image;
    byte[] decodedString;
    Bitmap decodedByte;
    ImagenBitmap decoder;

    public Libro(Context contexto, JSONObject unaLibro) {
        this.contexto = contexto;
        this.unaLibro = unaLibro;
    }

    @Resolve
    protected void onResolved(){
        try{
            this.txtNombreLibro.setText(unaLibro.getString("libro"));
            this.txtDescripcion.setText(unaLibro.getString("descripcion"));
            decoder = new ImagenBitmap(unaLibro.getString("foto"));
            this.imgPortadaLibro.setImageBitmap(decoder.getImagen());
            if(MainActivity.getUsuario().getString("rol") == "US"){
                this.btnEditar.setVisibility(android.view.View.INVISIBLE);
            }
        }catch (JSONException ex){
            Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Click(R.id.btnEditar)
    public void editar_libro(){

    }
}
