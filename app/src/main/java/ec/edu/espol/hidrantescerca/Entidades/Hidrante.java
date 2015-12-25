package ec.edu.espol.hidrantescerca.Entidades;


import java.util.Arrays;

/**
 * Created by jmoscoso on 26/10/2015.
 */
public class Hidrante {

    private int _id;
    private String nombre;
    private String posicion;
    private char estado;
    private int psi;
    private int t4;
    private int t25;
    private String acople;
    private byte[] foto;
    private String obs;



    public Hidrante(int id, String nombre, String posicion, char estado, int psi, int tomas_4, int tomas2_5, String acople, byte[] foto, String observacion) {
        this._id = id;
        this.nombre = nombre;
        this.posicion = posicion;
        this.estado = estado;
        this.psi = psi;
        this.t4 = tomas_4;
        this.t25 = tomas2_5;
        this.acople = acople;
        this.foto = foto;
        this.obs = observacion;
    }

    public Hidrante(String nombre, String posicion, char estado, int psi, int tomas_4, int tomas2_5, String acople, byte[] foto, String observacion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.estado = estado;
        this.psi = psi;
        this.t4 = tomas_4;
        this.t25 = tomas2_5;
        this.acople = acople;
        this.foto = foto;
        this.obs = observacion;
    }


    public int getId() {
        return _id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public char getEstado() {
        return estado;
    }

    public int getPsi() {
        return psi;
    }

    public int getTomas_4() {
        return t4;
    }

    public int getTomas2_5() {
        return t25;
    }

    public String getAcople() {
        return acople;
    }

    public byte[] getFoto() {
        return foto;
    }

    public String getObservacion() {
        return obs;
    }


    public void setId(int id) {
        this._id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public void setPsi(int psi) {
        this.psi = psi;
    }

    public void setTomas_4(int tomas_4) {
        this.t4 = tomas_4;
    }

    public void setTomas2_5(int tomas2_5) {
        this.t25 = tomas2_5;
    }

    public void setAcople(String acople) {
        this.acople = acople;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Hidrante{" +
                "_id=" + _id +
                ", nombre='" + nombre + '\'' +
                ", posicion='" + posicion + '\'' +
                ", estado=" + estado +
                ", psi=" + psi +
                ", t4=" + t4 +
                ", t25=" + t25 +
                ", acople='" + acople + '\'' +
                ", foto=" + Arrays.toString(foto) +
                ", obs='" + obs + '\'' +
                '}';
    }
}