package ec.edu.espol.hidrantescerca.BD;

import ec.edu.espol.hidrantescerca.Entidades.Movimiento;

/**
 * Created by jaime on 26/12/2015.
 */
public interface InsHidranteRDBTaskCompleted {
    void onInsHidranteRDBTaskCompleted(Movimiento movimiento, String estado);
}
