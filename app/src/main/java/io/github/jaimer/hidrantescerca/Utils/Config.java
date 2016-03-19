package io.github.jaimer.hidrantescerca.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by jaime on 6/12/2015.
 */
public class Config {

    public String server;
    public String getURL;
    public String getCantMovURL;
    public String getLastMovURL;
    public String setHidranteURL;
    public String DBsync;

    public Config(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.server = preferences.getString("serverurl", "");

        this.getURL = server+"getHidrante.php";
        this.getCantMovURL = server+"getMovimientos.php?TRA=ROW";
        this.getLastMovURL = server+"getMovimientos.php?TRA=LAS&NUM=";
        this.setHidranteURL = server+"setHidrante.php";
        this.DBsync = server+"DBsync.php?NUM=";

        Log.d("Servidor URL ", this.server);
        Log.d("GetURL", this.getURL);
    }
}
