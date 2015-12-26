package ec.edu.espol.hidrantescerca.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;

import ec.edu.espol.hidrantescerca.BD.LocalDB;
import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.R;

public class NuevoHidrante extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean fotoTomada = false;

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
        LatLng posicion = new LatLng(Double.parseDouble(lat.getText().toString()),Double.parseDouble(lng.getText().toString()));
        EditText acople = (EditText)findViewById(R.id.txt_acople);
        ImageView foto = (ImageView)findViewById(R.id.img_foto);
        byte[] imagen = null;
        if(fotoTomada){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)foto.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imagen = stream.toByteArray();
        }
        EditText obs = (EditText)findViewById(R.id.txt_obs);

        Hidrante hidrante = new Hidrante(nombre.getText().toString(),
                lat.getText().toString()+lng.getText().toString(),
                estado.getSelectedItem().toString().charAt(0),
                Integer.valueOf(psi.getText().toString()),
                Integer.valueOf(tomas4.getText().toString()),
                Integer.valueOf(tomas2_5.getText().toString()),
                acople.getText().toString(),
                imagen,
                obs.getText().toString()
        );
        LocalDB db = new LocalDB(this);
        if(db.insertarHidrante(hidrante) != 0){
            new AlertDialog.Builder(this)
                    .setTitle("Guardar")
                    .setMessage("Hidrante guardado exitósamente")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();//Cerrar
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Guardar")
                    .setMessage("Ocurrió un error al guardar, intente de nuevo")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // No hace nada
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
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
}
