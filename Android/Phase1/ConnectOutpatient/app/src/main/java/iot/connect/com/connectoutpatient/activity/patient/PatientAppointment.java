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
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.adapter.DoctorMyAppointment;
import iot.connect.com.connectoutpatient.activity.patient.adapter.PatientMyAppointment;
import iot.connect.com.connectoutpatient.modals.AppointmentParser;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 06-May-16.
 */
public class PatientAppointment extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    ListView appListview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment);

        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        recyclerView=(RecyclerView)findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("Appointment");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Medications");
        rows.add("Appointment");
        rows.add("Log Out");

        final String email=sharedpreferences.getString("email","");
        final String userName=sharedpreferences.getString("username","");
        String pic=sharedpreferences.getString("profilepic","http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterPatient drawerAdapter = new DrawerAdapterPatient(getApplicationContext(),rows,email,pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appListview=(ListView)findViewById(R.id.appointmentListView);
        /// Get appointment for patient
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


            String URL = AppBaseURL.BaseURL + "appointment/"+userName;
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, (JSONObject) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // the response is already constructed as a JSONObject!
                    Log.d("Response-", response.toString());
                    Gson gs=new Gson();
                    AppointmentParser appointmentParser=gs.fromJson(response.toString(),AppointmentParser.class);
                    appListview.setAdapter(new PatientMyAppointment(getApplicationContext(),appointmentParser.getData()));
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    error.printStackTrace();
                }
            });
            jsonRequest.setShouldCache(false);
            Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);
        } else {
            // If no network connectivity notify user
            Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    // Handle back button event fired.
    @Override
    public void onBackPressed()
    {
        // Go To Dashboard
        Intent i=new Intent(getApplicationContext(),PatientDashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
