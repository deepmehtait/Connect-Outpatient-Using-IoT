package iot.connect.com.connectoutpatient.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.gcm.RegistrationIntentService;
import iot.connect.com.connectoutpatient.utils.AppStatus;
import iot.connect.com.connectoutpatient.utils.ReadPhoneStatePermissionHandle;
import iot.connect.com.connectoutpatient.utils.Validator;

/**
 * Created by Deep on 15-Jan-16.
 */
public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String TAG = "MainActivity";
    Button login,noAccount;
    EditText emailID,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        String UUID=new String();
        if(Build.VERSION.SDK_INT >= 23){
            // Marshmallow +

            getUUIDCheck();
           // return UUID;
        }else{
            // Pre-Marshmallow
            getUUID();
            //return UUID;
        }
        // Buttons
        login=(Button)findViewById(R.id.btn_login);
        noAccount=(Button)findViewById(R.id.btn_signup);
        // Edit text
        emailID=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        // Login Handle
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid=emailID.getText().toString();
                if(!Validator.isValidEmail(emailid)){
                    emailID.setError("Invalid Email");
                }
                String pass=password.getText().toString();
                if(!Validator.isValidPassword(pass)){
                    password.setError("Invalid Password");
                }
                Intent i=new Intent(getApplicationContext(),GraphActivity.class);
                startActivity(i);


            }
        });
        //  Signup Handle
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });



       // ReadPhoneStatePermissionHandle rpsph=new ReadPhoneStatePermissionHandle(this,MainActivity.class);
        //String UID=rpsph.getDeviceUUID();
        //Toast t1=Toast.makeText(this,UUID,Toast.LENGTH_SHORT);
        if(AppStatus.getInstance(this).isOnline()) {

            Toast t = Toast.makeText(this, "You are online!!!!", Toast.LENGTH_SHORT);
            t.show();
           /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Internet Connectivity!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();*/

        } else {

            Toast t = Toast.makeText(this, "You are not online!!!!", Toast.LENGTH_SHORT);
            t.show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Internet Connectivity!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            Log.v("Home", "############################You are not online!!!!");
        }

    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public String getUUID(){
        String UUID=new String();
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        UUID=tManager.getDeviceId();
        Log.d("UUID= ", UUID);
        return UUID;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getUUID();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "READ_PHONE_STATE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getUUIDCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasPhoneReadStarePermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (hasPhoneReadStarePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }

        }
        getUUID();
    }

}
