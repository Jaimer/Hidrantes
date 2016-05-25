package io.github.jaimer.hidrantescerca.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.github.jaimer.hidrantescerca.Entidades.Hidrante;
import io.github.jaimer.hidrantescerca.Entidades.Marcador;
import io.github.jaimer.hidrantescerca.Entidades.Movimiento;
import io.github.jaimer.hidrantescerca.Entidades.Usuario;

/**
 * Created by jmoscoso on 27/10/2015.
 */
public class LocalDB extends SQLiteOpenHelper {
    SQLiteDatabase mDB = getWritableDatabase();

    public LocalDB(Context context){
        super(context, "hidrantescerca", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlHidrante = "create table Hidrantes" +
                " ( _id integer primary key, " +
                " nombre text , " +
                " posicion text , " +
                " estado char, " +
                " psi int, " +
                " t4 int, " +
                " t25 int, " +
                " acople text, " +
                " foto blob, " +
                " obs text " +
                " ) ";
        db.execSQL(sqlHidrante);

        String sqlUsuarios = "create table Usuario" +
                " ( id_cedula text primary key, " +
                " nombre text, " +
                " apellido text, " +
                " tipo integer, " +
                " institucion text, " +
                " cargo text, " +
                " password text, " +
                " estado char, " +
                " email text " +
                " )";
        db.execSQL(sqlUsuarios);

        String sqlMovimientos = "create table Movimientos" +
                "(idmov integer primary key, " +
                " id_hidrante integer, " +
                " fecha_mod text, " +
                " usuario_mod, " +
                " FOREIGN KEY(id_hidrante) REFERENCES Hidrantes(_id), " +
                " FOREIGN KEY(usuario_mod) REFERENCES Usuario(id_cedula) " +
                " ) ";
        db.execSQL(sqlMovimientos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table Hidrantes"); }
        catch (SQLiteException e) { }
        onCreate(db);
    }

    public long insertarHidrante(Hidrante hidrante){
        ContentValues values = new ContentValues();
        values.put("_id", hidrante.getId());
        values.put("nombre", hidrante.getNombre());
        values.put("posicion", hidrante.getPosicion());
        values.put("estado", ""+hidrante.getEstado());
        values.put("psi", hidrante.getPsi());
        values.put("t4", hidrante.getTomas_4());
        values.put("t25", hidrante.getTomas2_5());
        values.put("acople", hidrante.getAcople());
        values.put("foto", hidrante.getFoto());
        values.put("obs", hidrante.getObservacion());
        long rowID = mDB.insertWithOnConflict("Hidrantes", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return rowID;
    }

    public long insertarMovimiento(Movimiento movimiento){
        ContentValues values = new ContentValues();
        values.put("idmov", movimiento.getIdmov());
        values.put("id_hidrante", movimiento.getId_hidrante());
        values.put("fecha_mod", movimiento.getFecha_mod());
        values.put("usuario_mod", movimiento.getUsuario_mod());
        long rowID = mDB.insertWithOnConflict("Movimientos", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return rowID;
    }

    public ArrayList<Hidrante> getHidrantes(){
        ArrayList<Hidrante> hidrantes = new ArrayList<>();
        Cursor cursor = mDB.rawQuery("SELECT * FROM HIDRANTES", null);
        mDB.close();
        if(cursor != null){
            while (cursor.moveToNext()){
                Hidrante hidrante= new Hidrante(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3).charAt(0),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6),
                        cursor.getString(7),
                        cursor.getBlob(8),
                        cursor.getString(9));
                hidrantes.add(hidrante);
            }
        }
        cursor.close();
        return hidrantes;
    }

    public ArrayList<Marcador> getMarcadores(){
        ArrayList<Marcador> marcadores = new ArrayList<>();
        Cursor cursor = mDB.rawQuery("SELECT _id, nombre, posicion, estado FROM HIDRANTES", null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String[] latlng = cursor.getString(2).split("&");
                Marcador m = new Marcador(
                        cursor.getInt(0),
                        cursor.getString(1),
                        new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1])),
                        cursor.getString(3).charAt(0)
                );
                marcadores.add(m);
            }
        }
        cursor.close();
        return marcadores;
    }

    public Hidrante getHidrantePorId(int id){

        Cursor cursor = mDB.rawQuery("SELECT * FROM HIDRANTES WHERE _id = "+id, null);
        cursor.moveToFirst();
        Hidrante hidrante= new Hidrante(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3).charAt(0),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getString(7),
                cursor.getBlob(8),
                cursor.getString(9));
        cursor.close();
        return hidrante;
    }

    public Cursor borrarTodosLosHidrantes(){
        return mDB.rawQuery("DELETE FROM HIDRANTES", null);
    }

    public int getMovRows(){
        int movimientos = -1;
        Cursor cursor =  mDB.rawQuery("SELECT COUNT(*) FROM Movimientos", null);
        if (cursor != null){
            cursor.moveToFirst();
            movimientos = cursor.getInt(0);

        }
        cursor.close();
        return movimientos;
    }

    public long insertarUsuario(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("id_cedula", usuario.getId_cedula());
        values.put("nombre", usuario.getNombre());
        values.put("apellido", usuario.getApellido());
        values.put("tipo", usuario.getTipo());
        values.put("institucion", usuario.getInstitucion());
        values.put("cargo", usuario.getCargo());
        values.put("password", usuario.getPassword());
        values.put("estado", ""+usuario.getEstado());
        values.put("email", usuario.getEmail());
        long rowID = mDB.insertWithOnConflict("Usuario", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return rowID;
    }

    public int usersExist(){
        int users = 0;
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM Usuario", null);
        if(cursor != null){
            cursor.moveToFirst();
            users = cursor.getInt(0);
        }
        cursor.close();
        return users;
    }

    public Cursor borrarUsuarios(){
        return mDB.rawQuery("DELETE FROM USUARIO", null);
    }

    public void cerrar(){
        mDB.close();
    }
}
