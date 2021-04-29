package co.dekhok.railway;

import android.content.Context;
import android.util.Log;
import android.view.View;

/**
 * Created by dekho on 02/09/2019.
 */

public class OnClickListenerButtonStart implements View.OnClickListener {

    final String TAG = "OnClickListenerButtonStart.java";

    Locatin_Get mainActivity;
    Context context;

    @Override
    public void onClick(View view) {

        Log.e(TAG, "Started getting user location.");

        // to get the context and main activity
        this.context = view.getContext();
        this.mainActivity = ((Locatin_Get) context);

        // disable the START button, enable the STOP button
        mainActivity.btnMyLocation.setEnabled(false);

        // start listening to location updates
        mainActivity.locationHelper.getLocation(mainActivity, mainActivity.locationResult);

    }

}
