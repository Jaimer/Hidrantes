package io.github.jaimer.hidrantescerca.Actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.github.jaimer.hidrantescerca.R;

/**
 * Created by jaime on 19/3/2016.
 */
public class AjustesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajustesactivity);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AjustesFragment())
                .commit();
    }
}
