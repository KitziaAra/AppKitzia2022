package com.example.myappkitzia.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BDQuerys extends BDTablas{
    Context context;
    public BDQuerys(@Nullable Context context) {
        super(context);
    }

    public boolean checarCuenta(String usr, String pswd){
        BDTablas bdTablas = new BDTablas(context);
        SQLiteDatabase BD = bdTablas.getWritableDatabase();

        boolean cuenta;
        Cursor cursor;
        StringBuilder qry = new StringBuilder();

        //qry de consulta
        //select * from TABLA where USR = +USUARIO+ and pswd = +CONTRASEÑA+ limit 1

        qry.append("SELECT * FROM DATOS WHERE ");
        qry.append(usr);
        qry.append(" = + USUARIO + AND ");
        qry.append(pswd);
        qry.append(" = + CONTRA + LIMIT 1");

        cursor = BD.rawQuery(qry.toString(), null);

        if(cursor.moveToFirst()){
            cuenta = true;
        } else {
            cuenta = false;
        }
        cursor.close();
        return cuenta;
    }

    public ArrayList verCuenta(String usr, String pswd){
        BDTablas bdTablas = new BDTablas(context);
        SQLiteDatabase BD = bdTablas.getWritableDatabase();

        boolean cuenta;
        Cursor cursor;
        StringBuilder qry = new StringBuilder();
        ArrayList<String> cuentita = new ArrayList<String>();

        //qry de consulta
        //select * from TABLA where USR = +USUARIO+ and pswd = +CONTRASEÑA+ limit 1

        qry.append("SELECT * FROM DATOS WHERE ");
        qry.append(usr);
        qry.append(" = + USUARIO + AND ");
        qry.append(pswd);
        qry.append(" = + CONTRA + LIMIT 1");

        cursor = BD.rawQuery(qry.toString(), null);


        //orden de tablas/datos
        //usuario 1
        //contraseña 2
        //edad 3
        //fecha 4
        //correo 5
        //estacion 6
        //genero 7
        //cafe 8
        if(cursor.moveToFirst()){
            cuentita.add(cursor.getString(1)); //usuario
            cuentita.add(cursor.getString(2)); //contraseña
            cuentita.add(cursor.getString(3)); //edad
            cuentita.add(cursor.getString(4)); // fecha
            cuentita.add(cursor.getString(5)); //correo
            cuentita.add(cursor.getString(6)); //estacion
            cuentita.add(cursor.getString(7)); //genero
            cuentita.add(cursor.getString(8)); //cafe
        }

        cursor.close();
        return cuentita;
    }

    public long agregarCuenta(String usr, String contra, int edad, String fecha, String correo, String estacion, String genero, String cafe){
        long estado =0;

        try{
            BDTablas bdTablas = new BDTablas(context);
            SQLiteDatabase BD = bdTablas.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put("USUARIO", usr);
            valores.put("CONTRA", contra);
            valores.put("EDAD", edad);
            valores.put("FECHA", fecha);
            valores.put("CORREO", correo);
            valores.put("ESTACION", estacion);
            valores.put("GENERO", genero);
            valores.put("CAFE", cafe);

            estado = BD.insert(BDTablas.TABLA, null, valores);

        }
        catch (Exception ex){
            ex.toString();
        }
        return estado;
    }

    public boolean eliminarCuenta(String usr, String contra){
        boolean resultado;

        BDTablas bdTablas = new BDTablas(context);
        SQLiteDatabase BD = bdTablas.getWritableDatabase();

        StringBuilder qry = new StringBuilder();
        qry.append("DELETE FROM TABLA WHERE ");
        qry.append(usr);
        qry.append(" = + USUARIO + AND " );
        qry.append(contra);
        qry.append(" = + CONTRA +");
        //DELETE FROM DATOS WHERE USR = + USUARIO + AND PSWD = + CONTRA +
        try{
            BD.execSQL(qry.toString());
            resultado = true;
        }
        catch (Exception exception){
            exception.toString();
            resultado = false;
        }
        finally {
            BD.close();
        }
        return resultado;
    }
}
