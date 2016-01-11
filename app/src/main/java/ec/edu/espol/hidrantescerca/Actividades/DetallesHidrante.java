package ec.edu.espol.hidrantescerca.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;

import ec.edu.espol.hidrantescerca.BD.InsHidranteRDBTaskCompleted;
import ec.edu.espol.hidrantescerca.BD.InsertarHidranteRDB;
import ec.edu.espol.hidrantescerca.BD.LocalDB;
import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.Entidades.Movimiento;
import ec.edu.espol.hidrantescerca.R;

public class DetallesHidrante extends AppCompatActivity implements InsHidranteRDBTaskCompleted{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean fotoTomada = false;
    private Hidrante hidrante;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_hidrante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        id = bundle.getInt("id");
        if(id != -1){
            LocalDB db = new LocalDB(this);
            Hidrante hidrante = db.getHidrantePorId(id);
            if(hidrante.getFoto().length !=0){
                fotoTomada = true;
            }
            EditText nombre = (EditText)findViewById(R.id.txt_nombre);
            EditText lat = (EditText)findViewById(R.id.txt_lat);
            EditText lng = (EditText)findViewById(R.id.txt_lng);
            Spinner estado = (Spinner)findViewById(R.id.spn_estado);
            EditText tomas4 = (EditText)findViewById(R.id.txt_tomas4);
            EditText tomas2_5 = (EditText)findViewById(R.id.txt_tomas2_5);
            EditText psi = (EditText)findViewById(R.id.txt_presion);
            EditText acople = (EditText)findViewById(R.id.txt_acople);
            ImageView foto = (ImageView)findViewById(R.id.img_foto);
            EditText obs = (EditText)findViewById(R.id.txt_obs);

            nombre.setText(hidrante.getNombre());
            String[] latlng = hidrante.getPosicion().split("&");
            lat.setText(latlng[0]);
            lng.setText(latlng[1]);
            switch (hidrante.getEstado()){
                case 'A':
                    estado.setSelection(0);
                    break;
                case 'I':
                    estado.setSelection(1);
                    break;
                case 'E':
                    estado.setSelection(2);
                    break;
            }
            tomas4.setText(""+hidrante.getTomas_4());
            tomas2_5.setText(""+hidrante.getTomas2_5());
            psi.setText(""+hidrante.getPsi());
            acople.setText(hidrante.getAcople());
            if (hidrante.getFoto().length > 0) {
                Bitmap bmp = BitmapFactory.decodeByteArray(hidrante.getFoto(), 0, hidrante.getFoto().length);
                foto.setImageBitmap(bmp);
            }
            obs.setText(hidrante.getObservacion());
        }else{
            EditText lat = (EditText)findViewById(R.id.txt_lat);
            EditText lng = (EditText)findViewById(R.id.txt_lng);
            LatLng pos = bundle.getParcelable("pos");
            lat.setText(Double.toString(pos.latitude));
            lng.setText(Double.toString(pos.longitude));
        }
    }

    public void guardarHidrante(View view){

        //TODO: Validar que no haya cajas vacias. Poner texto por defecto a las cajas opcionales
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

        StringBuilder posicion = new StringBuilder();
        posicion.append(lat.getText().toString());
        posicion.append("&");
        posicion.append(lng.getText().toString());

        if(id == -1) {
            hidrante = new Hidrante(
                    nombre.getText().toString(),
                    posicion.toString(),
                    estado.getSelectedItem().toString().charAt(0),
                    Integer.valueOf(psi.getText().toString()),
                    Integer.valueOf(tomas4.getText().toString()),
                    Integer.valueOf(tomas2_5.getText().toString()),
                    acople.getText().toString(),
                    imagen,
                    obs.getText().toString()
            );
        }else{
            hidrante = new Hidrante(
                    id,
                    nombre.getText().toString(),
                    posicion.toString(),
                    estado.getSelectedItem().toString().charAt(0),
                    Integer.valueOf(psi.getText().toString()),
                    Integer.valueOf(tomas4.getText().toString()),
                    Integer.valueOf(tomas2_5.getText().toString()),
                    acople.getText().toString(),
                    imagen,
                    obs.getText().toString()
            );
        }
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
            ldb.insertarHidrante(this.hidrante);
            ldb.insertarMovimiento(movimiento);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Guardar")
                    .setMessage("Hidrante Guardado")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Slide slide = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                slide = new Slide();
                                slide.setDuration(500);
                                getWindow().setExitTransition(slide);
                            }
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }else{
            Toast.makeText(this, "Error al guardar el hidrante", Toast.LENGTH_LONG).show();
        }
    }
}
