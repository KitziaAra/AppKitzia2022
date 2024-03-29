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
import java.io.IOException;
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

        Read();
        json2List(json);

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

    public boolean Read() {
        if (!isFileExits()) {
            return false;
        }
        File file = getFile();
        FileInputStream fileInputStream = null;
        byte[] bytes = null;
        bytes = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            json = new String(bytes);
            Log.d(TAG, json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    private File getFile() {
        return new File(getDataDir(), Registro.archivo);
    }

    private boolean isFileExits() {
        File file = getFile();
        if (file == null) {
            return false;
        }
        return file.isFile() && file.exists();
    }

    public void acceso() {
        int i = 0;
        for (MyInfo myInfo : list) {
            if (myInfo.getNombre().equals(usr) && myInfo.getPswd().equals(pswdCifr)) {
                Intent intent = new Intent(Login2.this, Principal.class);
                startActivity(intent);
                i = 1;

            }
        }
        if (i == 0) {
            Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos ", Toast.LENGTH_LONG).show();
        }
    }
}