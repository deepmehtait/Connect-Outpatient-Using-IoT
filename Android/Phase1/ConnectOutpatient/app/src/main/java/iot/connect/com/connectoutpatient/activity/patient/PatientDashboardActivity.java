package iot.connect.com.connectoutpatient.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.DoctorDashboardActivity;
import iot.connect.com.connectoutpatient.modals.HealthData;
import iot.connect.com.connectoutpatient.modals.SignIn;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;

/**
 * Created by Deep on 19-Apr-16.
 */
public class PatientDashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    GraphView graph;
    TextView min,max,average;
    Runnable timer1;
    int count=0;
    private final Handler mHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        recyclerView=(RecyclerView)findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("Patient Dashboard");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Medications");
        rows.add("Appointment");
        rows.add("Log Out");
        String email=sharedpreferences.getString("email","");
        String pic=sharedpreferences.getString("profilepic","http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterPatient drawerAdapter = new DrawerAdapterPatient(getApplicationContext(),rows,email,pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        min=(TextView)findViewById(R.id.minValue);
        max=(TextView)findViewById(R.id.maxValue);
        average=(TextView)findViewById(R.id.averageValue);

        graph = (GraphView) findViewById(R.id.graph);
        final LineGraphSeries<DataPoint> seriesHR=new LineGraphSeries<DataPoint>();

        // get fitbit data
        String url= AppBaseURL.BaseURL+"fitbitdata/testuser1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("Response", jsonObject.toString());
                Gson gs=new Gson();
                HealthData healthData=gs.fromJson(jsonObject.toString(),HealthData.class);
                if(healthData.getResult()==null){
                    String[] minvalue = healthData.getMinValue().split(Pattern.quote("."));
                    String[] maxvalue = healthData.getMaxValue().split(Pattern.quote("."));
                    String[] avgvalue = healthData.getAvgValue().split(Pattern.quote("."));
                    min.setText(minvalue[0]);
                    max.setText(maxvalue[0]);
                    average.setText(avgvalue[0]);
                    Log.d("length-",""+healthData.getHealthdata().size());

                    for(int i=0;i<healthData.getHealthdata().size();i++){
                        Log.d("i-",healthData.getHealthdata().get(i).getValue()+","+i);
                        seriesHR.appendData(new DataPoint(getCount(),Double.parseDouble( healthData.getHealthdata().get(i).getValue())),true,50);
                    }
                    graph.addSeries(seriesHR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.getMessage());
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjectRequest.setShouldCache(false);
        Volley.newRequestQueue(getApplication()).add(jsonObjectRequest);


        timer1=new Runnable() {
            @Override
            public void run() {
                Log.d("run","run now");
                String url= AppBaseURL.BaseURL+"fitbitdata/testuser1";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                        new JSONObject(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Response", jsonObject.toString());
                        Gson gs=new Gson();
                        HealthData healthData=gs.fromJson(jsonObject.toString(),HealthData.class);
                        if(healthData.getResult()==null){
                            String[] minvalue = healthData.getMinValue().split(Pattern.quote("."));
                            String[] maxvalue = healthData.getMaxValue().split(Pattern.quote("."));
                            String[] avgvalue = healthData.getAvgValue().split(Pattern.quote("."));
                            min.setText(minvalue[0]);
                            max.setText(maxvalue[0]);
                            average.setText(avgvalue[0]);
                            Log.d("length-",""+healthData.getHealthdata().size());
                            for(int i=0;i<healthData.getHealthdata().size();i++){
                                Log.d("i-",healthData.getHealthdata().get(i).getValue()+","+i);
                                seriesHR.appendData(new DataPoint(getCount(),Double.parseDouble( healthData.getHealthdata().get(i).getValue())),true,50);
                            }
                            graph.addSeries(seriesHR);
                            //Toast.makeText(context,jsonObject.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                jsonObjectRequest.setShouldCache(false);
                Volley.newRequestQueue(getApplication()).add(jsonObjectRequest);
            }
        };
        mHandler.postDelayed(timer1,60000);
        graph.setTitle("Heart Rate Log");
        graph.getViewport().setScrollable(true);
        seriesHR.setDrawDataPoints(true);
        seriesHR.setDataPointsRadius(5);
        seriesHR.setColor(Color.GREEN);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setVerticalLabels(new String[] {"50", "70", "90","110","130"});
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(130);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        seriesHR.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(PatientDashboardActivity.this, "Heart Rate: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private int  getCount(){
        return count++;

    }
}
