package io.github.jaimer.hidrantescerca.Actividades;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

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
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
