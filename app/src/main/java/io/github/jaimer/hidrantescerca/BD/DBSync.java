package io.github.jaimer.hidrantescerca.BD;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import io.github.jaimer.hidrantescerca.Entidades.Hidrante;
import io.github.jaimer.hidrantescerca.Entidades.Movimiento;
import io.github.jaimer.hidrantescerca.Utils.Config;

/**
 * Created by jaime on 25/12/2015.
 */
public class DBSync extends AsyncTask<Context, Integer, String> {
    private SyncTaskCompleted listener;
    private ProgressDialog dialog;

    public DBSync(SyncTaskCompleted listener) {
        this.listener = listener;
        this.dialog = new ProgressDialog((Context)listener);
    }


    @Override
    protected String doInBackground(Context... params) {
        Context context = params[0];
        //Debug.waitForDebugger(); Debuggin'
        String response = null;
        String resultado = null;
        int localmovs;

        ArrayList<Movimiento> movimientos = new ArrayList<>();
        ArrayList<Hidrante> hidrantes = new ArrayList<>();

        LocalDB ldb = new LocalDB(context);
        localmovs = ldb.getMovRows();

        try {
            URL url = new URL(Config.DBsync+localmovs);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int status = conn.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    response = sb.toString();
            }
        } catch (MalformedURLException e) {
            Log.d("Error", "URL Malformada");
        } catch (ProtocolException e) {
            Log.d("Error", "Protocolo erróneo");
        } catch (IOException e) {
            Log.d("Error", "Error de Lectura");
        }

        try {
            JSONObject resp = new JSONObject(response);
            String mensaje = resp.getString("mensaje");
            if(mensaje.equals("Actualizacion Cliente")){

                JSONArray mData = resp.getJSONArray("Movimientos");

                // dialog.setMax(mData.length()*2); Para barra de prograso
                for(int i=0; i < mData.length(); i++){
                    JSONObject mObj = mData.getJSONObject(i);
                    Movimiento m = new Movimiento(
                            mObj.getInt("idmov"),
                            mObj.getInt("id_hidrante"),
                            mObj.getString("fecha_mod"),
                            mObj.getString("usuario_mod"));
                    movimientos.add(m);
                    // publishProgress(i); Para barra de prograso
                }

                JSONArray hData = resp.getJSONArray("Hidrantes");
                for(int i=0; i < hData.length(); i++){
                    JSONObject hObj = hData.getJSONObject(i);
                    String sfoto;
                    if(hObj.isNull("foto")){
                         sfoto = "";
                    }else{
                        sfoto = hObj.getString("foto");
                    }
                    byte[] foto = Base64.decode(sfoto, Base64.DEFAULT);
                    Hidrante h = new Hidrante(
                            hObj.getInt("_id"),
                            hObj.getString("nombre"),
                            hObj.getString("posicion"),
                            hObj.getString("estado").charAt(0),
                            hObj.getInt("psi"),
                            hObj.getInt("t4"),
                            hObj.getInt("t25"),
                            hObj.getString("acople"),
                            foto,
                            hObj.getString("obs"));
                    hidrantes.add(h);
                    // publishProgress(hData.length()+i); Para barra de prograso
                }
                if(movimientos.size() == hidrantes.size()){ //Deben haber la misma cantidad de movimientos e hidrantes
                    LocalDB db = new LocalDB(context);
                    for (int i = 0; i < hidrantes.size(); i++){
                        db.insertarMovimiento(movimientos.get(i));
                        db.insertarHidrante(hidrantes.get(i));
                    }
                }

                resultado = hidrantes.size()+" Hidrantes actualizados";
            }else{
                resultado = "Nada que sincronizar";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            resultado = "No se obtuvo respuesta del servidor";
        }

        return resultado;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Sincronizando");
        /* Barra de progreso determinada
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(false);
        */
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        listener.onSyncTaskCompleted(s);
    }

    /* Para barra de progreso determinada
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        dialog.setProgress(values[0]);
    }
    */
}
