package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.adapter.DaysOfWeekAdapter;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 04-May-16.
 */
public class DoctorViewPatientMedication extends AppCompatActivity  {
    ListView dayMedication;
    ListAdapter adapter;
    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medications);
        Intent i=getIntent();
        final String pusername=i.getStringExtra("id");
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        dayMedication=(ListView)findViewById(R.id.DaysListView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Patient Medication");

        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


            String URL = AppBaseURL.BaseURL + "medication/"+pusername;

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
                    adapter=new DaysOfWeekAdapter(getApplicationContext(),dayAndMedications,pusername);
                    dayMedication.setAdapter(adapter);

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

    @Override
    protected void onResume() {
        super.onResume();
       // adapter.notifyAll();
    }


}
