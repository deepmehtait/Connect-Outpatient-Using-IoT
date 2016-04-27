package iot.connect.com.connectoutpatient.activity.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.MainActivity;
import iot.connect.com.connectoutpatient.gcm.RegisterToken;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 19-Apr-16.
 */
public class PatientSettingsActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    Button signout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_settings);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);
        signout = (Button) findViewById(R.id.patientSignout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("Settings");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Medications");
        rows.add("Settings");
        String email = sharedpreferences.getString("email", "");
        String pic = sharedpreferences.getString("profilepic", "http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterPatient drawerAdapter = new DrawerAdapterPatient(getApplicationContext(), rows, email, pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    RegisterToken rt = new RegisterToken();
                    rt.unRegister(getApplicationContext());
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("LoggedIn", "null");
                    editor.putString("role", "");
                    editor.putString("username", "");
                    editor.putString("profilepic", "");
                    editor.putString("email", "");
                    editor.commit();
                    Intent i = new Intent(PatientSettingsActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    // If no network connectivity notify user
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    // Handle back button event fired.
    @Override
    public void onBackPressed() {
        // Go To Dashboard
        Intent i = new Intent(getApplicationContext(), PatientDashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
