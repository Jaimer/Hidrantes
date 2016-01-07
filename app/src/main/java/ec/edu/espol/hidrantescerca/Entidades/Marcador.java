package ec.edu.espol.hidrantescerca.Entidades;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;

import ec.edu.espol.hidrantescerca.R;

/**
 * Created by jaime on 3/1/2016.
 */
public class Marcador implements Comparator<Marcador>{
    private int id;
    private String titulo;
    private LatLng posicion;
    private BitmapDescriptor icono;
    private Float distancia;
    private char estado;

    public Marcador(int id, String titulo, LatLng posicion, char estado) {
        this.id = id;
        this.titulo = titulo;
        this.posicion = posicion;
        this.estado = estado;
        if(estado == 'A'){
            this.icono = BitmapDescriptorFactory.fromResource(R.mipmap.ic_hidrante);
        }else{
            this.icono = BitmapDescriptorFactory.fromResource(R.mipmap.ic_hidrante_black);
        }
    }

    public Marcador() {
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

    @Override
    public String toString() {
        return "" + id +  " | " + titulo + " | " + estado ;
    }

    @Override
    public int compare(Marcador lhs, Marcador rhs) {
        return lhs.getDistancia().compareTo(rhs.getDistancia());
    }

    public Float getDistancia() {
        return distancia;
    }

    public void setDistancia(Float distancia) {
        this.distancia = distancia;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }
}
