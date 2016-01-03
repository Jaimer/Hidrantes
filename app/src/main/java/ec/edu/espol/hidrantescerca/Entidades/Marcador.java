package ec.edu.espol.hidrantescerca.Entidades;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import ec.edu.espol.hidrantescerca.R;

/**
 * Created by jaime on 3/1/2016.
 */
public class Marcador {
    private int id;
    private String titulo;
    private LatLng posicion;
    private BitmapDescriptor icono = BitmapDescriptorFactory.fromResource(R.mipmap.ic_hidrante);

    public Marcador(int id, String titulo, LatLng posicion) {
        this.id = id;
        this.titulo = titulo;
        this.posicion = posicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LatLng getPosicion() {
        return posicion;
    }

    public void setPosicion(LatLng posicion) {
        this.posicion = posicion;
    }

    public BitmapDescriptor getIcono() {
        return icono;
    }

    public void setIcono(BitmapDescriptor icono) {
        this.icono = icono;
    }
}
