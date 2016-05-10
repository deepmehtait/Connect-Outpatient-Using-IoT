package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.Intent;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.adapter.DoctorMyPatientAdapter;
import iot.connect.com.connectoutpatient.activity.patient.PatientDashboardActivity;
import iot.connect.com.connectoutpatient.gcm.RegisterToken;
import iot.connect.com.connectoutpatient.modals.AddMedication;
import iot.connect.com.connectoutpatient.modals.Medication;
import iot.connect.com.connectoutpatient.modals.MyPatientList;
import iot.connect.com.connectoutpatient.modals.MyPatientListDetails;
import iot.connect.com.connectoutpatient.modals.SignIn;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 04-May-16.
 */
public class DoctorSetPatientMedication extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    Button Save;
    EditText MedicationName,Dosage,Manufature;
    Spinner days,times,patientName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setpatient_medication);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("Set Medication");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Patients");
        rows.add("Set Medication");
        rows.add("Appointment");
        rows.add("Settings");
        final String email=sharedpreferences.getString("email","");
        final String userName=sharedpreferences.getString("username","");
        String pic=sharedpreferences.getString("profilepic","http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterDoctor drawerAdapter = new DrawerAdapterDoctor(getApplicationContext(), rows, email, pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Edit text

        MedicationName=(EditText)findViewById(R.id.input_medicationName);
        Dosage=(EditText)findViewById(R.id.input_dosage);
        Manufature=(EditText)findViewById(R.id.input_manufacture);
        Save=(Button)findViewById(R.id.btn_save);

        final HashMap<String,String> patientNameMapper=new HashMap<String,String>();
        final ArrayList<String> patientNameList=new ArrayList<String>();
        patientName=(Spinner)findViewById(R.id.patientNamePicker);
        // Get List of Patients of current doctor
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


            String URL = AppBaseURL.BaseURL + "doctor/patients/"+userName;
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, URL, (JSONObject) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // the response is already constructed as a JSONObject!
                    Log.d("Response-", response.toString());
                    MyPatientList mPL = new MyPatientList();
                    Gson gs = new Gson();
                    mPL = gs.fromJson(response.toString(), MyPatientList.class);

                    if (mPL.getMessage().matches("Success")) {
                        ArrayList<MyPatientListDetails> mpld = new ArrayList<MyPatientListDetails>(mPL.getData());
                        for(int i=0;i<mpld.size();i++){
                            patientNameMapper.put(mpld.get(i).getDisplayName(),mpld.get(i).getUsername());
                            patientNameList.add(mpld.get(i).getDisplayName());
                        }

                        ArrayAdapter<String> arrayadaptername=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,patientNameList);
                        arrayadaptername.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        arrayadaptername.notifyDataSetChanged();
                        patientName.setAdapter(arrayadaptername);
                        patientName.setBackgroundColor(Color.BLUE);

                        //mypatientList.setAdapter(new DoctorMyPatientAdapter(getApplicationContext(), mpld));
                    }


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



        // set spinner data - days
        days=(Spinner)findViewById(R.id.daypicker);
        ArrayAdapter<CharSequence> dayadapter=ArrayAdapter.createFromResource(this,R.array.daysList,android.R.layout.simple_spinner_item);
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days.setAdapter(dayadapter);

        // onItemSelected listner
        String daysvalue=days.getSelectedItem().toString();
        Log.d("Spinner",daysvalue);
        // set spinner data - times
        times = (Spinner)findViewById(R.id.timepicker);
        ArrayAdapter<CharSequence> timedapter=ArrayAdapter.createFromResource(this,R.array.time,android.R.layout.simple_spinner_item);
        timedapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        times.setAdapter(timedapter);

        // save button onclick listner

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String patientid=patientID.getText().toString();
                String medicationname=MedicationName.getText().toString();
                String dosage=Dosage.getText().toString();
                String manufature=Manufature.getText().toString();
                String daysvalue=days.getSelectedItem().toString();
                String timevalue=times.getSelectedItem().toString();
                String pName=patientName.getSelectedItem().toString();

                Medication m=new Medication();
                m.setName(medicationname);
                m.setDosage(dosage);
                m.setDoctorId(sharedpreferences.getString("username",""));
                m.setCompany(manufature);
                m.setDay(daysvalue);
                m.setTime(timevalue);

                AddMedication addMedication=new AddMedication();
                addMedication.setPatientId(patientNameMapper.get(pName));
                addMedication.setMedications(m);

                JSONObject js = null;
                //js.
                Log.d("Set Objects", " Set Objects 2");
                Gson gs = new Gson();
                Log.d("gson to object", gs.toJson(addMedication));
                try {
                    js = new JSONObject(gs.toJson(addMedication));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Add new Medication

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


                    String url = AppBaseURL.BaseURL + "medication";
                    JSONObject jsonObject;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                            js, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Response", jsonObject.toString());
                            //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }
                    };
                    Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
                } else {
                    // If no network connectivity notify user
                    Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }



                //Log.d("data to send",patientid+"-"+medicationname+"-"+dosage+"-"+manufature +"-"+daysvalue+"-"+timevalue);
            }
        });

    }
    // Handle back button event fired.
    @Override
    public void onBackPressed() {
        // Go To Dashboard
        Intent i = new Intent(getApplicationContext(), DoctorDashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
