package com.example.myappkitzia.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class BDInfo extends BDTablas{
    Context context;
    public BDInfo(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public boolean checarInfo(int Id){
        BDTablas dbPagina = new BDTablas(context);
        SQLiteDatabase db = dbPagina.getWritableDatabase();

        boolean Info;
        Cursor cursorInfo;

        cursorInfo = db.rawQuery("SELECT * FROM " + Tab_Info + " WHERE Id = " + Id + " LIMIT 1", null);

        if (cursorInfo.moveToFirst()) {
            Info = true;
        }else{
            Info = false;
        }

        cursorInfo.close();

        return Info;
    }

    public String verInfo(int Id){
        BDTablas dbPagina = new BDTablas(context);
        SQLiteDatabase db = dbPagina.getWritableDatabase();

        String Info = null;
        Cursor cursorInfo;

        cursorInfo = db.rawQuery("SELECT * FROM " + Tab_Info + " WHERE Id = " + Id + " LIMIT 1", null);

        if (cursorInfo.moveToFirst()) {
            Info = cursorInfo.getString(1);
        }

        cursorInfo.close();

        return Info;
    }

    public long insertarInfo(int Id, String textoC){
        long status = 0;

        try {
            BDTablas dbPagina = new BDTablas(context);
            SQLiteDatabase db = dbPagina.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("Id", Id);
            values.put("txtC", textoC);

            status = db.insert(Tab_Info, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return status;
    }

    public boolean editarInfo(int Id, String txtC){
        boolean correcto = false;

        BDTablas dbPagina = new BDTablas(context);
        SQLiteDatabase db = dbPagina.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + Tab_Info + " SET txtC = '" + txtC + "' WHERE Id = " + Id + "");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }
}
