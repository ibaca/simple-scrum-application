
package org.inftel.ssa.mobile.ui;

import org.inftel.ssa.mobile.R;
import org.inftel.ssa.mobile.utils.Util;
import org.inftel.ssa.services.SsaRequestFactory;
import org.inftel.ssa.services.UserRequest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class SsaActivity extends Activity {

    private TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ssa_activity);
        // save useful elements
        text = (TextView) findViewById(R.id.ssa_activity_text);

        SsaRequestFactory rf = Util.getRequestFactory(this, SsaRequestFactory.class);
        UserRequest usersContext = rf.userRequest();
        usersContext.countUsers().fire(new Receiver<Long>() {
            @Override
            public void onSuccess(Long response) {
                text.append("obtenido user count: " + response);
            }

            @Override
            public void onFailure(ServerFailure error) {
                text.append("fallo obtieniendo usuarios: " + error.getMessage());
            }
        });

    }

}
