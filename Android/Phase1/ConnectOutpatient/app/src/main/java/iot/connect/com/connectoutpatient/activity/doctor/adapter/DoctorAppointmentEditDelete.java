package iot.connect.com.connectoutpatient.activity.doctor.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.AddAppoinment;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 09-May-16.
 */
public class DoctorAppointmentEditDelete extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    ImageButton imageButton;
    Spinner times;
    TextView tv,patientNametv;
    int year_x, month_x, date_x;
    static final int DILOG_ID = 0;
    Button btnedit,btndelete;
    HashMap<String,String> dateset;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_delete_edit);
        tv=(TextView)findViewById(R.id.TxtViewDate);
        patientNametv=(TextView)findViewById(R.id.patientNameApp);
        btnedit=(Button)findViewById(R.id.btn_editApp);
        btndelete=(Button)findViewById(R.id.btn_deleteApp);
        Intent i=getIntent();
        final String patientid=i.getStringExtra("patientID");
        final String patientName=i.getStringExtra("patientName");
        final String _id=i.getStringExtra("_id");
        final String patientURL=i.getStringExtra("patientURL");
        final String[] date=i.getStringExtra("date").split("T");
        final String doctorid=i.getStringExtra("doctorID");
        tv.setText("Date:"+date[0]);
        patientNametv.setText(patientName);


        // set spinner data - times
        times = (Spinner)findViewById(R.id.timepicker);
        ArrayAdapter<CharSequence> timedapter=ArrayAdapter.createFromResource(this,R.array.time,android.R.layout.simple_spinner_item);
        timedapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        times.setAdapter(timedapter);

        //Date Picker
        final Calendar c=Calendar.getInstance();
        year_x= c.get(Calendar.YEAR);
        month_x=c.get(Calendar.MONTH);
        date_x=c.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();

        // Edit
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timevalue=timevalue=times.getSelectedItem().toString();
                String[] dateSet=null;
                try{
                    dateSet=tv.getText().toString().split(":");
                }catch(Exception e){
                    e.printStackTrace();
                }
                AddAppoinment addAppoinment=new AddAppoinment();
                addAppoinment.setDate(dateSet[1]);
                addAppoinment.setPatientName(patientName);
                addAppoinment.setDoctorId(doctorid);
                addAppoinment.setPatientId(patientid);
                addAppoinment.setPatientProfileImageURL(patientURL);
                addAppoinment.setTime(timevalue);

                JSONObject js = null;
                //js.
                Log.d("Set Objects", " Set Objects 2");
                Gson gs = new Gson();
                Log.d("gson to object", gs.toJson(addAppoinment));
                try {
                    js = new JSONObject(gs.toJson(addAppoinment));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    String url = AppBaseURL.BaseURL + "appointment/"+_id;
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

            }
        });
        // Delete Appointment
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


                    String url = AppBaseURL.BaseURL + "appointment/"+_id;
                    JSONObject jsonObject;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url,
                            new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Response", jsonObject.toString());
                            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
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

            }
        });


    }
    public void showDialogOnButtonClick(){
        imageButton=(ImageButton)findViewById(R.id.datepicker);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DILOG_ID);
            }
        });
    }

    @Override
    public Dialog onCreateDialog(int id){
        if(id == DILOG_ID){
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,date_x);
        }else{
            return null;
        }
    }
    private DatePickerDialog.OnDateSetListener dpickerListner= new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfYear){
            year_x=year;
            month_x=monthOfYear+1;
            date_x=dayOfYear;
            tv=(TextView)findViewById(R.id.TxtViewDate);
            tv.setText("Date:"+year_x+"-"+month_x+"-"+date_x);

        }

    };
}
