package com.example.myappkitzia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappkitzia.Adapter.MiAdapter;
import com.example.myappkitzia.Recursos.MyData;
import com.example.myappkitzia.Recursos.Nya;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {
    public String TAG = "Principal";
    private ListView listView;
    private List<MyData> list;
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
                    textito.setText(object);
                    Log.d(TAG, object);

                }
                else{
                    textito.setText("oops");
                }
            }

        }

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
}