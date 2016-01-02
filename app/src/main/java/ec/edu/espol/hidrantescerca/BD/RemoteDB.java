package ec.edu.espol.hidrantescerca.BD;

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

import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.Utils.Config;

/**
 * Created by jaime on 6/12/2015.
 */
public class RemoteDB extends AsyncTask<String,Integer,String>{
    private String respuesta = null;
    private String accion = null;
    private RemoteDBTaskCompleted listener;

    public RemoteDB(RemoteDBTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        accion = params[0];
        String response = null;

        switch(accion){
            case "getHidrantes":
                try {
                    URL url = new URL(Config.getURL);
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
                break;
            case "setHidrante":
                try {
                    URL url = new URL(Config.getURL+"?TRA=ROW");
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
                break;


        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.respuesta = s;
        switch(accion) {
            case "getHidrantes":
                listener.onGetHidrantesCompleted();
                break;
            case "getMovRows":
                listener.onGetMovRowsCompleted();
                break;
        }
    }


    public ArrayList<Hidrante> getHidrantes() {
        ArrayList<Hidrante> Hidrantes = new ArrayList<>();
        try {
            JSONObject resp = new JSONObject(respuesta);
            JSONArray data = resp.getJSONArray("hidrantes");
            for(int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                byte[] foto = Base64.decode(obj.getString("foto"), Base64.DEFAULT);
                Hidrante h = new Hidrante(
                        obj.getInt("_id"),
                        obj.getString("nombre"),
                        obj.getString("posicion"),
                        obj.getString("estado").charAt(0),
                        obj.getInt("psi"),
                        obj.getInt("t4"),
                        obj.getInt("t25"),
                        obj.getString("acople"),
                        foto,
                        obj.getString("obs"));
                Hidrantes.add(h);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Hidrantes;
    }

    public int getMovRows(){
        int movimientos = 0;

        try {
            JSONObject resp = new JSONObject(respuesta);
            JSONArray data = resp.getJSONArray("Movimientos");
            JSONObject obj = data.getJSONObject(0);
            movimientos = obj.getInt("Filas");
            //Log.d("Movs de JSON",""+movimientos);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return movimientos;
    }

}
