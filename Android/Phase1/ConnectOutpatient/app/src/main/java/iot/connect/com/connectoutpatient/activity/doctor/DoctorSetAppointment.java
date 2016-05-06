package iot.connect.com.connectoutpatient.activity.doctor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.MyPatientList;
import iot.connect.com.connectoutpatient.modals.MyPatientListDetails;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;
import iot.connect.com.connectoutpatient.utils.AppStatus;

/**
 * Created by Deep on 05-May-16.
 */
public class DoctorSetAppointment extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    Spinner times,patientName;
    ImageButton imageButton;
    TextView tv;
    int year_x, month_x, date_x;
    static final int DILOG_ID = 0;
    Button btnConfirm;
    HashMap<String,String> dateset;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_form);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        final String email=sharedpreferences.getString("email","");
        final String userName=sharedpreferences.getString("username","");
        final HashMap<String,String> patientNameMapper=new HashMap<String,String>();
        final HashMap<String,String> patientProfileMappter=new HashMap<String, String>();
        final ArrayList<String> patientNameList=new ArrayList<String>();
        btnConfirm=(Button)findViewById(R.id.btn_confirm);
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
                            patientProfileMappter.put(mpld.get(i).getDisplayName(),mpld.get(i).getProfileImageURL());
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
        tv=(TextView)findViewById(R.id.TxtViewDate);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName=patientNameMapper.get(patientName.getSelectedItem().toString());
                String pUrl=patientProfileMappter.get(patientName.getSelectedItem().toString());
                String timevalue=times.getSelectedItem().toString();
                String[] date=tv.getText().toString().split(":");
                Log.d("msg","Pid-"+pName+" time-"+timevalue+" Date-"+date[1]+" url-"+pUrl);
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
