package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.adapter.DaysOfWeekAdapter;
import iot.connect.com.connectoutpatient.activity.doctor.adapter.DoctorMyAppointment;
import iot.connect.com.connectoutpatient.modals.AppointmentParser;
import iot.connect.com.connectoutpatient.modals.ViewAppoinment;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 19-Apr-16.
 */
public class DoctorDashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    GraphView graph;
    SharedPreferences sharedpreferences;
    ListView appListview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("Dashboard Activity");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Patients");
        rows.add("Set Medication");
        rows.add("Appointment");
        rows.add("Log Out");
        final String email=sharedpreferences.getString("email","");
        final String userName=sharedpreferences.getString("username","");
        String pic=sharedpreferences.getString("profilepic","http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterDoctor drawerAdapter = new DrawerAdapterDoctor(getApplicationContext(), rows, email, pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appListview=(ListView)findViewById(R.id.appointmentListView);
        /// Get appointment for doctor
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


            String URL = AppBaseURL.BaseURL + "appointment/"+userName;
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, (JSONObject) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // the response is already constructed as a JSONObject!
                    Log.d("Response-", response.toString());
                    Gson gs=new Gson();
                    AppointmentParser appointmentParser=gs.fromJson(response.toString(),AppointmentParser.class);
                    ArrayList<ViewAppoinment> viewAppoinments=new ArrayList<ViewAppoinment>();
                    for(int i=0;i<appointmentParser.getData().size();i++){
                        String[] date=appointmentParser.getData().get(i).getDate().split("T");
                        Date date1=new Date();
                        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date1);
                        if(modifiedDate.equals(date[0])){
                            viewAppoinments.add(appointmentParser.getData().get(i));
                        }

                    }
                    appListview.setAdapter(new DoctorMyAppointment(getApplicationContext(),viewAppoinments));




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
