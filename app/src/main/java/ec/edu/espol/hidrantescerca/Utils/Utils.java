package ec.edu.espol.hidrantescerca.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jaime on 6/12/2015.
 */
public final class Utils {

    public static void alerta(String titulo, String mensaje, Context context){
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // algo
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
