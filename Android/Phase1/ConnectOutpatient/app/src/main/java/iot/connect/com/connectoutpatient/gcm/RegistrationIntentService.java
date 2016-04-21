package iot.connect.com.connectoutpatient.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import iot.connect.com.connectoutpatient.R;

/**
 * Created by Deep on 05-Apr-16.
 */
public class RegistrationIntentService extends IntentService {
    // abbreviated tag name
    private static final String TAG = "RegIntentService";
    public RegistrationIntentService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d(TAG, "GCM Registration Token: " + token);

            // pass along this data
            sendRegistrationToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String token) {
        final String tk=token;
        // Add custom implementation, as needed.
        /*new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Code here will run in UI thread
               // Toast.makeText(RegistrationIntentService.this, tk, Toast.LENGTH_SHORT).show();
            }
        });*/

    }
}
