package io.github.jaimer.hidrantescerca.BD;

import io.github.jaimer.hidrantescerca.Entidades.Movimiento;

/**
 * Created by jaime on 26/12/2015.
 */
public interface InsHidranteRDBTaskCompleted {
    void onInsHidranteRDBTaskCompleted(Movimiento movimiento, String estado);
}
