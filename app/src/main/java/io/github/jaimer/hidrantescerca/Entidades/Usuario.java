package io.github.jaimer.hidrantescerca.Entidades;

/**
 * Created by jaime on 14/12/2015.
 */
public class Usuario {

    private String id_cedula;
    private String nombre;
    private String apellido;
    private int tipo;
    private String institucion;
    private String cargo;
    private String password;
    private char estado;
    private String email;

    public Usuario(String id_cedula, String nombre, String apellido, int tipo, String institucion, String cargo, String password, char estado, String email) {
        this.id_cedula = id_cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipo = tipo;
        this.institucion = institucion;
        this.cargo = cargo;
        this.password = password;
        this.estado = estado;
        this.email = email;
    }

    public String getId_cedula() {
        return id_cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getTipo() {
        return tipo;
    }

    public String getInstitucion() {
        return institucion;
    }

    public String getCargo() {
        return cargo;
    }

    public String getPassword() {
        return password;
    }

    public char getEstado() {
        return estado;
    }

    public String getEmail() {
        return email;
    }

    public void setId_cedula(String id_cedula) {
        this.id_cedula = id_cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id_cedula='" + id_cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", tipo=" + tipo +
                ", institucion='" + institucion + '\'' +
                ", cargo='" + cargo + '\'' +
                ", password='" + password + '\'' +
                ", estado=" + estado +
                ", email='" + email + '\'' +
                '}';
    }
}
