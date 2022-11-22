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
import com.example.myappkitzia.Recursos.MyData;
import com.example.myappkitzia.Recursos.Nya;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    public String TAG = "Principal";
    private ListView listView;
    private List<MyData> list;
    public String archivo = "";
    public String usuario = "";
    public String json= "";
    private int[]logos ={R.mipmap.discord,R.mipmap.yutu, R.mipmap.tumblur, R.mipmap.tuiter, R.mipmap.redit};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        MyData myData = null;
        listView = (ListView) findViewById(R.id.listViewID);
        list = new ArrayList<MyData>();

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
        MiAdapter myAdapter = new MiAdapter(list, getBaseContext());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Toast.makeText(getBaseContext(), list.get(i).getPswd(),Toast.LENGTH_SHORT).show();
            }
        });

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


    public void aGuardar(){
        Intent intent =  new Intent(getBaseContext(), Agregar.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
    }
}