package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.HealthData;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;

/**
 * Created by Deep on 27-Apr-16.
 */
public class DoctorPatientDetail extends AppCompatActivity {
    ImageView profilepic;
    TextView Name,Moreinfo;
    TextView min,max,average;
    GraphView graph;
    Button viewMedication;
    Runnable timer1;
    int count=0;
    private final Handler mHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_detail);
        profilepic=(ImageView)findViewById(R.id.patientProfilePic);
        Name=(TextView)findViewById(R.id.patientdetailName);
        Moreinfo=(TextView)findViewById(R.id.patientdetailMoreInfo);
        viewMedication=(Button)findViewById(R.id.btn_viewMedication);
        Intent i=getIntent();
        String PName=i.getStringExtra("patientName");
        String PPicUrl=i.getStringExtra("picURL");
        final String PUsername=i.getStringExtra("id");
        Picasso.with(getApplicationContext()).load(AppBaseURL.BaseURL+PPicUrl).into(profilepic);
        Name.setText("Name: "+PName);
        Moreinfo.setText(PName+" has recently been diagnosed with heart pain issues.\nThe Patient have been recently examined for regular post surgery check and shown good sign of recovery. ");
        final int counter=0;
         min=(TextView)findViewById(R.id.minValue);
         max=(TextView)findViewById(R.id.maxValue);
         average=(TextView)findViewById(R.id.averageValue);

        /// Generate graph

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


                min.setText(healthData.getMinValue());
                max.setText(healthData.getMaxValue());
                average.setText(healthData.getAvgValue());
                Log.d("length-",""+healthData.getHealthdata().size());

                for(int i=0;i<healthData.getHealthdata().size();i++){
                    Log.d("i-",""+i);
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
                            min.setText(healthData.getMinValue());
                            max.setText(healthData.getMaxValue());
                            average.setText(healthData.getAvgValue());
                            Log.d("length-",""+healthData.getHealthdata().size());
                            for(int i=0;i<healthData.getHealthdata().size();i++){
                                Log.d("i-",""+i);
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
        //        mHandler.postDelayed(this,60000);
                }
        };
        mHandler.postDelayed(timer1,60000);




        /*LineGraphSeries<DataPoint> seriesLow=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,60),
                new DataPoint(200,60)


        });
        LineGraphSeries<DataPoint> seriesHigh=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,85),
                new DataPoint(200,85)


        });*/
        graph.setTitle("Heart Rate Log");

        //graph.addSeries(seriesLow);
        //graph.addSeries(seriesHigh);
        //seriesLow.setColor(Color.BLUE);
        //seriesHigh.setColor(Color.BLUE);
        //graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        seriesHR.setDrawDataPoints(true);
        seriesHR.setDataPointsRadius(5);
        seriesHR.setColor(Color.GREEN);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        //staticLabelsFormatter.setHorizontalLabels(new String[] {"0", "2", "4","6","8","10","12","14","16","18","20","22"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"50", "70", "90","110","130"});
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(23);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(130);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        seriesHR.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(DoctorPatientDetail.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        // Handle New Medication button
        viewMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorPatientDetail.this,DoctorViewPatientMedication.class);
                i.putExtra("id",PUsername);
                startActivity(i);
            }
        });

    }
    private int  getCount(){
        return count++;

    }

}
