package io.github.jaimer.hidrantescerca.BD;

import android.os.AsyncTask;

/**
 * Created by jaime on 26/12/2015.
 */
public class GetCambiosRDB extends AsyncTask<Integer, Integer, String> {
    private GetCambiosRBDTaskCompleted listener;

    public GetCambiosRDB(GetCambiosRBDTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String respuesta = null;
        int cambios = params[0];



        return respuesta;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onGetCambiosRDBTaskCompleted();
    }


}
