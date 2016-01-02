package ec.edu.espol.hidrantescerca.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import java.util.ArrayList;

import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.Entidades.Movimiento;

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
        return hidrantes;
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
        return movimientos;
    }

    public void cerrar(){
        mDB.close();
    }
}
