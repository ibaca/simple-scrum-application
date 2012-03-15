
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.TaskContentProvider;
import org.inftel.ssa.mobile.contentproviders.TaskTable;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TaskListFragment extends ListActivity {
    // For logging and debugging
    private static final String TAG = "TasksListActivity";

    /**
     * The columns needed by the cursor adapter
     */
    private static final String[] PROJECTION = new String[] {
            TaskTable.COLUMN_ID, // 0
            TaskTable.COLUMN_SUMMARY, // 1
            TaskTable.COLUMN_ESTIMATED, // 2
    };

    /** The index of the title column */
    private static final int COLUMN_INDEX_SUMMARY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(TaskContentProvider.CONTENT_URI);
        }

        // Inform the list we provide context menus for items
        getListView().setOnCreateContextMenuListener(this);

        // Perform a managed query. The Activity will handle closing and
        // requerying the cursor when needed.
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null,
                TaskContentProvider.DEFAULT_SORT_ORDER);

        // Used to map tasks entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.ssa_task_list, cursor,
                new String[] {
                        TaskTable.COLUMN_SUMMARY
                }, new int[] {
                        android.R.id.text1
                });
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from XML resource
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ssa_task_list_menu, menu);

        // Generate any additional actions that can be performed on the
        // overall list.
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, TaskListFragment.class), null, intent, 0, null);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                // Launch activity to insert a new item
                startActivity(new Intent(TaskListFragment.this, TaskEditFragment.class));
                // startActivity(new Intent(Intent.ACTION_INSERT,
                // getIntent().getData()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri taskUri = ContentUris.withAppendedId(getIntent().getData(), id);

        // Launch activity to view/edit the currently selected item
        // startActivity(new Intent(Intent.ACTION_VIEW, taskUri));
        startActivity(new Intent(Intent.ACTION_VIEW, taskUri, TaskListFragment.this,
                TaskDetailFragment.class));

    }
}
