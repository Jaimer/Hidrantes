package ec.edu.espol.hidrantescerca.Entidades;

/**
 * Created by jaime on 14/12/2015.
 */
public class Movimiento {
    private int idmov;
    private int id_hidrante;
    private String fecha_mod;
    private String usuario_mod;

    public Movimiento(int idmov, int id_hidrante, String fecha_mod, String usuario_mod) {
        this.idmov = idmov;
        this.id_hidrante = id_hidrante;
        this.fecha_mod = fecha_mod;
        this.usuario_mod = usuario_mod;
    }

    public Movimiento(int id_hidrante, String fecha_mod, String usuario_mod) {
        this.id_hidrante = id_hidrante;
        this.fecha_mod = fecha_mod;
        this.usuario_mod = usuario_mod;
    }

    public int getIdmov() {
        return idmov;
    }

    public void setIdmov(int idmov) {
        this.idmov = idmov;
    }

    public int getId_hidrante() {
        return id_hidrante;
    }

    public void setId_hidrante(int id_hidrante) {
        this.id_hidrante = id_hidrante;
    }

    public String getFecha_mod() {
        return fecha_mod;
    }

    public void setFecha_mod(String fecha_mod) {
        this.fecha_mod = fecha_mod;
    }

    public String getUsuario_mod() {
        return usuario_mod;
    }

    public void setUsuario_mod(String usuario_mod) {
        this.usuario_mod = usuario_mod;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "idmov=" + idmov +
                ", id_hidrante=" + id_hidrante +
                ", fecha_mod='" + fecha_mod + '\'' +
                ", usuario_mod='" + usuario_mod + '\'' +
                '}';
    }
}
