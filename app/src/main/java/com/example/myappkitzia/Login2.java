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
import com.example.myappkitzia.Recursos.Nya;
import com.example.myappkitzia.SQLite.BDInfo;
import com.example.myappkitzia.SQLite.BDTablas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Login2 extends AppCompatActivity {
    private List<MyInfo> list;
    public static String TAG = "mensaje";
    String json = null;
    public static String usr, passw, pswdCifr;
    public Button reg, olvi, acceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        reg = findViewById(R.id.btnRegi);
        acceso = findViewById(R.id.btnInit);
        EditText usuario = findViewById(R.id.editTxtUsr);
        EditText passws = findViewById(R.id.editTxtPswd);
        olvi = findViewById(R.id.btnOlvi);

        //Read();
        //json2List(json);

        acceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr = String.valueOf(usuario.getText());
                passw = String.valueOf(passws.getText());

                Digest sha1 = new Digest();
                byte[] txtByte = sha1.createSha1(usr + passw);
                pswdCifr = sha1.bytesToHex(txtByte);
                acceso();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login2.this, Registro.class);
                startActivity(i);
            }
        });

        olvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login2.this, Olvido.class);
                startActivity(i);
            }
        });

    }

    public void json2List(String json) {
        Gson gson = null;
        String mensaje = null;
        if (json == null || json.length() == 0) {
            Toast.makeText(getApplicationContext(), "Error json null or empty", Toast.LENGTH_SHORT).show();
            return;
         }
        gson = new Gson();
        Type listType = new TypeToken<ArrayList<MyInfo>>() {
        }.getType();
        list = gson.fromJson(json, listType);
       if (list == null || list.size() == 0) {
            Toast.makeText(getApplicationContext(), "Error list is null or empty", Toast.LENGTH_LONG).show();
            return;
        }
    }


    public void acceso() {
        String mnsj = "";
        try {
            boolean BucleArchivo = true;
            int x = 1;
            int numArchivo = 0;
            while (BucleArchivo) {
                BDInfo bdInfo = new BDInfo(Login2.this);
                if (bdInfo.checarInfo(x)) {
                    String textoInfo = bdInfo.verInfo(x);

                    json2List(textoInfo);
                    for (MyInfo myInfo : list) {
                        String usuario = myInfo.getNombre();
                        String pswd = myInfo.getPswd();

                        if (myInfo.getNombre().equals(usr) && myInfo.getPswd().equals(pswdCifr)) {
                            numArchivo = x;
                            BucleArchivo = false;
                            mnsj = "Usuario encontrado";

                        } else {
                            x = x + 1;
                        }
                    }
                }else{
                        mnsj = "Usuario no Encontrado";
                        BucleArchivo = false;
                    }
                }
            if (mnsj=="Usuario encontrado"){
                Intent intent = new Intent(getBaseContext(), Principal.class);

                intent.putExtra("numArchivo", numArchivo);
                intent.putExtra("numLista", 1);
                startActivity(intent);
            }
        }
        catch (Exception e){
            mnsj = e.toString();
        }
        Toast.makeText(Login2.this, mnsj, Toast.LENGTH_SHORT).show();
    }
}