
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TaskNewFragment extends Activity {
    private static final String TAG = "TaskNewActivity";

    private EditText mTxtBeginDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssa_task_new);

        Button button = (Button) findViewById(R.id.cmdSave);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ContentResolver cr = getContentResolver();

                ContentValues values = new ContentValues();
                values.put(TaskTable.COLUMN_USER, "-100");
                values.put(TaskTable.COLUMN_PROJECT, "-200");
                values.put(TaskTable.COLUMN_SPRINT, "-400");
                values.put(TaskTable.COLUMN_SUMMARY,
                        ((EditText) findViewById(R.id.txtSummary)).getText().toString());
                values.put(TaskTable.COLUMN_DESCRIPTION,
                        ((EditText) findViewById(R.id.txtDescription)).getText().toString());
                values.put(TaskTable.COLUMN_ESTIMATED,
                        ((EditText) findViewById(R.id.txtEstimated)).getText().toString());
                values.put(TaskTable.COLUMN_BURNED, "4");
                values.put(TaskTable.COLUMN_REMAINING, "0");
                values.put(TaskTable.COLUMN_PRIORITY,
                        ((EditText) findViewById(R.id.txtPriority)).getText().toString());
                values.put(TaskTable.COLUMN_BEGINDATE,
                        ((EditText) findViewById(R.id.txtBeginDate)).getText().toString());
                values.put(TaskTable.COLUMN_ENDDATE, "2011-12-01");
                values.put(TaskTable.COLUMN_STATUS, "2");
                values.put(TaskTable.COLUMN_CREATED, "2012-03-07");
                values.put(TaskTable.COLUMN_COMMENTS, "comentario");

                cr.insert(TaskContentProvider.CONTENT_URI, values);

            }
        });

    }
}
