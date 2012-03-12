
package org.inftel.ssa.mobile;

import android.app.Application;
import android.util.Log;

public class SsaApplication extends Application {

    private static final String TAG = "TmsApplication";

    @Override
    public final void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate application.");

    }

}
