
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.contentproviders.UserTable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserDetailFragment extends Activity {

    private Uri mUri;
    private Cursor mCursor;
    private TextView fullname;
    private TextView nickname;
    private TextView email;
    private TextView company;
    private TextView role;
    private TextView number;

    private Button btnEmail;
    private Button btnCall;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();

        mUri = intent.getData();

        setContentView(R.layout.ssa_user_details);
        fullname = (TextView) (findViewById(R.id.textView2));
        nickname = (TextView) (findViewById(R.id.TextView3));
        email = (TextView) (findViewById(R.id.TextView5));
        company = (TextView) (findViewById(R.id.TextView7));
        role = (TextView) (findViewById(R.id.TextView10));
        number = (TextView) (findViewById(R.id.TextView07));

        String[] projection = new String[] {
                UserTable.KEY_ID,
                UserTable.KEY_FULLNAME, UserTable.KEY_NICKNAME,
                UserTable.KEY_EMAIL, UserTable.KEY_NUMBER,
                UserTable.KEY_COMPANY,
                UserTable.KEY_ROLE
        };

        mCursor = managedQuery(mUri, projection, null, null, null);

        if (mCursor != null) {
            // Requery in case something changed while paused (such as the
            // title)
            mCursor.requery();
            // Make sure we are at the one and only row in the cursor.
            mCursor.moveToFirst();

            int colName = mCursor
                    .getColumnIndex(UserTable.KEY_FULLNAME);
            int colNickname = mCursor
                    .getColumnIndex(UserTable.KEY_NICKNAME);
            int colEmail = mCursor
                    .getColumnIndex(UserTable.KEY_EMAIL);
            int colCompany = mCursor
                    .getColumnIndex(UserTable.KEY_COMPANY);
            int colRole = mCursor
                    .getColumnIndex(UserTable.KEY_ROLE);
            int colNumber = mCursor
                    .getColumnIndex(UserTable.KEY_NUMBER);

            // Set the title of the Activity to include the note title
            /*
             * String title = mCursor.getString(colId); Resources res =
             * getResources(); String text =
             * String.format(res.getString(R.string.title_edit), title);
             * setTitle(text);
             */

            // Modify the task data
            // This is a little tricky: we may be resumed after previously being
            // paused/stopped. We want to put the new text in the text view,
            // but leave the user where they were (retain the cursor position
            // etc). This version of setText does that for us.
            String name = mCursor.getString(colName);
            fullname.setText(name);
            nickname.setText(mCursor.getString(colNickname));
            email.setText(mCursor.getString(colEmail));

            company.setText(mCursor.getString(colCompany));
            number.setText(mCursor.getString(colNumber));
            role.setText(mCursor.getString(colRole));

        } else {
            /*
             * setTitle(getText(R.string.error_title));
             * mTxtSummary.setText(getText(R.string.error_message));
             */
        }

        btnEmail = (Button) (findViewById(R.id.button1));
        btnEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Su email aquí");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {
                        (String) email.getText()
                });
                startActivity(emailIntent);
            }

        });

        btnCall = (Button) (findViewById(R.id.button2));
        btnCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:" + number.getText()));
                startActivity(callIntent);

            }

        });

    }

}
