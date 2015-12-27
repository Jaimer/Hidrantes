package ec.edu.espol.hidrantescerca.BD;

import android.content.Context;
import android.os.AsyncTask;
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
        String response = null;
            //TODO: Cambiar la forma de obtener los cambios. Enviar la cantidad de movimientos locales al servidor, y que el servidor calcule la diferencia y devuelva los movimientos faltantes
        try {
            URL url = new URL(Config.getMovURL+"?TRA=ROW");
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

        int localmovs, remotemovs = 0;

        LocalDB ldb = new LocalDB(context);
        localmovs = ldb.getMovRows();

        if(localmovs != -1){
            try {
                JSONObject resp = new JSONObject(response);
                JSONArray data = resp.getJSONArray("Movimientos");
                JSONObject obj = data.getJSONObject(0);
                remotemovs = obj.getInt("Filas");
                Log.d("Movs de JSON",""+remotemovs);
            }catch (JSONException e){
                e.printStackTrace();
            }

            if(remotemovs > localmovs){
                int cambios = remotemovs - localmovs;

            }
        }else{
            Log.d("Error", "No se obtuvieron movimientos");
        }


        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onSyncTaskCompleted();


    }
}
