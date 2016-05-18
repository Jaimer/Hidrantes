package io.github.jaimer.hidrantescerca.Actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import io.github.jaimer.hidrantescerca.R;

/**
 * Created by jaime on 19/3/2016.
 */
public class AjustesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajustesactivity);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AjustesFragment())
                .commit();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        EditTextPreference usuario = (EditTextPreference)findPreference("appusername");
        //usuario.setSummary(sp.getString("appusername", "none"));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
