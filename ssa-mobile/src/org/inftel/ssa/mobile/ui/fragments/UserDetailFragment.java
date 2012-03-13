
package org.inftel.ssa.mobile.ui.fragments;

import org.inftel.ssa.mobile.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserDetailFragment extends Activity {

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
        setContentView(R.layout.ssa_user_details);
        fullname = (TextView) (findViewById(R.id.textView2));
        nickname = (TextView) (findViewById(R.id.TextView3));
        email = (TextView) (findViewById(R.id.TextView5));
        company = (TextView) (findViewById(R.id.TextView7));
        role = (TextView) (findViewById(R.id.TextView10));
        number = (TextView) (findViewById(R.id.TextView07));
        final Bundle extras = getIntent().getExtras();

        if (extras == null) {
            return;
        }
        fullname.setText(extras.getString("FullName"));
        nickname.setText(extras.getString("NickName"));
        email.setText(extras.getString("Email"));
        company.setText(extras.getString("Company"));
        role.setText(extras.getString("Role"));
        number.setText(extras.getString("Number"));

        btnEmail = (Button) (findViewById(R.id.button1));
        btnEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Su email aquí");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {
                        extras.getString("Email")
                });
                startActivity(emailIntent);
            }

        });

        btnCall = (Button) (findViewById(R.id.button2));
        btnCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:" + extras.getString("Number")));
                startActivity(callIntent);

            }

        });

    }
}
