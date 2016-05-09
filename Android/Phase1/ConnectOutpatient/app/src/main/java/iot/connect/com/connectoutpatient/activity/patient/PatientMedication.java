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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.adapter.DaysOfWeekAdapter;
import iot.connect.com.connectoutpatient.activity.doctor.adapter.DoctorMyPatientAdapter;
import iot.connect.com.connectoutpatient.modals.DaysOfWeekData;
import iot.connect.com.connectoutpatient.modals.MyPatientList;
import iot.connect.com.connectoutpatient.modals.MyPatientListDetails;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 23-Apr-16.
 */
public class PatientMedication extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ListView dayMedication;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medications);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        recyclerView=(RecyclerView)findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("My Medications");
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
        dayMedication=(ListView)findViewById(R.id.DaysListView);
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            String URL = AppBaseURL.BaseURL + "medication/"+userName;
            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, URL, (JSONObject) null, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    // the response is already constructed as a JSONObject!
                    ArrayList<dayAndMedication> dayAndMedications=new ArrayList<dayAndMedication>();
                    Log.d("Response-", response.toString());
                    Log.d("Size", ""+response.length());
                    for(int i=0;i<response.length();i++){
                        JSONObject obj=response.optJSONObject(i);
                        dayAndMedication dam=new dayAndMedication();
                        try{
                            dam.setName(obj.getString("name"));
                            dam.setDosage(obj.getString("dosage"));
                            dam.setCompany(obj.getString("company"));
                            dam.setDay(obj.getString("day"));
                            dam.setTime(obj.getString("time"));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        dayAndMedications.add(dam);
                    }
                    dayMedication.setAdapter(new DaysOfWeekAdapter(getApplicationContext(),dayAndMedications));
                    //DaysOfWeekData daysOfWeekData=new DaysOfWeekData();
                    //Gson gs = new Gson();

                    //daysOfWeekData=gs.fromJson(response.toString(),DaysOfWeekData.class);
                    //Log.d("Arrlis","Size = "+daysOfWeekData.getDaysofweek().size());
                    //dayMedication.setAdapter(new DaysOfWeekAdapter(getApplicationContext(),daysOfWeekData.getDaysofweek()));
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
