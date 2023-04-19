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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myappkitzia.Adapter.MiAdapter;
import com.example.myappkitzia.Adapter.MiAdapterEdit;
import com.example.myappkitzia.Adapter.MiAdapterQuitar;
import com.example.myappkitzia.Recursos.DesUtil;
import com.example.myappkitzia.Recursos.MyData;
import com.example.myappkitzia.SQLite.BDCuenta;
import com.example.myappkitzia.SQLite.BDInfo;
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
    private int []imagen = { R.drawable.mapita,R.mipmap.editar1,R.mipmap.borrar };
    private ListView listView, listView1, listView2, listView3;
    private Button btnAnterior, btnSiguiente;
    public MyData myData1;
    private List<MyData> lista, lista1, lista2, lista3, list;
    public String archivo = "";
    public String usuario = "";
    public String json= "";
    public DesUtil desUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        desUtil = new DesUtil();
        listView = (ListView) findViewById(R.id.listViewID);
        lista = new ArrayList<MyData>();

        int numArchivo = getIntent().getExtras().getInt("numArchivo");
        int numLista = getIntent().getExtras().getInt("numLista");

        btnAnterior = (Button) findViewById(R.id.btnAnt);
        btnSiguiente = (Button) findViewById(R.id.btnSig);


        try {
            BDInfo dbInfo = new BDInfo(Principal.this);
            BDCuenta dbCuenta = new BDCuenta(Principal.this);
            String completoTextoU = dbInfo.verInfo(numArchivo);


            listView = (ListView) findViewById(R.id.listViewSitio);
            lista = new ArrayList<MyData>();

            listView1 = (ListView) findViewById(R.id.listViewMapa);
            lista1 = new ArrayList<MyData>();

            listView2 = (ListView) findViewById(R.id.listViewEditar);
            lista2 = new ArrayList<MyData>();

            listView3 = (ListView) findViewById(R.id.listViewQuitar);
            lista3 = new ArrayList<MyData>();

            boolean BucleArchivo = true;
            int x = numLista;
            while (BucleArchivo) {
                if((dbCuenta.checarCuenta(numArchivo, x)) && (x < (numLista + 5))){
                    String completoTexto = dbCuenta.verCuenta(numArchivo, x);

                    json2List(completoTexto);
                    for (MyData myData : list) {


                        MyData cuenta = new MyData();
                        MyData cuenta1 = new MyData();
                        MyData cuenta2 = new MyData();
                        MyData cuenta3 = new MyData();
                        cuenta.setPswd(myData.getPswd());
                        cuenta.setNombre(myData.getNombre());
                        cuenta.setLocation(myData.getLocation());
                        cuenta.setTipo(myData.isTipo());
                        cuenta.setImageP(myData.getImageP());
                        cuenta.setImage(myData.getImage());
                        cuenta1.setImage(imagen[0]);
                        cuenta2.setImage(imagen[1]);
                        cuenta3.setImage(imagen[2]);
                        lista.add(cuenta);
                        lista1.add(cuenta1);
                        lista2.add(cuenta2);
                        lista3.add(cuenta3);
                    }
                    x = x + 1;
                }else{
                    BucleArchivo = false;
                }
            }

            if(numLista == 1){
                btnAnterior.setEnabled(false);
            }
            if (!dbCuenta.checarCuenta(numArchivo, (numLista + 5))){
                btnSiguiente.setEnabled(false);
            }

            MiAdapter myAdapter = new MiAdapter(lista, getBaseContext());
            listView.setAdapter(myAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    toast(i);
                }
            });
/*
            MiAdapterEdit myAdapter1 = new MiAdapterEdit(lista1, getBaseContext());
            listView1.setAdapter(myAdapter1);
            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    toast1( i + (numLista - 1));
                }
            });*/

            MiAdapterEdit myAdapter2 = new MiAdapterEdit(lista2, getBaseContext());
            listView2.setAdapter(myAdapter2);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    toast2( i + (numLista - 1));
                }
            });

            MiAdapterQuitar myAdapter3 = new MiAdapterQuitar(lista3, getBaseContext());
            listView3.setAdapter(myAdapter3);
            listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    toast3( i + (numLista - 1));
                }
            });

            btnAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (Principal.this, Principal.class);
                    intent.putExtra("numArchivo", numArchivo);
                    intent.putExtra("numLista", numLista - 5);
                    startActivity( intent );
                }
            });

            btnSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (Principal.this, Principal.class);
                    intent.putExtra("numArchivo", numArchivo);
                    intent.putExtra("numLista", numLista + 5);
                    startActivity( intent );
                }
            });

        }catch(Exception e){
            Toast.makeText(getBaseContext(), "Error al Cargar la Lista", Toast.LENGTH_SHORT).show();
        }
    }

    private void toast(int i )
    {
        Toast.makeText(getBaseContext(), lista.get(i).getPswd(), Toast.LENGTH_SHORT).show();
    }
/*
    private void toast1( int i )
    {
        int numArchivo = getIntent().getExtras().getInt("numArchivo");
        int numLista = getIntent().getExtras().getInt("numLista");
        Intent intent = new Intent (Principal.this, MapList.class);
        intent.putExtra("numArchivo", numArchivo);
        intent.putExtra("numLista", numLista);
        intent.putExtra("numArchivoCuenta", (i + 1));
        startActivity(intent);
    }*/

    private void toast2( int i )
    {
        int numArchivo = getIntent().getExtras().getInt("numArchivo");
        int numLista = getIntent().getExtras().getInt("numLista");
        Intent intent = new Intent (Principal.this, Agregar.class);
        intent.putExtra("numArchivo", numArchivo);
        intent.putExtra("numContext", 2);
        intent.putExtra("numLista", numLista);
        intent.putExtra("numArchivoCuenta", (i + 1));
        startActivity(intent);
    }

    private void toast3( int i )
    {
        try {
            BDCuenta dbCuenta = new BDCuenta(Principal.this);
            int numArchivo = getIntent().getExtras().getInt("numArchivo");
            int numLista = getIntent().getExtras().getInt("numLista");
            if (numLista == (i+1) && numLista > 1 && !dbCuenta.checarCuenta(numArchivo, (numLista + 1))){numLista -= 5;}
            boolean BucleArchivo = true;
            int x = (i + 1);
            while (BucleArchivo) {
                if (dbCuenta.checarCuenta(numArchivo, x) & dbCuenta.checarCuenta(numArchivo, (x + 1))){
                    int numArchivoCuenta = getIntent().getExtras().getInt("numArchivoCuenta");
                    String completoTexto = dbCuenta.verCuenta(numArchivo, (x + 1));
                    dbCuenta.editarCuenta(numArchivo, x, completoTexto);

                    x = x + 1;
                }
                if (dbCuenta.checarCuenta(numArchivo, x) & !dbCuenta.checarCuenta(numArchivo, (x + 1))){
                    dbCuenta.borrarCuenta(numArchivo, x);

                    Intent intent = new Intent (Principal.this, Principal.class);
                    intent.putExtra("numArchivo", numArchivo);
                    intent.putExtra("numLista", numLista);
                    startActivity( intent );
                    BucleArchivo = false;
                }
            }
        }catch(Exception e){}
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

            case R.id.videoId:
                selection = String.format("Option %s", item.getTitle().toString());
                aVideo();
                break;

            default:
                selection = "No hay ninguno seleccionado";
                break;
        }
        Toast.makeText(this, selection, Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
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
        list = gson.fromJson(json, listType);
        if (list == null || list.size() == 0) {
            Toast.makeText(getApplicationContext(), "Error list is null or empty", Toast.LENGTH_LONG).show();
            return;
        }
    }


    public void aGuardar(){
        Intent intent =  new Intent(getBaseContext(), Agregar.class);
        intent.putExtra("Usuario", usuario);
        startActivity(intent);
    }

    public void aVideo(){
        Intent intent =  new Intent(getBaseContext(), Yutubcito.class);
        startActivity(intent);

    }
}