package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myappkitzia.Recursos.Correito;
import com.example.myappkitzia.Recursos.DesUtil;
import com.example.myappkitzia.Recursos.Digest;
import com.example.myappkitzia.Recursos.MyInfo;
import com.example.myappkitzia.Recursos.Nya;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Olvido extends AppCompatActivity {
    private List<MyInfo> list;
    public static String TAG = "mensaje";
    public EditText userName, Mail;
    public String json, json2, nuevaContra, HTMLCorreo, mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido);
        userName = (EditText) findViewById(R.id.editUsuario);
        Mail = (EditText) findViewById(R.id.editCorreo);

        String mensaje = "";
    }


    public void RecuperarContraseña (View v){



        if("".equals(userName.getText().toString()) || "".equals(Mail.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Llena todos los campos", Toast.LENGTH_LONG).show();
            return;
        }else{
            boolean TipoCorreo = false;
            String Correo = "";
            for(int x = 0 ; x < Mail.length(); x++){
                if(Mail.getText().charAt(x) == '@'){
                    for(int y = x ; y < Mail.length(); y++){
                        Correo = Correo + Mail.getText().charAt(y);
                    }
                    if("@gmail.com".equals(Correo) || "@hotmail.com".equals(Correo) || "@outlook.com".equals(Correo)){
                        TipoCorreo = true;
                    }
                    break;
                }
            }
            if(userName.getText().length()==0 ||
                    TipoCorreo == false){
                Toast.makeText(getApplicationContext(), "Correo inválido", Toast.LENGTH_LONG).show();
                return;
            }else {
                Read();
                json2List(json);

                String usuario = userName.getText().toString();
                String mail = Mail.getText().toString();

                int i = 0;
                for (MyInfo myInfo : list) {
                    if (myInfo.getNombre().equals(usuario) && myInfo.getCorreo().equals(mail)) {
                        Nya testJson = new Nya();
                        testJson.setNombre(usuario);
                        testJson.setCorreo(mail);

                        nuevaContra = String.format(String.valueOf(Math.random()*100));

                        Digest digest = new Digest();
                        byte[] txtByte = digest.createSha1(usuario + nuevaContra);
                        String Sha1Password = digest.bytesToHex(txtByte);

                        json2 = lista2Json(testJson.getNombre(), Sha1Password, Integer.toString(testJson.getEdad()), testJson.getGenero(), testJson.getCorreo(),
                                testJson.getFecha(), testJson.getEstacion(), testJson.isCafe());

                        i= 1;
                    }
                }
                try{
                    if (writeFile(json2)){
                        i = 2;
                        return;
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                if (i == 0) {
                    Toast.makeText(getApplicationContext(), "Usuario no encontrado :/", Toast.LENGTH_LONG).show();
                }


                if(i == 2){
                    DesUtil myDes = new DesUtil();

                    HTMLCorreo = "<html>\\n\\t<body>\\n\\t\\tLe enviamos este correo para recuperar su contraseña," +
                            " La nueva contraseña es:" + nuevaContra + "en caso de que no lo haya enviado, ignore este correo\\n\\t<body>\\n</html>";
                    mail = myDes.cifrar(mail);
                    HTMLCorreo = myDes.cifrar(HTMLCorreo);
                    String text = correoAJson(mail, HTMLCorreo);
                    if( sendInfo( text ) )
                    {
                        mensaje = "Se envío el Correo";
                    }
                    else
                    {
                        mensaje = "Error en el envío del Correo";
                    }
                }
            }


        }
        Toast.makeText(Olvido.this, mensaje, Toast.LENGTH_SHORT).show();
    }



    public boolean sendInfo( String Correo )
    {
        String TAG = "App";
        JsonObjectRequest jsonObjectRequest = null;
        JSONObject jsonObject = null;
        String url = "https://us-central1-nemidesarrollo.cloudfunctions.net/function-test";
        RequestQueue requestQueue = null;

        jsonObject = new JSONObject( );
        try {
            jsonObject.put("Correo" , Correo );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i(TAG, response.toString());
            }
        } , new  Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        } );
        requestQueue = Volley.newRequestQueue( getBaseContext() );
        requestQueue.add(jsonObjectRequest);

        return true;
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

    public String correoAJson(String mail, String html){
        Correito correo = new Correito();
        Gson gson = new Gson();

        correo.setMailCorreo(mail);
        correo.setHTMLCorreo(html);

        String jsonCorreo = gson.toJson(correo);

        return jsonCorreo;
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
}
