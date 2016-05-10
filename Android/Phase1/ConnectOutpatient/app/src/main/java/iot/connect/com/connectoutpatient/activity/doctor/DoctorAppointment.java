package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
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
import iot.connect.com.connectoutpatient.modals.AppointmentParser;
import iot.connect.com.connectoutpatient.modals.ViewAppoinment;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 05-May-16.
 */
public class DoctorAppointment extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    ImageButton add;
    ListView appListview;
    DoctorMyAppointment adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<ViewAppoinment> appoinmentslist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        final String email=sharedpreferences.getString("email","");
        final String userName=sharedpreferences.getString("username","");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("Appointment");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Patients");
        rows.add("Set Medication");
        rows.add("Appointment");
        rows.add("Log Out");

        String pic=sharedpreferences.getString("profilepic","http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterDoctor drawerAdapter = new DrawerAdapterDoctor(getApplicationContext(), rows, email, pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        add=(ImageButton)findViewById(R.id.imageButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),DoctorSetAppointment.class);
                DoctorAppointment.this.startActivity(i);
            }
        });
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swiperefresh);

        appListview=(ListView)findViewById(R.id.appointmentListView);
        appoinmentslist=new ArrayList<ViewAppoinment>();
        adapter=new DoctorMyAppointment(getApplicationContext(),appoinmentslist);
        appListview.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getAppointmentList(userName);
            }
        });
        /// Get appointment for doctor
        /*if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


            String URL = AppBaseURL.BaseURL + "appointment/"+userName;
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, (JSONObject) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // the response is already constructed as a JSONObject!
                    Log.d("Response-", response.toString());
                    Gson gs=new Gson();
                    AppointmentParser appointmentParser=gs.fromJson(response.toString(),AppointmentParser.class);
                    appListview.setAdapter(new DoctorMyAppointment(getApplicationContext(),appointmentParser.getData()));




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
        }*/

    }

    // Handle back button event fired.
    @Override
    public void onBackPressed() {
        // Go To Dashboard
        Intent i = new Intent(getApplicationContext(), DoctorDashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Stopped"," Calleed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resuem"," Calleed");

    }

    @Override
    public void onRefresh() {
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);

        final String userName=sharedpreferences.getString("username","");

        getAppointmentList(userName);
    }
    private void getAppointmentList(String userName){

        /// Get appointment for doctor
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

            swipeRefreshLayout.setRefreshing(true);
            String URL = AppBaseURL.BaseURL + "appointment/"+userName;
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, (JSONObject) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // the response is already constructed as a JSONObject!
                    Log.d("Response-", response.toString());
                    Gson gs=new Gson();
                    AppointmentParser appointmentParser=gs.fromJson(response.toString(),AppointmentParser.class);
                    //adapter(getApplicationContext(),appointmentParser.getData());
                    appoinmentslist.clear();
                    appoinmentslist.addAll(appointmentParser.getData());
                    //appListview.setAdapter(new DoctorMyAppointment(getApplicationContext(),appointmentParser.getData()));
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);



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
}
