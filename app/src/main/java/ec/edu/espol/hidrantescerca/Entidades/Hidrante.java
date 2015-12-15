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
    private String fecha_crea;
    private String fecha_mod;
    private String fecha_insp;
    private String fecha_man;
    private String usuario_crea;
    private String usuario_mod;



    public Hidrante(int id, String nombre, String posicion, char estado, int psi, int tomas_4, int tomas2_5, String acople, byte[] foto, String observacion, String fecha_crea, String fecha_mod, String fecha_insp, String fecha_man, String usuario_crea, String usuario_mod) {
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
        this.fecha_crea = fecha_crea;
        this.fecha_mod = fecha_mod;
        this.fecha_insp = fecha_insp;
        this.fecha_man = fecha_man;
        this.usuario_crea = usuario_crea;
        this.usuario_mod = usuario_mod;
    }

    public Hidrante(String nombre, String posicion, char estado, int psi, int tomas_4, int tomas2_5, String acople, byte[] foto, String observacion, String fecha_crea, String fecha_mod, String fecha_insp, String fecha_man, String usuario_crea, String usuario_mod) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.estado = estado;
        this.psi = psi;
        this.t4 = tomas_4;
        this.t25 = tomas2_5;
        this.acople = acople;
        this.foto = foto;
        this.obs = observacion;
        this.fecha_crea = fecha_crea;
        this.fecha_mod = fecha_mod;
        this.fecha_insp = fecha_insp;
        this.fecha_man = fecha_man;
        this.usuario_crea = usuario_crea;
        this.usuario_mod = usuario_mod;
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

    public String getFecha_crea() {
        return fecha_crea;
    }

    public String getFecha_mod() {
        return fecha_mod;
    }

    public String getFecha_insp() {
        return fecha_insp;
    }

    public String getFecha_man() {
        return fecha_man;
    }

    public String getUsuario_crea() {
        return usuario_crea;
    }

    public String getUsuario_mod() {
        return usuario_mod;
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

    public void setObservacion(String observacion) {
        this.obs = observacion;
    }

    public void setFecha_crea(String fecha_crea) {
        this.fecha_crea = fecha_crea;
    }

    public void setFecha_mod(String fecha_mod) {
        this.fecha_mod = fecha_mod;
    }

    public void setFecha_insp(String fecha_insp) {
        this.fecha_insp = fecha_insp;
    }

    public void setFecha_man(String fecha_man) {
        this.fecha_man = fecha_man;
    }

    public void setUsuario_crea(String usuario_crea) {
        this.usuario_crea = usuario_crea;
    }

    public void setUsuario_mod(String usuario_mod) {
        this.usuario_mod = usuario_mod;
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
                ", fecha_crea='" + fecha_crea + '\'' +
                ", fecha_mod='" + fecha_mod + '\'' +
                ", fecha_insp='" + fecha_insp + '\'' +
                ", fecha_man='" + fecha_man + '\'' +
                ", usuario_crea='" + usuario_crea + '\'' +
                ", usuario_mod='" + usuario_mod + '\'' +
                '}';
    }
}