package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.AddMedication;
import iot.connect.com.connectoutpatient.modals.Medication;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 04-May-16.
 */
public class DoctorEditPatientMedication extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    Button Save,Delete;
    EditText patientID,MedicationName,Dosage,Manufature;
    Spinner days,times;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_patient_medication);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        Intent i=getIntent();
        final String patientid,Medicationname,dosage,manufacture;
        patientid=i.getStringExtra("patientid");
        Medicationname=i.getStringExtra("Medicationname");
        dosage=i.getStringExtra("dosage");
        manufacture=i.getStringExtra("manufacture");
        // Edit text
        patientID=(EditText)findViewById(R.id.input_patientID);
        patientID.setText(patientid);
        MedicationName=(EditText)findViewById(R.id.input_medicationName);
        MedicationName.setText(Medicationname);
        Dosage=(EditText)findViewById(R.id.input_dosage);
        Dosage.setText(dosage);
        Manufature=(EditText)findViewById(R.id.input_manufacture);
        Manufature.setText(manufacture);
        Save=(Button)findViewById(R.id.btn_save);
        Delete=(Button)findViewById(R.id.btn_delete);
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

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientid=patientID.getText().toString();
                String medicationname=MedicationName.getText().toString();
                String dosage=Dosage.getText().toString();
                String manufature=Manufature.getText().toString();
                String daysvalue=days.getSelectedItem().toString();
                String timevalue=times.getSelectedItem().toString();

                Medication m=new Medication();
                m.setName(medicationname);
                m.setDosage(dosage);
                m.setDoctorId(sharedpreferences.getString("email",""));
                m.setCompany(manufature);
                m.setDay(daysvalue);
                m.setTime(timevalue);

                AddMedication addMedication=new AddMedication();
                addMedication.setPatientId(patientid);
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


                    String url = AppBaseURL.BaseURL + "medication/"+patientid+"/"+Medicationname;
                    JSONObject jsonObject;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                            js, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Response", jsonObject.toString());
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();

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


        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


                    String url = AppBaseURL.BaseURL + "medication/"+patientid+"/"+Medicationname;
                    JSONObject jsonObject;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url,
                            new JSONObject() , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Response", jsonObject.toString());
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();

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


            finish();
            }
        });
    }
}
