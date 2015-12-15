package ec.edu.espol.hidrantescerca.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Lista extends AppCompatActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Hidrante> hidrantes = cargarHidrantes();
        if(!hidrantes.isEmpty()){
            ListView lstv = (ListView)findViewById(R.id.lst_hidrantes);
            ArrayAdapter<Hidrante> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hidrantes);
            lstv.setAdapter(adapter);
        }
    }

    public void abrirMapaNuevo(View view){
        Intent intent = new Intent(this, Mapa.class);
        startActivity(intent);
    }

    private ArrayList<Hidrante> cargarHidrantes(){
       // LocalDB db = new LocalDB(this);
        ArrayList<Hidrante> Hidrantes = new ArrayList<>();

       // Cursor c = db.getHidrantes();

        //recorrer resultados del query
        if (c.moveToFirst()){//ver si hay resultados
            do {
                int id = c.getInt(0);
                String nombre = c.getString(1);
                Double lat = c.getDouble(2);
                Double lng = c.getDouble(3);
                char estado = c.getString(4).charAt(0);
                int presion = c.getInt(5);
                int t15 = c.getInt(6);
                int t25 = c.getInt(7);

                Hidrante hidrante = new Hidrante(id, nombre, new LatLng(lat, lng), estado, presion, t15, t25);
                Hidrantes.add(hidrante);
            }while (c.moveToNext());
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Atención")
                    .setMessage("No hay hidrantes registrados!")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // algo
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        db.cerrar();

        return Hidrantes;
    }*/
}
