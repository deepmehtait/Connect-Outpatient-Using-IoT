package iot.connect.com.connectoutpatient.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.DoctorDashboardActivity;
import iot.connect.com.connectoutpatient.activity.patient.PatientDashboardActivity;
import iot.connect.com.connectoutpatient.gcm.RegistrationIntentService;
import iot.connect.com.connectoutpatient.modals.SignIn;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;
import iot.connect.com.connectoutpatient.utils.ReadPhoneStatePermissionHandle;
import iot.connect.com.connectoutpatient.utils.Validator;
import iot.connect.com.connectoutpatient.gcm.RegisterToken;
/**
 * Created by Deep on 15-Jan-16.
 */
public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String TAG = "MainActivity";
    Button login,noAccount;
    EditText emailID,password;
    RadioGroup userType;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
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
        // Radio Group
        userType=(RadioGroup)findViewById(R.id.login_RG);
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
                final String emailid=emailID.getText().toString();
                /*if(!Validator.isValidEmail(emailid)){
                    emailID.setError("Invalid Email");
                }*/
                final String pass=password.getText().toString();
                if(!Validator.isValidPassword(pass)){
                    password.setError("Invalid Password");
                }
                String RadioSelection = new String();
                if(userType.getCheckedRadioButtonId()!=-1){
                    int id=userType.getCheckedRadioButtonId();
                    View radioButton=userType.findViewById(id);
                    int radioId = userType.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) userType.getChildAt(radioId);
                    RadioSelection = (String) btn.getText();
                    /*if(RadioSelection.matches("Patient")){
                        Intent i=new Intent(getApplicationContext(), PatientDashboardActivity.class);
                        startActivity(i);
                    }else if(RadioSelection.matches("Doctor")){
                        Intent i=new Intent(getApplicationContext(), DoctorDashboardActivity.class);
                        startActivity(i);
                    }*/
                }
                JSONObject obj=new JSONObject();
                try{
                    obj.put("username",emailid);
                    obj.put("password",pass);

                }catch(JSONException e){
                    e.printStackTrace();
                }
                Log.d("Request=",obj.toString());
                String url= AppBaseURL.BaseURL+"api/auth/signin";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                        obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Response", jsonObject.toString());
                        Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
                        SignIn signIn=new SignIn();
                        Gson gs=new Gson();
                        String msg=new String();
                        try{
                            msg=jsonObject.getString("message");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        if(msg.matches("Success")){
                            try{
                                signIn=gs.fromJson(jsonObject.getJSONObject("data").toString(),SignIn.class);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            String role=signIn.getRoles().get(0).toString();
                            String username=signIn.getUsername();
                            String profilepic=AppBaseURL.BaseURL+signIn.getProfileImageURL();
                            String email=signIn.getEmail();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("role",role);
                            editor.putString("username",username);
                            editor.putString("profilepic",profilepic);
                            editor.putString("email",email);
                            editor.putString("LoggedIn","true");
                            editor.commit();
                            RegisterToken rt=new RegisterToken();
                            rt.register(getApplicationContext());
                            Log.d("Registration Complete","yea.!");
                            if(role.matches("patient")){
                                Intent i=new Intent(getApplicationContext(), PatientDashboardActivity.class);
                                startActivity(i);
                            }else if(role.matches("doctor")){
                                Intent i=new Intent(getApplicationContext(), DoctorDashboardActivity.class);
                                startActivity(i);
                            }
                           // Toast.makeText(getApplicationContext(),signIn.getRoles().get(0).toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                jsonObjectRequest.setShouldCache(false);
                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

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
        if(!(AppStatus.getInstance(this).isOnline())) {


            Toast t = Toast.makeText(this, "You are not online!!!!", Toast.LENGTH_SHORT);
            t.show();

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
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("UUID",UUID);
        editor.commit();
        Log.d("UUID= ", UUID);
        Log.d("UUID from shared pref",sharedpreferences.getString("UUID"," no key"));
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
