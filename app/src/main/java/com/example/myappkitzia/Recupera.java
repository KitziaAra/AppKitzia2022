package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myappkitzia.Recursos.Digest;
import com.example.myappkitzia.Recursos.MyInfo;
import com.example.myappkitzia.SQLite.BDInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Recupera extends AppCompatActivity {
    private List<MyInfo> list;
    private static final String TAG = "Recuperar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera);

    }

    public void recuperar(View view) {
        EditText codigo = findViewById(R.id.editCod);
        EditText nuevaContra = findViewById(R.id.editNContra);
        EditText nuevaContra2 = findViewById(R.id.editNContra2);

        String cod = codigo.getText().toString();
        String nContra = nuevaContra.getText().toString();
        String nContra2 = nuevaContra2.getText().toString();
        String mnsj = "";

        int numArchivo = getIntent().getExtras().getInt("numArchivo");
        String valorPass = getIntent().getExtras().getString("valorPass");

        if (cod.equals("") || nContra.equals("") || nContra2.equals("")) {
            mnsj = "Llene todos los campos";
        }

        if (!cod.equals(valorPass) || !nContra.equals(nContra2)) {
            mnsj = "Los datos no coinciden";

            if (!cod.equals(valorPass)) {
                mnsj = "El codigo no coincide";
            }
            if (!nContra.equals(nContra2)) {
                mnsj = "La nueva contraseña no coincide";
            }
        } else {
            try {

                BDInfo dbInfo = new BDInfo(Recupera.this);
                String textito = dbInfo.verInfo(numArchivo);

                json2List(textito);
                for (MyInfo myInfo : list) {
                    String valorName = myInfo.getNombre();

                    Digest digest = new Digest();
                    byte[] txtByte = digest.createSha1(valorName + nContra);
                    String Sha1Password = digest.bytesToHex(txtByte);

                    String textoJson = lista2Json(myInfo.getNombre(), Sha1Password, Integer.toString(myInfo.getEdad()), myInfo.getGenero(),
                            myInfo.getCorreo(), myInfo.getFecha(), myInfo.getEstacion(), myInfo.isCafe());

                    dbInfo.editarInfo(numArchivo, textoJson);

                    mnsj = "Contraseña Restablecida";
                    Toast.makeText(Recupera.this, mnsj, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Recupera.this, Login2.class);
                    startActivity(intent);

                }
            }catch(Exception e){
                    mnsj = "Error al Restablecer Contraseña";
                    Log.e(TAG, " Exception: " + e.getMessage());
            }
        }
        Toast.makeText(Recupera.this, mnsj, Toast.LENGTH_SHORT).show();
    }


    public void json2List(String json) {
        Gson gson = null;
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

    public String lista2Json(String nom, String contra, String e, String gen, String email, String fech, String est, boolean cafe) {
        MyInfo myInfo = null;
        Gson gson = null;
        String json = null;
        String mnsj = null;
        ArrayList list;
        int ed = Integer.valueOf(e);

        myInfo = new MyInfo();
        myInfo.setNombre(nom);
        myInfo.setPswd(contra);
        myInfo.setEdad(ed);
        myInfo.setGenero(gen);
        myInfo.setCorreo(email);
        myInfo.setFecha(fech);
        myInfo.setEstacion(est);
        myInfo.setCafe(cafe);

        Log.d(TAG, "test");

        gson = new Gson();
        list = new ArrayList<MyInfo>();
        list.add(myInfo);
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
}