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
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ec.edu.espol.hidrantescerca.BD.LocalDB;
import ec.edu.espol.hidrantescerca.BD.RemoteDB;
import ec.edu.espol.hidrantescerca.BD.SyncTaskCompleted;
import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.R;
import ec.edu.espol.hidrantescerca.BD.RemoteDBTaskCompleted;
import ec.edu.espol.hidrantescerca.Utils.Utils;

public class Mapa extends AppCompatActivity implements RemoteDBTaskCompleted, SyncTaskCompleted {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ArrayList<Hidrante> Hidrantes = new ArrayList<>();
    RemoteDB rdb = new RemoteDB(this);
    LocalDB ldb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpMapIfNeeded();
        ldb = new LocalDB(this);
        Hidrantes = ldb.getHidrantes();

        //Dibuja un marcador en el punto donde el usuario toco el mapa y centra la vista a ese punto
        mMap.setOnMapClickListener(new OnMapClickListener() {

            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title("Toca para agregar");

                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_hidrante));

                // Clears the previously touched position
                mMap.clear();
                dibujarHidrantes();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                Marker marker = mMap.addMarker(markerOptions);

                marker.showInfoWindow();
            }
        });

        //Abre la ventana de mantenimiento cuando el usuario toca el titulo del marcador y pasa la posicion de este
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Mapa.this, NuevoHidrante.class);
                LatLng pos = marker.getPosition();
                Bundle bundle = new Bundle();
                bundle.putParcelable("pos", pos);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

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
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mMap.clear();
        dibujarHidrantes();
    }

    public void abrirLista(View view){
        Intent intent = new Intent(this, Lista.class);
        startActivity(intent);
    }

    public void dibujarHidrantes(){
        if(!Hidrantes.isEmpty()){
            for (Hidrante h : Hidrantes){
                String[] latlng = h.getPosicion().split(",");
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1])))
                        .title(h.getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_hidrante)));
            }
        } else{
            Utils.alerta("Error", "No hay hidrantes", this);
        }
    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public void onGetHidrantesCompleted() {
        Hidrantes = rdb.getHidrantes();
        dibujarHidrantes();
    }

    @Override
    public void onGetMovRowsCompleted() {
        int movimientos = rdb.getMovRows();
        Utils.alerta("Movimientos",""+movimientos,this);
    }

    @Override
    public void onSyncTaskCompleted() {

    }
}
