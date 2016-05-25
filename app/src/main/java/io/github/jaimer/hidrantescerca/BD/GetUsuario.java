package io.github.jaimer.hidrantescerca.BD;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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

import io.github.jaimer.hidrantescerca.Entidades.Usuario;
import io.github.jaimer.hidrantescerca.Utils.Config;

/**
 * Created by jaime on 22/5/2016.
 */
public class GetUsuario extends AsyncTask<Context, Integer, Boolean> {
    private GetUsuarioTaskCompleted listener;
    private ProgressDialog progressDialog;
    private Config config;
    private String usuario;
    private LocalDB localDB;

    public GetUsuario(GetUsuarioTaskCompleted listener) {
        this.listener = listener;
        this.progressDialog = new ProgressDialog((Context) listener);
    }

    @Override
    protected Boolean doInBackground(Context... params) {
        Context context = params[0];
        this.config  = new Config(context);
        this.localDB = new LocalDB(context);
        //Debug.waitForDebugger(); Debuggin'
        String response = null;
        String resultado = null;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.usuario = preferences.getString("appusername", "");
        Log.d("usuario", usuario);
        try {
            URL url = new URL(config.GetUsuariosByEmailURL + usuario);
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
                    JSONObject resp = new JSONObject(response);
                    if(resp.get("estado").toString().equals("1")){
                        JSONArray usrdata = resp.getJSONArray("Usuario");
                        JSONObject usr = usrdata.getJSONObject(0);
                        Usuario usuario = new Usuario(
                                usr.getString("id_cedula"),
                                usr.getString("nombre"),
                                usr.getString("apellido"),
                                usr.getInt("tipo"),
                                usr.getString("institucion"),
                                usr.getString("cargo"),
                                usr.getString("password"),
                                usr.getString("estado").charAt(0),
                                usr.getString("email")
                        );
                        if(usuario.getEstado() == 'A'){
                            localDB.insertarUsuario(usuario);
                            return true;
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.d("Error", "URL Malformada");
        } catch (ProtocolException e) {
            Log.d("Error", "Protocolo erróneo");
        } catch (IOException e) {
            Log.d("Error", "Error de Lectura");
        }

        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        listener.onGetUsuarioTaskCompleted(b);
    }
}
