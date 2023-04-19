package com.example.myappkitzia.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BDTablas extends SQLiteOpenHelper {
    private static final String BD_Nom = "datosPer.db";
    private static final int BD_ver = 1;
    public static final String Tab_Info = "tab_Info";
    public static final String Tab_Cuenta = "tab_Cuenta";
    public BDTablas(@Nullable Context context) {
        super(context, BD_Nom, null, BD_ver);
    }
    // clase con las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {
        //mentira, solo hay una jaja
        //mentira ahora son dos
        // create table Tab_Info (
        //	ID Integer primarykey not null,
        //  textC Text not null
        //)
        // create table Tab_Cuenta (
        //
        //
        db.execSQL("CREATE TABLE " + Tab_Info + "(" +
                "ID INTEGER PRIMARY KEY NOT NULL," +
                "txtC TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + Tab_Cuenta + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IdUsr INTEGER NOT NULL," +
                "IdCut INTEGER NOT NULL," +
                "txtCC TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Tab_Info);
        db.execSQL("DROP TABLE " + Tab_Cuenta);
        onCreate(db);
    }


}
