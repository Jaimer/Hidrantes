package ec.edu.espol.hidrantescerca.BD;

/**
 * Created by jaime on 24/12/2015.
 */
public interface RemoteDBTaskCompleted {
    void onTaskCompleted();
    void onGetHidrantesCompleted();
    void onGetMovRowsCompleted();
}
