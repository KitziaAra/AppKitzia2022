package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myappkitzia.Recursos.Nya;

public class Principal extends AppCompatActivity {
    public TextView textito;
    public String TAG = "Principal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        String aux = null;
        Nya testJson = null;
        String object = null;
        TextView textito = findViewById(R.id.textito);
        Intent intent = getIntent();
        if (intent != null) {
            aux = intent.getStringExtra("Hola");
            if (aux != null && aux.length() > 0) {
                textito.setText(aux);
            }
            if (intent.getExtras() != null) {
                object = intent.getStringExtra("Usuario");
                if (object != null) {
                    textito.setText(object);
                    Log.d(TAG, object);

                }
                else{
                    textito.setText("oops");
                }
            }

        }

    }
}