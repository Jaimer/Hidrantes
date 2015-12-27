package ec.edu.espol.hidrantescerca.Actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;

import ec.edu.espol.hidrantescerca.BD.InsHidranteRDBTaskCompleted;
import ec.edu.espol.hidrantescerca.BD.InsertarHidranteRDB;
import ec.edu.espol.hidrantescerca.BD.LocalDB;
import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.Entidades.Movimiento;
import ec.edu.espol.hidrantescerca.R;
import ec.edu.espol.hidrantescerca.Utils.Utils;

public class NuevoHidrante extends AppCompatActivity implements InsHidranteRDBTaskCompleted{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean fotoTomada = false;
    private Hidrante hidrante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_hidrante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng pos = bundle.getParcelable("pos");
        EditText lat = (EditText)findViewById(R.id.txt_lat);
        EditText lng = (EditText)findViewById(R.id.txt_lng);
        if (pos != null){
            lat.setText(Double.toString(pos.latitude));
            lng.setText(Double.toString(pos.longitude));
        }else{
            lat.setText(Double.toString(0));
            lng.setText(Double.toString(0));
        }

    }

    public void guardarHidrante(View view){

        EditText nombre = (EditText)findViewById(R.id.txt_nombre);
        EditText lat = (EditText)findViewById(R.id.txt_lat);
        EditText lng = (EditText)findViewById(R.id.txt_lng);
        Spinner estado = (Spinner)findViewById(R.id.spn_estado);
        EditText tomas4 = (EditText)findViewById(R.id.txt_tomas4);
        EditText tomas2_5 = (EditText)findViewById(R.id.txt_tomas2_5);
        EditText psi = (EditText)findViewById(R.id.txt_presion);
        EditText acople = (EditText)findViewById(R.id.txt_acople);
        ImageView foto = (ImageView)findViewById(R.id.img_foto);
        byte[] imagen = new byte[0];
        if(fotoTomada){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)foto.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imagen = stream.toByteArray();
        }
        EditText obs = (EditText)findViewById(R.id.txt_obs);

        hidrante = new Hidrante(nombre.getText().toString(),
                lat.getText().toString()+lng.getText().toString(),
                estado.getSelectedItem().toString().charAt(0),
                Integer.valueOf(psi.getText().toString()),
                Integer.valueOf(tomas4.getText().toString()),
                Integer.valueOf(tomas2_5.getText().toString()),
                acople.getText().toString(),
                imagen,
                obs.getText().toString()
        );
        new InsertarHidranteRDB(this).execute(hidrante);

    }

    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageview = (ImageView)findViewById(R.id.img_foto);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageview.setImageBitmap(imageBitmap);
            fotoTomada = true;
        }
    }

    @Override
    public void onInsHidranteRDBTaskCompleted(Movimiento movimiento, String estado) {
        if(estado.equals("1")){
            LocalDB ldb = new LocalDB(this);
            this.hidrante.setId(movimiento.getId_hidrante());
            //ldb.insertarHidrante(this.hidrante);
            //ldb.insertarMovimiento(movimiento);
            Utils.alerta("Guardar", "Hidrante guardado", this);
        }else{
            Utils.alerta("Guardar", "Error al guardar Hifdrante", this);
        }
    }
}
