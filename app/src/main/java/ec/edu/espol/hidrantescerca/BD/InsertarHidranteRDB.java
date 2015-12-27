package ec.edu.espol.hidrantescerca.BD;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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

    public InsertarHidranteRDB(InsHidranteRDBTaskCompleted listener) {
        this.listener = listener;
        this.context = (Context) listener;
    }

    @Override
    protected String doInBackground(Hidrante... params) {
        Hidrante hidrante = params[0];
        String respuesta = null;
        String estado;

        try {
            URL url = new URL(Config.setHidranteURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setChunkedStreamingMode(0);
            conn.connect();


            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            Log.d("Hidrante", hidrante.toJSON());
            writer.write(hidrante.toJSON());
            out.flush();
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null){
                result.append(linea);
            }

            writer.close();
            out.close();

            Log.d("Respuesta", result.toString());
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

        } catch (MalformedURLException e) {
            Log.d("Error", "URL Malformada");
        } catch (ProtocolException e) {
            Log.d("Error", "Protocolo erróneo");
        } catch (IOException e) {
            Log.d("Error", "Error de Lectura");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return respuesta;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s == null){
            Log.d("Error", "respuesta nula");
        }else{
            listener.onInsHidranteRDBTaskCompleted(movimiento, s);
        }

    }
}
