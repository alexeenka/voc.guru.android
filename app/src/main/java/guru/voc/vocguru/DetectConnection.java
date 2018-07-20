package guru.voc.vocguru;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Utility class to check, if connection to internet exist!
 *
 * @author alexey.alexeenka 20 July 2018
 */
public class DetectConnection {

    private ConnectivityManager conManager;

    public DetectConnection(Context context) {
        this.conManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean connected() {
        return (conManager.getActiveNetworkInfo() != null && conManager.getActiveNetworkInfo().isAvailable() && conManager.getActiveNetworkInfo().isConnected());
    }

}