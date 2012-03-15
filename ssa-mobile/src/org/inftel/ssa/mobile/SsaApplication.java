
package org.inftel.ssa.mobile;

import org.inftel.ssa.mobile.contentproviders.SprintContentProvider;
import org.inftel.ssa.mobile.contentproviders.SprintTable;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.util.Log;

public class SsaApplication extends Application {

    private static final String TAG = "SsaApplication";

    @Override
    public final void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate application.");

        // Algunos datos para hacer pruebas
        ContentResolver cr = getContentResolver();

        if (cr.query(SprintContentProvider.CONTENT_URI, new String[] {
                SprintTable.KEY_SUMMARY
        }, null, null, null).getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put(SprintTable.KEY_SUMMARY, "Primer sprint");
            cr.insert(SprintContentProvider.CONTENT_URI, values);
            values.put(SprintTable.KEY_SUMMARY, "Segundo sprint");
            cr.insert(SprintContentProvider.CONTENT_URI, values);
            values.put(SprintTable.KEY_SUMMARY, "Tercer sprint");
            cr.insert(SprintContentProvider.CONTENT_URI, values);
        }

    }

}
