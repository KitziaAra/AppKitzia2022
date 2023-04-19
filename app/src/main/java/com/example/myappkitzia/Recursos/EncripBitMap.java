package com.example.myappkitzia.Recursos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class EncripBitMap implements Serializable {

    public static String cifrar(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedBitmap = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encodedBitmap;
    }

    public static Bitmap desCifrar(String encodedBitmap) {
        byte[] byteArray = Base64.decode(encodedBitmap, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return bitmap;
    }
}
