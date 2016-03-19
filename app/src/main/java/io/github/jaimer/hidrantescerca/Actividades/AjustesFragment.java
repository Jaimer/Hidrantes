package io.github.jaimer.hidrantescerca.Actividades;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import io.github.jaimer.hidrantescerca.R;

/**
 * Created by jaime on 19/3/2016.
 */
public class AjustesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.ajustes);
    }
}
