package ec.edu.espol.hidrantescerca.Actividades;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import ec.edu.espol.hidrantescerca.BD.LocalDB;
import ec.edu.espol.hidrantescerca.Entidades.Marcador;
import ec.edu.espol.hidrantescerca.R;

public class Lista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LocalDB db = new LocalDB(this);
        final ArrayList<Marcador> marcadores = obtenerDistancias(db.getMarcadores());
        Collections.sort(marcadores, new Marcador());//Ordenar por distancia
        if(!marcadores.isEmpty()){
            ListView lstv = (ListView)findViewById(R.id.lst_hidrantes);
            ArrayAdapter<Marcador> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, marcadores);
            lstv.setAdapter(adapter);
            lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Lista.this, DetallesHidrante.class);
                    LatLng pos = marcadores.get(position).getPosicion();
                    int _id;
                    try {
                        _id = marcadores.get(position).getId();
                    } catch (NumberFormatException e) {
                        _id = -1;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", _id);
                    bundle.putParcelable("pos", pos);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            });
        }
    }

    public ArrayList<Marcador> obtenerDistancias(ArrayList<Marcador> marcadores){
        ArrayList<Marcador> activos = new ArrayList<>();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
            }
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        for(Marcador m : marcadores) {
            //if(m.getEstado() != 'E') { //No mostrar los eliminados. Se quita ya que despues no se puede administrar hidrantes eliminados.
                Location marcador = new Location("Marcador");
                marcador.setLatitude(m.getPosicion().latitude);
                marcador.setLongitude(m.getPosicion().longitude);
                marcador.setTime(new Date().getTime());
                m.setDistancia(location.distanceTo(marcador));
                activos.add(m);
            //}
        }
        return activos;
    }
}
