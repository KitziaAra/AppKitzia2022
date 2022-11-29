package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myappkitzia.Recursos.DesUtil;
import com.example.myappkitzia.Recursos.MyData;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Agregar extends AppCompatActivity {
    public String archivo = "";
    public static final String KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg";
    public EditText sitio, contra;
    public Button guarda;
    public String usr;
    public String TAG="Agregar";
    public DesUtil desUtil;
    public String txtCifr1, txtCfr2, json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        sitio = findViewById(R.id.editSitio);
        contra = findViewById(R.id.editContra);
        guarda = findViewById(R.id.button);

        Intent intent = getIntent();
        String object = null;

        if (intent != null) {
            if (intent.getExtras() != null) {
                object = intent.getStringExtra("Usuario");
                if (object != null) {
                    usr = object;
                    archivo = object + ".json";
                    Log.d(TAG, object);

                }
            }
        }

        guardar();
    }

    public void guardar(){
        guarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desUtil = new DesUtil();
                String sitioWeb = sitio.getText().toString();
                String contraseña = contra.getText().toString();

                if (isNotNullAndNotEmpty(KEY)){
                    desUtil.addStringKeyBase64(KEY);
                }
                if (!isNotNullAndNotEmpty(sitioWeb) || !isNotNullAndNotEmpty(contraseña)) {
                    Toast.makeText(getApplicationContext(), "Vacío", Toast.LENGTH_LONG).show();
                    return;
                }
                String paLog = sitioWeb+contraseña;
                Log.d(TAG, paLog);
                txtCifr1 = desUtil.cifrar(sitioWeb);
                txtCfr2 = desUtil.cifrar(contraseña);

                json = lista2Json(txtCifr1, txtCfr2);

                try {
                    if (writeFile(json)) {
                        vaciar();
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void vaciar(){
        sitio.setText("");
        contra.setText("");
    }

    public String lista2Json(String sitio, String contra) {
        MyData myData = null;
        Gson gson = null;
        String json = null;
        String mnsj = null;
        ArrayList list;

        myData = new MyData();
        myData.setNombre(sitio);
        myData.setPswd(contra);

        Log.d(TAG, "test");

        gson = new Gson();
        list = new ArrayList<MyData>();
        list.add(myData);
        json = gson.toJson(list, ArrayList.class);

        if (json != null) {
            Log.d(TAG, json);
            mnsj = "JSON OK";
        } else {
            mnsj = "Error JSON";
        }
        Toast.makeText(getApplicationContext(), mnsj, Toast.LENGTH_LONG).show();
        return json;
    }

    private boolean writeFile(String text) throws IOException {
        File file = null;
        FileOutputStream fileOutputStream = null;
        try {
            file = getFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private File getFile() {
        return new File(getDataDir(), archivo);
    }

    public boolean isNotNullAndNotEmpty( String aux )
    {
        return aux != null && aux.length() > 0;
    }

}