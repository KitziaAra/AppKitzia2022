package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myappkitzia.Recursos.Digest;
import com.example.myappkitzia.Recursos.MyInfo;
import com.example.myappkitzia.SQLite.BDInfo;
import com.example.myappkitzia.SQLite.BDTablas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity {
    public String TAG = "Registro";
    private EditText txtNombre, txtContra, txtEdad, txtCorreo, txtFecha;
    private CheckBox Prim, Ver, Oto, Inv;
    private Switch Cafe;
    private RadioGroup Gen;
    private RadioButton Fem, Masc, Otro;
    private Button btnRegistro;
    private String json2;
    private List<MyInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre = findViewById(R.id.editTextNom);
        txtContra = findViewById(R.id.editTxtP);
        txtEdad = findViewById(R.id.editTextEdad);
        txtCorreo = findViewById(R.id.editTextCorreo);
        txtFecha = findViewById(R.id.editTextFecha);
        Prim = findViewById(R.id.cBPrim);
        Ver = findViewById(R.id.cBVer);
        Oto = findViewById(R.id.cBOto);
        Inv = findViewById(R.id.cBInv);
        Gen = findViewById(R.id.radioGroup);
        Fem = findViewById(R.id.rBFem);
        Masc = findViewById(R.id.rBMasc);
        Otro = findViewById(R.id.rBOtro);
        Cafe = findViewById(R.id.switch1);
        btnRegistro = findViewById(R.id.btnRegistro);

        registrar();

    }


    public void registrar() {
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtNombre.getText().length() == 0 | txtContra.getText().length() == 0 | txtEdad.getText().length() == 0 | txtCorreo.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Vacío", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean TipoCorreo = false;
                String Mail = "";
                for(int x = 0 ; x < txtCorreo.getText().length(); x++) {
                    if (txtCorreo.getText().charAt(x) == '@') {
                        for (int y = x; y < txtCorreo.length(); y++) {
                            Mail = Mail + txtCorreo.getText().charAt(y);
                        }
                        if ("@gmail.com".equals(Mail) || "@hotmail.com".equals(Mail) || "@outlook.com".equals(Mail)) {
                            TipoCorreo = true;
                        }
                        break;
                    }
                }
                if(TipoCorreo == false){
                    Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_LONG).show();
                    return;
                }


                String Nombre = txtNombre.getText().toString();
                String Contra = txtContra.getText().toString();
                String Edad = txtEdad.getText().toString();
                String Correo = txtCorreo.getText().toString();
                String Fecha = txtFecha.getText().toString();
                String Genero = "";
                String estacion = "";
                boolean cafe = Cafe.isChecked();

                if (Prim.isChecked()) {
                    estacion += "Primavera ";
                }
                if (Ver.isChecked()) {
                    estacion += "Verano ";
                }
                if (Oto.isChecked()) {
                    estacion += "Otoño ";
                }
                if (Inv.isChecked()) {
                    estacion += "Invierno ";
                }
                if (!Prim.isChecked() && !Ver.isChecked() && !Oto.isChecked() && !Inv.isChecked()) {
                    estacion = "ninguna";
                }
                if (Fecha == "") {
                    Fecha = "---";
                }
                if (Genero == "") {
                    Genero = "No especificado";
                }
                if (Fem.isChecked()){
                    Genero = "Mujer";
                }
                if (Masc.isChecked()){
                    Genero = "Hombre";
                }
                if(Otro.isChecked()){
                    Genero = "Otro";
                }

                Digest sha1 = new Digest();
                byte[] txtByte = sha1.createSha1(Nombre + Contra);
                String pswdCifr = sha1.bytesToHex(txtByte);


                json2 = lista2Json(Nombre, pswdCifr, Edad, Genero, Correo, Fecha, estacion, cafe);
/*
                try {
                    if (writeFile(json2)) {
                        vaciar();
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();*/

// si jalo AAAAAAAAAA

                String mnsj = "";
                try {
                    boolean BucleArchivo = true;
                    int x = 1;
                    while (BucleArchivo) {
                        BDInfo bdInfo = new BDInfo(Registro.this);
                        if (bdInfo.checarInfo(x)) {
                            String textoInfo = bdInfo.verInfo(x);

                            json2List(textoInfo);
                            for (MyInfo info : list) {
                                String usrName = info.getNombre();
                                String mail = info.getCorreo();

                                if (Nombre.equals(usrName)) {

                                    mnsj = "Numero Ya Registrado";
                                    BucleArchivo = false;
                                } else {
                                    if(Correo.equals(mail)){
                                        mnsj = "Correo Ya Registrado";
                                    }
                                    else{
                                        x = x + 1;}
                                }
                            }
                        }
                        else {
                            long status = bdInfo.insertarInfo(x, json2);
                            if (status > 0) {
                                mnsj = "Usuario Registrado";
                                vaciar();
                                BucleArchivo = false;
                                new Handler( ).postDelayed(new Runnable() {
                                    @Override
                                    public void run(){
                                        Intent intent = new Intent( Registro.this, Login2.class);
                                        startActivity( intent );
                                    }
                                } , 1500 );
                            } else {
                                mnsj = "Error al Hacer Registro";
                            }
                        }
                    }
                }
                catch (Exception e){
                    mnsj = e.toString();
                }
                Toast.makeText(Registro.this, mnsj, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vaciar(){
        txtNombre.setText("");
        txtContra.setText("");
        txtEdad.setText("");
        txtCorreo.setText("");
        txtFecha.setText("");
        Gen.clearCheck();
        Prim.setSelected(false);
        Ver.setSelected(false);
        Oto.setSelected(false);
        Inv.setSelected(false);
        Cafe.setChecked(false);
    }


    /*EL CEMENTERIO >:D*/




 //para pasar de lista a JSON

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

 //para escribir en el archivo


 //botones

    public void inicio(View view) {
        Intent intent = new Intent(this, Login2.class);
        startActivity(intent);
    }


}
