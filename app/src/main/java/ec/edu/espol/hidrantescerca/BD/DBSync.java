package ec.edu.espol.hidrantescerca.BD;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
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

import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.Entidades.Movimiento;
import ec.edu.espol.hidrantescerca.Utils.Config;

/**
 * Created by jaime on 25/12/2015.
 */
public class DBSync extends AsyncTask<Context, Integer, String> {
    private SyncTaskCompleted listener;

    public DBSync(SyncTaskCompleted listener) {
        this.listener = listener;
    }


    @Override
    protected String doInBackground(Context... params) {
        Context context = params[0];
        //Debug.waitForDebugger();
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
                for(int i=0; i < mData.length(); i++){
                    JSONObject mObj = mData.getJSONObject(i);
                    Movimiento m = new Movimiento(
                            mObj.getInt("idmov"),
                            mObj.getInt("id_hidrante"),
                            mObj.getString("fecha_mod"),
                            mObj.getString("usuario_mod"));
                    movimientos.add(m);
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onSyncTaskCompleted(s);
    }
}
