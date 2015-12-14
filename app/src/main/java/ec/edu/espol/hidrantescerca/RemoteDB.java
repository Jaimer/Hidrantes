package ec.edu.espol.hidrantescerca;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by jaime on 6/12/2015.
 */
public class RemoteDB {
    private String getURL = "http://sip-publicidad.com/hidrantes/getHidrante.php";
    private String setURL = "http://sip-publicidad.com/hidrantes/setHidrante.php";
    private RequestQueue queue = null;
    private Context context = null;

    public RemoteDB(Context context)
    {
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
    }


    public void getHidrantes(){
        final ArrayList<Hidrante> Hidrantes = new ArrayList<>();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, this.getURL,
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
                            Utils.alerta("Error", "Error al deserializar", context);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error en respuesta del servidor");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
