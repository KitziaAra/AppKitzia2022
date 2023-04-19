package com.example.myappkitzia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myappkitzia.Recursos.DesUtil;
import com.example.myappkitzia.Recursos.EncripBitMap;
import com.example.myappkitzia.Recursos.MyData;
import com.example.myappkitzia.Recursos.MyInfo;
import com.example.myappkitzia.SQLite.BDCuenta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Agregar extends AppCompatActivity {
    public String archivo = "";
    public static final String KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg";
    public EditText sitio, contra;
    public RadioButton opc1, opc2, opc3, opc4, opc5;
    public ImageView ivFoto, tomaFoto;
    private List<MyData> list;
    public Button guarda;
    public String usr;
    public String TAG = "Agregar";
    public DesUtil desUtil;
    private int[] logos = {R.mipmap.img, R.mipmap.ranni3, R.mipmap.img_2};
    public String txtCifr1, txtCfr2, json;
    private Uri imagenUri;
    private Bitmap imageP;

    private int TOMAR_FOTO = 100;
    private int SELEC_IMAGEN = 200;

    private String CARPETA_RAIZ = "MyPaginaWebFotos";
    private String CARPETAS_IMAGENES = "imagenes";
    private String RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        sitio = findViewById(R.id.editSitio);
        contra = findViewById(R.id.editContra);
        guarda = findViewById(R.id.button);
        opc1 = (RadioButton) findViewById(R.id.opc1);
        opc2 = (RadioButton) findViewById(R.id.opc2);
        opc3 = (RadioButton) findViewById(R.id.opc3);
        opc4 = (RadioButton) findViewById(R.id.opc4);
        opc5 = (RadioButton) findViewById(R.id.opc5);
        tomaFoto = findViewById(R.id.tomaFoto);
        ivFoto = findViewById(R.id.selecFoto);

        imageP = null;

        int numArchivo = getIntent().getExtras().getInt("numArchivo");
        int numContext = getIntent().getExtras().getInt("numContext");

        if (ContextCompat.checkSelfPermission(Agregar.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Agregar.this,
                    new String[]{android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        try {
            if (numContext == 2) {
                int numArchivoCuenta = getIntent().getExtras().getInt("numArchivoCuenta");
                BDCuenta dbCuenta = new BDCuenta(Agregar.this);
                String textito = dbCuenta.verCuenta(numArchivo, numArchivoCuenta);

                json2List(textito);
                EncripBitMap EBM = new EncripBitMap();
                for (MyData myData : list){
                    String valorAccountName = myData.getNombre();
                    String valorAccountPassword = myData.getPswd();
                    boolean valorAccountTipo = myData.isTipo();
                    int valorAccountImage = myData.getImage();

                    sitio.setText(valorAccountName);
                    contra.setText(valorAccountPassword);
                    if (valorAccountTipo != true) {
                        if (valorAccountImage == logos[0]) {
                            opc1.setChecked(true);
                        }
                        if (valorAccountImage == logos[1]) {
                            opc2.setChecked(true);
                        }
                        if (valorAccountImage == logos[2]) {
                            opc3.setChecked(true);
                        }

                    } else {
                        imageP = EBM.desCifrar(myData.getImageP());
                        opc5.setChecked(true);
                        ivFoto.setImageBitmap(imageP);
                    }
                }
            }
        } catch (Exception e) {
        }

        opc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarBoton(1);
            }
        });

        opc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarBoton(2);
            }
        });

        opc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarBoton(3);
            }
        });

        opc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarBoton(4);
            }
        });

        opc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarBoton(5);
            }
        });

        tomaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });

        ivFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });
    }

    public void guardar(View view) {
        int numArchivo = getIntent().getExtras().getInt("numArchivo");
        int numArchivoCuenta = getIntent().getExtras().getInt("numArchivoCuenta");
        int numContext = getIntent().getExtras().getInt("numContext");
        int numLista = getIntent().getExtras().getInt("numLista");

        if((false == opc1.isChecked() & false == opc2.isChecked() & false == opc3.isChecked() & false == opc4.isChecked() & false == opc5.isChecked()) || "".equals(sitio.getText().toString()) || "".equals(contra.getText().toString())) {
            Toast.makeText(Agregar.this, "Falta un parametro", Toast.LENGTH_SHORT).show();
        }
        else {
            if(sitio.length() > 22 || contra.length() > 30){
                String mensaje = "Parametro Erroneo";
                if(sitio.length() > 22){mensaje = "Nombre Muy Largo";}
                if(contra.length() > 30){mensaje = "Contraseña Muy Larga";}
                Toast.makeText(Agregar.this, mensaje, Toast.LENGTH_SHORT).show();
            }else {
                BDCuenta dbCuenta = new BDCuenta(Agregar.this);

                try {
                    String valorNombre = sitio.getText().toString();
                    String valorPassword = contra.getText().toString();
                    Location valorLocation = obtenerUbAc(Agregar.this);
                    if (numContext == 2){
                        String completoTexto = dbCuenta.verCuenta(numArchivo, numArchivoCuenta);
                        json2List(completoTexto);
                        for (MyData myData : list) {
                            valorLocation = myData.getLocation();
                        }
                    }
                    if (valorLocation != null) {
                        int valorImage = logos[0];
                        boolean valorTipo = false;
                        Bitmap valorImageP = null;
                        if (opc1.isChecked()) {
                            valorImage = logos[0];
                        }
                        if (opc2.isChecked()) {
                            valorImage = logos[1];
                        }
                        if (opc3.isChecked()) {
                            valorImage = logos[2];
                        }
                        if (opc4.isChecked()) {
                            valorImage = logos[2];
                        }
                        if (opc5.isChecked()) {
                            if(imageP != null) {
                                valorTipo = true;
                                valorImageP = imageP;
                            }
                        }

                        String textoJsonCuenta = lista2Json(valorNombre, valorPassword, valorLocation, valorTipo, valorImageP, valorImage);

                        if (numContext == 1) {
                            boolean BucleArchivo = true;
                            int x = 1;
                            while (BucleArchivo) {
                                if (dbCuenta.checarCuenta(numArchivo, x)) {
                                    x = x + 1;
                                } else {
                                    dbCuenta.insertarCuenta(numArchivo, x, textoJsonCuenta);
                                    while((numLista + 5) <= x){numLista += 5;}
                                    BucleArchivo = false;
                                }
                            }
                        }
                        if (numContext == 2) {
                            dbCuenta.editarCuenta(numArchivo, numArchivoCuenta, textoJsonCuenta);
                        }
                        Intent intent = new Intent(Agregar.this, Principal.class);
                        intent.putExtra("numArchivo", numArchivo);
                        intent.putExtra("numLista", numLista);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    Toast.makeText(Agregar.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void vaciar() {
        sitio.setText("");
        contra.setText("");
    }

    public String lista2Json(String sitio, String contra, Location location, boolean tipo, Bitmap imagen, int img) {
        MyData myData = null;
        Gson gson = null;
        String json = null;
        String mnsj = null;
        ArrayList list;

        myData = new MyData();
        myData.setNombre(sitio);
        myData.setPswd(contra);
        myData.setLocation(location);
        myData.setTipo(tipo);
        myData.setImageP(imagen.toString());
        myData.setImage(img);

        Log.d(TAG, "test");

        gson = new Gson();
        list = new ArrayList<MyData>();
        list.add(myData);
        json = gson.toJson(list, ArrayList.class);

        if (json != null) {
            Log.d(TAG, json);
            mnsj = "JSON OK";
        } else {
            mnsj = "Error JSON";
        }
        Toast.makeText(getApplicationContext(), mnsj, Toast.LENGTH_LONG).show();
        return json;
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

    public Location obtenerUbAc(Context context) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 3);
            return null;
        } else {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    if (location == null) {
                        LocationListener locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location loc) {
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        };
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location == null) {
                                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                                if (location == null) {
                                    location = new Location("");
                                }
                            }
                        }
                        locationManager.removeUpdates(locationListener);
                    }
                }
            }
            return location;
        }
    }

    public void tomarFoto() {

        String nombreImagen = "";
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();

        if (isCreada == false) {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada == true) {
            nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
        }

        path = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen;
        File imagen = new File(path);

        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authorities = this.getPackageName() + ".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities, imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }

        startActivityForResult(intent, TOMAR_FOTO);
    }

    public void seleccionarImagen() {
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeria, SELEC_IMAGEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        cambiarBoton(5);
        if(resultCode == RESULT_OK && requestCode == SELEC_IMAGEN) {
            imagenUri = data.getData();
            try {
                Bitmap imagen = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);
                imageP = imagen;
                ivFoto.setImageBitmap(imagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(resultCode == RESULT_OK && requestCode == TOMAR_FOTO) {
            MediaScannerConnection.scanFile(Agregar.this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {

                }
            });

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageP = bitmap;
            ivFoto.setImageBitmap(bitmap);
        }

    }

    public void cambiarBoton(int i) {
        opc1.setChecked(false);
        opc2.setChecked(false);
        opc3.setChecked(false);
        opc4.setChecked(false);
        opc5.setChecked(false);
        if(i == 1){opc1.setChecked(true);}
        if(i == 2){opc2.setChecked(true);}
        if(i == 3){opc3.setChecked(true);}
        if(i == 4){opc4.setChecked(true);}
        if(i == 5){opc5.setChecked(true);}
    }
}
