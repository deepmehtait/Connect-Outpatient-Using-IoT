package iot.connect.com.connectoutpatient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import iot.connect.com.connectoutpatient.activity.doctor.DoctorDashboardActivity;
import iot.connect.com.connectoutpatient.activity.doctor.DoctorMyPatientListActivity;
import iot.connect.com.connectoutpatient.activity.patient.PatientDashboardActivity;

/**
 * Created by Deep on 1-Feb-16.
 */
public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        String isLoggedIn=sharedpreferences.getString("LoggedIn","Null");
        if(isLoggedIn.matches("true")){
            String role=sharedpreferences.getString("role","null");
            if(role.matches("patient")){
                Intent i=new Intent(getApplicationContext(), PatientDashboardActivity.class);
                startActivity(i);
                finish();
            }else if(role.matches("doctor")){
                Intent i=new Intent(getApplicationContext(), DoctorDashboardActivity.class);
                startActivity(i);
                finish();
            }
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
        Intent intent = new Intent(this, DoctorMyPatientListActivity.class);
        startActivity(intent);

    }
}
