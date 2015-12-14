package ec.edu.espol.hidrantescerca;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

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
                " obs text, " +
                " fecha_crea text, " +
                " fecha_mod text, " +
                " fecha_insp text, " +
                " fecha_man text, " +
                " usuario_crea text, " +
                " usuario_mod text " +
                " ) ";
        db.execSQL(sqlHidrante);
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
        values.put("fecha_crea", hidrante.getFecha_crea());
        values.put("fecha_mod", hidrante.getFecha_mod());
        values.put("fecha_insp", hidrante.getFecha_insp());
        values.put("fecha_man", hidrante.getFecha_man());
        values.put("usuario_crea", hidrante.getUsuario_crea());
        values.put("usuario_mod", hidrante.getUsuario_mod());
        long rowID = mDB.insert("Hidrantes", null, values);
        return rowID;
    }

    public Cursor obtenerHidrantes(){
        return mDB.rawQuery("SELECT * FROM HIDRANTES", null);
    }

    public Cursor borrarTodosLosHidrantes(){
        return mDB.rawQuery("DELETE FROM HIDRANTES", null);
    }

    public void cerrar(){
        mDB.close();
    }
}
