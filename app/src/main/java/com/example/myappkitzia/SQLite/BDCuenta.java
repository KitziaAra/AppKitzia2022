package com.example.myappkitzia.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BDCuenta extends BDTablas{
    Context context;
    public BDCuenta(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public boolean checarCuenta(int IdUsr, int IdCut){
        BDTablas BD =new BDTablas(context);
        SQLiteDatabase bd = BD.getWritableDatabase();

        boolean Cuenta;
        Cursor cursor;

        cursor = bd.rawQuery("SELECT FROM " +Tab_Cuenta + " WHERE IdUsr = " + IdUsr + " and IdCut = " + IdCut + " LIMIT 1",null );

        if (cursor.moveToFirst()){
            Cuenta = true;
        }
        else {
            Cuenta = false;
        }
        cursor.close();
        return Cuenta;
    }

    public String verCuenta(int IdUsr, int IdCut){
        BDTablas dbPagina = new BDTablas(context);
        SQLiteDatabase db = dbPagina.getWritableDatabase();

        String Cuenta = null;
        Cursor cursorCuenta;

        cursorCuenta = db.rawQuery("SELECT * FROM " + Tab_Cuenta + " WHERE IdUsr = " + IdUsr + " and IdCut = " + IdCut + " LIMIT 1", null);

        if (cursorCuenta.moveToFirst()) {
            Cuenta = cursorCuenta.getString(3);
        }

        cursorCuenta.close();

        return Cuenta;
    }


    public long insertarCuenta(int IdUsr, int IdCut, String textoCC){
        long status = 0;

        try {
            BDTablas dbPagina = new BDTablas(context);
            SQLiteDatabase db = dbPagina.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("idUsr", IdUsr);
            values.put("idCut", IdCut);
            values.put("txtCC", textoCC);

            status = db.insert(Tab_Cuenta, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return status;
    }

    public boolean editarCuenta(int IdUsr, int IdCut, String textoCC){
        boolean correcto = false;

        BDTablas dbPagina = new BDTablas(context);
        SQLiteDatabase db = dbPagina.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + Tab_Cuenta + " SET txtCC = '" + textoCC + "' WHERE IdUsr = " + IdUsr + " and IdCut = " + IdCut + "");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;

    }

    public boolean borrarCuenta(int Id_Usr, int IdCut){
        boolean correcto = false;

        BDTablas dbPagina = new BDTablas(context);
        SQLiteDatabase db = dbPagina.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + Tab_Cuenta + " WHERE IdUsr = " + Id_Usr + " and IdCut = " + IdCut + "");
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