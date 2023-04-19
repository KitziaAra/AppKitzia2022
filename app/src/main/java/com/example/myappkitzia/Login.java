package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myappkitzia.Recursos.Digest;
import com.example.myappkitzia.Recursos.MyInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    public List<MyInfo> list = null;
    public static String TAG = "mensaje";
    String json = null;
    public EditText txtUsr, txtContra;
    private Button btnInicio;
    private Button btnOlv;
    private Button btnReg;
    private String usuario, contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsr = (EditText) findViewById(R.id.txtUsuario);
        txtContra = (EditText) findViewById(R.id.txtContra);
        btnInicio = findViewById(R.id.btnInicio);
        btnReg = findViewById(R.id.btnReg);
        btnOlv = findViewById(R.id.btnOlv);


    }

/* para checar si existe el archivo, si no hay deshabilita los botones */
    @Override
    public void onResume(){
        super.onResume();
        setContentView(R.layout.activity_login);


    }

    public void acceder() {
        Intent intent = new Intent(Login.this, Principal.class);
        startActivity(intent);
    }


    public void json2List(String json) {
        Gson gson = null;
        String mnsj = null;

        if (json == null || json.length() == 0) {
            Toast.makeText(getApplicationContext(), "Error JSON null or empty", Toast.LENGTH_LONG).show();
        }
        gson = new Gson();
        Type listType = new TypeToken<ArrayList<MyInfo>>() {
        }.getType();
        list = gson.fromJson(json, listType);
        Log.d(TAG, list.toString());
        if (list == null) {
            Toast.makeText(getApplicationContext(), "Error list null or empty", Toast.LENGTH_LONG).show();
            return;
        }
    }
/* botones */

    public void registro(View view){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
    public void olvido(View view){
        Intent intent = new Intent(this, Olvido.class);
        startActivity(intent);
    }

    public void iniciar(View view){
        usuario = txtUsr.getText().toString();
        contra = txtContra.getText().toString();
        Log.d(TAG, usuario);
        Log.d(TAG, contra);

        if(usuario.length()== 0 | contra.length() == 0){

            Toast.makeText(getApplicationContext(), "Llena todos los campos por favor", Toast.LENGTH_LONG).show();
            return;
        }
        //Leer();
        json2List(json);

        Digest sha1 = new Digest();

        int i = 0;
        for (MyInfo myInfo : list) {
            String conjunto = usuario + contra;
            byte[] txtByte = sha1.createSha1(conjunto);
            String pswdCifr = sha1.bytesToHex(txtByte);

            if (myInfo.getNombre().equals(usuario) && myInfo.getPswd().equals(pswdCifr)) {
                Intent intent = new Intent(Login.this, Principal.class);
                startActivity(intent);
                i = 1;

            }
        }
        if (i == 0) {
            Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos ", Toast.LENGTH_LONG).show();
        }
    }

}