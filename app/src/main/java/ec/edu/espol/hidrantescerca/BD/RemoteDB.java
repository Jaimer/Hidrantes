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
import ec.edu.espol.hidrantescerca.Utils.RemoteDBTaskCompleted;

/**
 * Created by jaime on 6/12/2015.
 */
public class RemoteDB extends AsyncTask<Void,Integer,String>{
    private String respuesta = null;
    private String accion = null;
    private RemoteDBTaskCompleted listener;

    public RemoteDB(RemoteDBTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... params) {
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


        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.respuesta = s;
        listener.onGetHidrantesCompleted();
    }


    public ArrayList<Hidrante> getHidrantes() {
        ArrayList<Hidrante> Hidrantes = new ArrayList<>();
        try {
            JSONObject resp = new JSONObject(respuesta);
            JSONArray data = resp.getJSONArray("hidrantes");

            for(int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                byte[] foto = Base64.decode(obj.getString("foto"), Base64.DEFAULT);
                Hidrante h = new Hidrante(obj.getInt("_id"), obj.getString("nombre"), obj.getString("posicion"), obj.getString("estado").charAt(0), obj.getInt("psi"), obj.getInt("t4"), obj.getInt("t25"), obj.getString("acople"), foto, obj.getString("obs"));
                Hidrantes.add(h);
                Log.d("Hidrante: ", h.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Hidrantes;

        /*final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.getURL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("hidrantes");
                            for(int i = 0; i < data.length(); i++){
                                JSONObject obj = data.getJSONObject(i);
                                byte[] foto = Base64.decode(obj.getString("foto"), Base64.DEFAULT);
                                Hidrante h = new Hidrante(obj.getInt("_id"),obj.getString("nombre"),obj.getString("posicion"),obj.getString("estado").charAt(0),obj.getInt("psi"),obj.getInt("t4"),obj.getInt("t25"),obj.getString("acople"),foto,obj.getString("obs"),obj.getString("fecha_crea"),obj.getString("fecha_mod"),obj.getString("fecha_insp"),obj.getString("fecha_man"),obj.getString("usuario_crea"),obj.getString("usuario_mod"));
                                LocalDB db = new LocalDB(context);
                                Log.d("Hidrante: ",h.toString());
                                db.insertarHidrante(h);
                            }
                        } catch (JSONException e) {
                            Log.d("Error", "Error al deserializar");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","Error en respuesta del servidor");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);*/
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public void setListener(RemoteDBTaskCompleted listener) {
        this.listener = listener;
    }

    /*public int getMovRows(){
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.getURL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("Movimientos");
                            for(int i = 0; i < data.length(); i++){
                                JSONObject obj = data.getJSONObject(i);
                                byte[] foto = Base64.decode(obj.getString("foto"), Base64.DEFAULT);
                                Hidrante h = new Hidrante(obj.getInt("_id"),obj.getString("nombre"),obj.getString("posicion"),obj.getString("estado").charAt(0),obj.getInt("psi"),obj.getInt("t4"),obj.getInt("t25"),obj.getString("acople"),foto,obj.getString("obs"),obj.getString("fecha_crea"),obj.getString("fecha_mod"),obj.getString("fecha_insp"),obj.getString("fecha_man"),obj.getString("usuario_crea"),obj.getString("usuario_mod"));
                                LocalDB db = new LocalDB(context);
                                Log.d("Hidrante: ",h.toString());
                                db.insertarHidrante(h);
                            }
                        } catch (JSONException e) {
                            Log.d("Error", "Error al deserializar");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","Error en respuesta del servidor");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }*/
}
