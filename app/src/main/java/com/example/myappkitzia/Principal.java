package com.example.myappkitzia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappkitzia.Adapter.MiAdapter;
import com.example.myappkitzia.Recursos.DesUtil;
import com.example.myappkitzia.Recursos.MyData;
import com.example.myappkitzia.Recursos.Nya;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    public String TAG = "Principal";
    private ListView listView;
    public MyData myData1;
    private List<MyData> lista;
    public static final String KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg";
    public String archivo = "";
    public String usuario = "";
    public String json= "";
    public DesUtil desUtil;
    private int[]logos ={R.mipmap.img,R.mipmap.img_2, R.mipmap.img_1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        desUtil = new DesUtil();
        listView = (ListView) findViewById(R.id.listViewID);
        lista = new ArrayList<MyData>();

        String aux = null;
        Nya testJson = null;
        String object = null;
        TextView textito = findViewById(R.id.textito);
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.getExtras() != null) {
                object = intent.getStringExtra("Usuario");
                if (object != null) {
                    usuario = object;
                    textito.setText(object);
                    archivo = object+".json";
                    Log.d(TAG, object);
                }
                else{
                    textito.setText("oops");
                }
            }

        }

        if(isFileExits()){
            Read();
            json2List(json);
            Log.d(TAG, json);

            for (MyData myData1 : lista){
                if (isNotNullAndNotEmpty(KEY)){
                    desUtil.addStringKeyBase64(KEY);
                }

                String usuario = myData1.getNombre();
                String contra = myData1.getPswd();
                String usr = desUtil.desCifrar(usuario);
                String pswd = desUtil.desCifrar(contra);

                Log.d(TAG, usr);
                Log.d(TAG, pswd);

                myData1.setLogo(logos[(int)(Math.random() *3 )]);
                myData1.setNombre(usr);
                myData1.setPswd(pswd);

                lista.add(myData1);
                MiAdapter myAdapter = new MiAdapter(lista, getBaseContext());
                listView.setAdapter(myAdapter);
            }


        }
        else{
            Toast.makeText(getApplicationContext(), "No hay archivo", Toast.LENGTH_LONG).show();
        }
/*
        for (int i = 0; i<5; i++){
            myData = new MyData();
            myData.setPswd(String.format("Contraseña: %d" , (int)(Math.random()*1000000)));

            if(i==0){
                myData.setNombre("Discord");
                myData.setLogo(logos[0]);
            }
            if(i==1){
                myData.setNombre("Youtube");
                myData.setLogo(logos[1]);
            }
            if(i==2){
                myData.setNombre("Tumblr");
                myData.setLogo(logos[2]);
            }
            if (i==3){
                myData.setNombre("Twitter");
                myData.setLogo(logos[3]);
            }
            if(i==4){
                myData.setNombre("Reddit");
                myData.setLogo(logos[4]);
            }

            list.add(myData);
        }

 */


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean flag = false;
        flag = super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = null;
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mi_menu, menu);

        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String selection = null;
        switch (item.getItemId()){
            case R.id.menuNuevoId:
                selection = String.format("Option %s" , item.getTitle().toString());
                aGuardar();
                break;

            default:
                selection = "No hay ninguno seleccionado";
                break;
        }
        Toast.makeText(this, selection, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private File getFile() {
        return new File(getDataDir(), archivo);
    }

    private boolean isFileExits() {
        File file = getFile();
        if (file == null) {
            return false;
        }
        return file.isFile() && file.exists();
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
        Type listType = new TypeToken<ArrayList<MyData>>() {
        }.getType();
        lista = gson.fromJson(json, listType);
        if (lista == null || lista.size() == 0) {
            Toast.makeText(getApplicationContext(), "Error list is null or empty", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean isNotNullAndNotEmpty( String aux )
    {
        return aux != null && aux.length() > 0;
    }

    public void aGuardar(){
        Intent intent =  new Intent(getBaseContext(), Agregar.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
    }
}