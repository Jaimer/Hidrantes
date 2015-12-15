package ec.edu.espol.hidrantescerca.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import ec.edu.espol.hidrantescerca.Entidades.Hidrante;

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
                " ( _id integer primary key autoincrement , " +
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
        values.put("nombre", hidrante.getNombre());
        values.put("posicion", hidrante.getPosicion());
        values.put("estado", ""+hidrante.getEstado());
        values.put("psi", hidrante.getPsi());
        values.put("t4", hidrante.getTomas_4());
        values.put("t25", hidrante.getTomas2_5());
        values.put("acople", hidrante.getAcople());
        values.put("foto", hidrante.getFoto());
        values.put("obs", hidrante.getObservacion());
        long rowID = mDB.insert("Hidrantes", null, values);
        return rowID;
    }

    public Cursor getHidrantes(){
        return mDB.rawQuery("SELECT * FROM HIDRANTES", null);
    }

    public Cursor getHidrantePorId(int id){ return mDB.rawQuery("SELECT * FROM HIDRANTES WHERE _id = "+id, null);}

    public Cursor borrarTodosLosHidrantes(){
        return mDB.rawQuery("DELETE FROM HIDRANTES", null);
    }

    public void cerrar(){
        mDB.close();
    }
}
