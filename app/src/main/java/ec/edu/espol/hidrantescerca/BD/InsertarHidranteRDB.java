package ec.edu.espol.hidrantescerca.BD;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ec.edu.espol.hidrantescerca.Entidades.Hidrante;
import ec.edu.espol.hidrantescerca.Entidades.Movimiento;
import ec.edu.espol.hidrantescerca.Utils.Config;

/**
 * Created by jaime on 26/12/2015.
 */
public class InsertarHidranteRDB extends AsyncTask<Hidrante, Integer, String> {
    private InsHidranteRDBTaskCompleted listener;
    private  Movimiento movimiento;
    private Context context;
    private ProgressDialog dialog;

    public InsertarHidranteRDB(InsHidranteRDBTaskCompleted listener) {
        this.listener = listener;
        this.context = (Context) listener;
        this.dialog = new ProgressDialog((Context) listener);
    }

    @Override
    protected String doInBackground(Hidrante... params) {
        Debug.waitForDebugger();
        String respuesta = null;
        String estado;
        String hidrante = params[0].toJSON();

        try {
            URL url = new URL(Config.setHidranteURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.setRequestProperty("Accept","*/*");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.connect();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(hidrante);
            bw.flush();
            bw.close();

            int status = conn.getResponseCode();
            switch (status){
                case 200:
                case 201:

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null){
                        result.append(linea);
                    }

                    JSONObject resp = new JSONObject(result.toString());
                    estado = resp.getString("estado");
                    if(estado.equals("1")){
                        JSONObject data = resp.getJSONObject("movimiento");
                        movimiento = new Movimiento(data.getInt("idmovimiento"), data.getInt("id_hidrante"), data.getString("fecha_mod"), data.getString("usuario_mod"));
                        respuesta = estado;
                    }else{
                        movimiento = null;
                        respuesta = estado;
                    }
                    break;
                default:
                    Log.d("Respuesta",""+status);
                    break;
            }


        } catch (MalformedURLException e) {
            Log.d("Error", "URL Malformada");
        } catch (ProtocolException e) {
            Log.d("Error", "Protocolo erróneo");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("Error", "Error de Lectura");
            respuesta = "0";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return respuesta;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Guardando");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if(s == null){
            Log.d("Error", "respuesta nula");
        }else{
            listener.onInsHidranteRDBTaskCompleted(movimiento, s);
        }

    }
}
