package iot.connect.com.connectoutpatient.activity.doctor;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.squareup.picasso.Picasso;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;

/**
 * Created by Deep on 27-Apr-16.
 */
public class DoctorPatientDetail extends AppCompatActivity {
    ImageView profilepic;
    TextView Name,Moreinfo;
    GraphView graph;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_detail);
        profilepic=(ImageView)findViewById(R.id.patientProfilePic);
        Name=(TextView)findViewById(R.id.patientdetailName);
        Moreinfo=(TextView)findViewById(R.id.patientdetailMoreInfo);
        Intent i=getIntent();
        String PName=i.getStringExtra("patientName");
        String PPicUrl=i.getStringExtra("picURL");
        String PUsername=i.getStringExtra("id");
        Picasso.with(getApplicationContext()).load(AppBaseURL.BaseURL+PPicUrl).into(profilepic);
        Name.setText("Name: "+PName);
        Moreinfo.setText(PName+" has recently been diagnosed with heart pain issues.\nThe Patient have been recently examined for regular post surgery check and shown good sign of recovery. ");


        /// Generate graph

        graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 71),
                new DataPoint(1, 75),
                new DataPoint(2, 63),
                new DataPoint(3, 82),
                new DataPoint(4, 66), new DataPoint(5, 81),
                new DataPoint(6, 85),
                new DataPoint(7, 83),
                new DataPoint(8, 87),
                new DataPoint(9, 76), new DataPoint(10, 71),
                new DataPoint(11, 65),
                new DataPoint(12, 63),
                new DataPoint(13, 72),
                new DataPoint(14, 76),
                new DataPoint(15, 63),
                new DataPoint(16, 72),
                new DataPoint(18, 65),
                new DataPoint(19, 61),
                new DataPoint(20, 59),new DataPoint(21, 62),
        });
        LineGraphSeries<DataPoint> seriesLow=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,60),
                new DataPoint(23,60)


        });
        LineGraphSeries<DataPoint> seriesHigh=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,85),
                new DataPoint(23,85)


        });
        graph.setTitle("Heart Rate Log");
        graph.addSeries(series);
        graph.addSeries(seriesLow);
        graph.addSeries(seriesHigh);
        seriesLow.setColor(Color.BLUE);
        seriesHigh.setColor(Color.BLUE);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        series.setColor(Color.GREEN);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"0", "2", "4","6","8","10","12","14","16","18","20","22"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"50", "70", "90","110","130"});
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(23);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(130);

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(DoctorPatientDetail.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
