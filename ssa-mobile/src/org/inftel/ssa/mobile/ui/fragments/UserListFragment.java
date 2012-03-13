
package org.inftel.ssa.mobile.ui.fragments;

import java.io.Serializable;
import java.util.ArrayList;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.UserContentProvider;
import org.inftel.ssa.mobile.contentproviders.UserTable;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserListFragment extends Activity {

    private UserData users[] = new UserData[50];

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssa_user_list);
        ContentResolver cr = getContentResolver();
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
        values.put(UserTable.KEY_COMPANY, "Master Inftel");
        values.put(UserTable.KEY_PASS, "inftel");
        values.put(UserTable.KEY_ROLE, "SM");

        cr.insert(UserContentProvider.CONTENT_URI, values);

        values.put(UserTable.KEY_FULLNAME, "Juan Ant. Cobo");
        values.put(UserTable.KEY_NICKNAME, "JuaNaN");
        values.put(UserTable.KEY_EMAIL, "juanan20@gmail.com");
        values.put(UserTable.KEY_NUMBER, "957700652");
        values.put(UserTable.KEY_COMPANY, "Master Inftel");
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

        // Columnas de la tabla a recuperar
        String[] projection = new String[] {
                UserTable.KEY_ID,
                UserTable.KEY_FULLNAME, UserTable.KEY_NICKNAME,
                UserTable.KEY_EMAIL, UserTable.KEY_NUMBER,
                UserTable.KEY_COMPANY,
                UserTable.KEY_ROLE
        };

        Uri clientesUri = UserContentProvider.CONTENT_URI;

        Cursor cur = cr.query(clientesUri, projection, // Columnas a devolver
                null, // Condicion de la query
                null, // Argumentos variables de la query
                null); // Orden de los resultados

        ArrayList<String> listausuarios = new ArrayList<String>();

        if (cur.moveToFirst()) {
            int colName = cur.getColumnIndex(UserTable.KEY_FULLNAME);
            int colNickname = cur.getColumnIndex(UserTable.KEY_NICKNAME);
            int colEmail = cur.getColumnIndex(UserTable.KEY_EMAIL);
            int colNumber = cur.getColumnIndex(UserTable.KEY_NUMBER);
            int colCompany = cur.getColumnIndex(UserTable.KEY_COMPANY);
            int colRole = cur.getColumnIndex(UserTable.KEY_ROLE);
            int i = 0;

            do {
                users[i] = new UserData();

                users[i].setFullName(cur.getString(colName));
                users[i].setNickName(cur.getString(colNickname));
                users[i].setEmail(cur.getString(colEmail));
                users[i].setNumber(cur.getString(colNumber));
                users[i].setCompany(cur.getString(colCompany));
                users[i].setRole(cur.getString(colRole));

                listausuarios.add(users[i].getFullName());
                i++;
            } while (cur.moveToNext());
        }

        ListView userslist = (ListView) findViewById(R.id.UsersList);
        listausuarios.toArray();

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listausuarios);
        userslist.setAdapter(adaptador);

        userslist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> padre, View v, int posicion,
                    long id) {
                Intent details = new Intent(getApplicationContext(), UserDetailFragment.class);
                int pos = (int) padre.getItemIdAtPosition(posicion);
                details.putExtra("FullName", users[pos].getFullName());
                details.putExtra("NickName", users[pos].getNickName());
                details.putExtra("Email", users[pos].getEmail());
                details.putExtra("Number", users[pos].getNumber());
                details.putExtra("Company", users[pos].getCompany());
                details.putExtra("Role", users[pos].getRole());

                startActivity(details);
                Toast.makeText(v.getContext(),
                        padre.getItemAtPosition(posicion).toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class UserData implements Serializable {
        private String fullName;
        private String nickName;
        private String email;
        private String number;
        private String company;
        private String role;
        private int id;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

    }
}
