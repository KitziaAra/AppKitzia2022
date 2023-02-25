package com.example.myappkitzia.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BDTablas extends SQLiteOpenHelper {
    public static final String TAG = "BDTablas";

    private static final String BD_Nom = "datosPer.db";
    private static final int BD_ver = 1;
    public static final String TABLA = "DATOS";
    private static final String usuario = "USUARIO";
    private static final String contraseña = "CONTRA";
    private static final int edad = 0;
    private static final String fecha = "FECHA";
    private static final String correo = "CORREO";
    private static final String estacion = "ESTACION";
    private static final String genero = "GENERO";
    private static final String cafe = "CAFE";

    public BDTablas(@Nullable Context context) {
        super(context, BD_Nom, null, BD_ver);
    }
    // clase con las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {
        //mentira, solo hay una jaja
        // create table DATOS (
        //	usuario text not null
        //	contraseña text not null
        //	edad integer not null
        //	correo text not null
        //	fecha text not null
        //	estacion text not null
        //	genero text not null
        //	cafe text not null
        //)
        StringBuilder si = new StringBuilder();
        si.append("CREATE TABLE ");
        si.append(TABLA);
        si.append(" ( ");
        si.append(usuario);
        si.append(" TEXT NOT NULL, ");
        si.append(contraseña);
        si.append(" TEXT NOT NULL, ");
        si.append(edad);
        si.append(" INTEGER NOT NULL, ");
        si.append(fecha);
        si.append(" TEXT NOT NULL, ");
        si.append(correo);
        si.append(" TEXT NOT NULL, ");
        si.append(estacion);
        si.append(" TEXT NOT NULL, ");
        si.append(genero);
        si.append(" TEXT NOT NULL, ");
        si.append(cafe);
        si.append(" TEXT NOT NULL, ");
        si.append(" UNIQUE (");
        si.append(usuario);
        si.append(" ) )");
        Log.d(TAG, si.toString());

        String Qry = si.toString();

        db.execSQL(Qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLA);
        onCreate(db);
    }


}
