package io.github.jaimer.hidrantescerca.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.github.jaimer.hidrantescerca.BD.DBSync;
import io.github.jaimer.hidrantescerca.BD.LocalDB;
import io.github.jaimer.hidrantescerca.BD.SyncTaskCompleted;
import io.github.jaimer.hidrantescerca.Entidades.Marcador;
import io.github.jaimer.hidrantescerca.R;
import io.github.jaimer.hidrantescerca.Utils.Config;

public class Mapa extends AppCompatActivity implements SyncTaskCompleted, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<Marcador> marcadores = new ArrayList<>();
    private LocalDB ldb;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest = new LocationRequest();
    private Boolean mRequestingLocationUpdates = true;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    protected String mLastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Config config = new Config(this);
        mRequestingLocationUpdates = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
        }

        this.ldb = new LocalDB(this);

        setUpMapIfNeeded();
        sincronizar();
        mMap.getUiSettings().setMapToolbarEnabled(false);

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
                Intent intent = new Intent(Mapa.this, DetallesHidrante.class);
                LatLng pos = marker.getPosition();
                int id;
                try {
                    id = Integer.valueOf(marker.getTitle().split(" | ")[0]);
                } catch (NumberFormatException e) {
                    id = -1;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putParcelable("pos", pos);
                bundle.putParcelable("userpos", mLastLocation);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        setUpMapIfNeeded();
        dibujarHidrantes();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mLastLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
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

         if (mLastLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void abrirLista(MenuItem menuItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("userpos", mLastLocation);
        Intent intent = new Intent(this, Lista.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    public void abrirAjustes(MenuItem menuItem){
        Intent intent = new Intent(this, AjustesActivity.class);
        startActivity(intent);
    }

    public void sincronizar(MenuItem menuItem) {
        if(ldb.usersExist() > 0){
            new DBSync(this).execute(this);
        }else{
            Toast.makeText(this, "Usuario no autorizado", Toast.LENGTH_LONG).show();
        }
    }

    public void sincronizar() {
        if(ldb.usersExist() > 0){
            new DBSync(this).execute(this);
        }else{
            Toast.makeText(this, "Usuario no autorizado", Toast.LENGTH_LONG).show();
        }
    }

    public void masCercano(View view) {//Quise implementar un metodo parecido a la actividad lista pero salia al reves ???? Lo dejo como estaba.
        Toast.makeText(this, "Buscando Hidrante activo mas cercano", Toast.LENGTH_LONG).show();
        if(mLastLocation != null){
            int minIndex = -1;
            float minDist = Float.MAX_VALUE;
            int size = this.marcadores.size();
            float distancia;
            for(int i = 0; i < size; i++){
                Marcador m = this.marcadores.get(i);
                if(m.getEstado() == 'A') {
                    Location marcador = new Location("Marcador");
                    marcador.setLatitude(m.getPosicion().latitude);
                    marcador.setLongitude(m.getPosicion().longitude);
                    marcador.setTime(new Date().getTime());
                    distancia = mLastLocation.distanceTo(marcador);
                    if (distancia < minDist) {
                        minDist = distancia;
                        minIndex = i;
                    }
                }
            }
            if(minIndex >= 0){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(marcadores.get(minIndex).getPosicion().latitude, marcadores.get(minIndex).getPosicion().longitude), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(marcadores.get(minIndex).getPosicion().latitude, marcadores.get(minIndex).getPosicion().longitude))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }
    }

    public void dibujarHidrantes() {
        mMap.clear();
        this.marcadores = ldb.getMarcadores();
        if (!this.marcadores.isEmpty()) {
            for (Marcador m : this.marcadores) {
                if (m.getEstado() != 'E') {
                    mMap.addMarker(new MarkerOptions()
                            .position(m.getPosicion())
                            .title(m.toString())
                            .icon(m.getIcono()));
                }
            }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onSyncTaskCompleted(String resultado) {
        Toast.makeText(this, "Sincronización: "+resultado, Toast.LENGTH_LONG).show();
        dibujarHidrantes();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mLastLocation == null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            setUpMap();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
            setUpMap();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "No se pudo conectar a GoogleAPI", Toast.LENGTH_SHORT).show();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        setUpMap();
    }
}
