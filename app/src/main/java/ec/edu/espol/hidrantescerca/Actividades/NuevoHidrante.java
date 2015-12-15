package ec.edu.espol.hidrantescerca.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.gms.maps.model.LatLng;

public class NuevoHidrante extends AppCompatActivity {
/*
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
        EditText tomas1_5 = (EditText)findViewById(R.id.txt_tomas1_5);
        EditText tomas2_5 = (EditText)findViewById(R.id.txt_tomas2_5);
        EditText psi = (EditText)findViewById(R.id.txt_presion);
        LatLng posicion = new LatLng(Double.parseDouble(lat.getText().toString()),Double.parseDouble(lng.getText().toString()));

        Hidrante hidrante = new Hidrante(nombre.getText().toString(),posicion,estado.getSelectedItem().toString().charAt(0),Integer.parseInt(psi.getText().toString()),Integer.parseInt(tomas1_5.getText().toString()),Integer.parseInt(tomas2_5.getText().toString()));
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
    }*/
}
