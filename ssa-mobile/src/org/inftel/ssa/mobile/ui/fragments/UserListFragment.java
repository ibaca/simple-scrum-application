
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.UserContentProvider;
import org.inftel.ssa.mobile.contentproviders.UserTable;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class UserListFragment extends ListActivity {

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ContentResolver cr = getContentResolver();
        insertarEliminar(cr);

        // Columnas de la tabla a recuperar
        String[] projection = new String[] {
                UserTable.KEY_ID,
                UserTable.KEY_FULLNAME,
                UserTable.KEY_COMPANY
        };

        // Desde la pantalla de proyectos se enviaría la URI de dicho proyecto
        // y se le sacaría el ID directamente desde la URI.
        String company = "Inftel";

        String search =
                UserTable.KEY_COMPANY + " = " + "\"" + company + "\"";
        System.out.println(search);

        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(UserContentProvider.CONTENT_URI);
        }

        // Inform the list we provide context menus for items
        getListView().setOnCreateContextMenuListener(this);

        // Perform a managed query. The Activity will handle closing and
        // requerying the cursor when needed.

        Cursor cursor = managedQuery(getIntent().getData(), projection,
                search, null, null);

        // Used to map tasks entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.ssa_user_list,
                cursor,
                new String[] {
                        UserTable.KEY_FULLNAME
                }, new int[] {
                        android.R.id.text1
                });
        setListAdapter(adapter);

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri userUri = ContentUris.withAppendedId(getIntent().getData(), id);

        // Launch activity to view/edit the currently selected item
        // startActivity(new Intent(Intent.ACTION_EDIT, noteUri));
        startActivity(new Intent(Intent.ACTION_EDIT, userUri, UserListFragment.this,
                UserDetailFragment.class));

    }

    static void insertarEliminar(ContentResolver cr) {

        ContentValues values = new ContentValues();

        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = 'ibaca'", null);
        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = 'JuaNaN'", null);
        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = '3'", null);
        cr.delete(UserContentProvider.CONTENT_URI,
                UserTable.KEY_NICKNAME + " = '4'", null);

        values.put(UserTable.KEY_FULLNAME, "Ignacio Baca");
        values.put(UserTable.KEY_NICKNAME, "ibaca");
        values.put(UserTable.KEY_EMAIL, "ignacio@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Juan Ant. Cobo");
        values.put(UserTable.KEY_NICKNAME, "JuaNaN");
        values.put(UserTable.KEY_EMAIL, "juanan20@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Jesus Ruiz");
        values.put(UserTable.KEY_NICKNAME, "3");
        values.put(UserTable.KEY_EMAIL, "jrovillano@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Master Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Jesus Barriga");
        values.put(UserTable.KEY_NICKNAME, "4");
        values.put(UserTable.KEY_EMAIL, "jesusbarriga@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Master Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

    }
}
