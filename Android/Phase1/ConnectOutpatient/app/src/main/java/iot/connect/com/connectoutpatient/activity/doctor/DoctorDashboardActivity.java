package iot.connect.com.connectoutpatient.activity.doctor;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.adapter.DaysOfWeekAdapter;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;

/**
 * Created by Deep on 19-Apr-16.
 */
public class DoctorDashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    GraphView graph;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
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
        rows.add("Settings");
        DrawerAdapterDoctor drawerAdapter = new DrawerAdapterDoctor(getApplicationContext(), rows, "Doctor@gmail.com", "https:");
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 71),
                new DataPoint(1, 75),
                new DataPoint(2, 63),
                new DataPoint(3, 82),
                new DataPoint(4, 66), new DataPoint(5, 81),
                new DataPoint(6, 85),
                new DataPoint(7, 83),
                new DataPoint(8, 72),
                new DataPoint(9, 76), new DataPoint(10, 71),
                new DataPoint(11, 65),
                new DataPoint(12, 63),
                new DataPoint(13, 72),
                new DataPoint(14, 76)
        });
        LineGraphSeries<DataPoint> seriesLow=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,60),
                new DataPoint(23,60)


        });
        LineGraphSeries<DataPoint> seriesHigh=new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0,100),
                new DataPoint(23,100)


        });
        graph.setTitle("Heart Rate Log");
        graph.addSeries(series);
        graph.addSeries(seriesLow);
        graph.addSeries(seriesHigh);
        seriesLow.setColor(Color.GREEN);
        seriesHigh.setColor(Color.GREEN);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"0", "2", "4","6","8","10","12","14","16","18","20","22"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"50", "70", "90","110","130","150"});
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(23);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(170);

        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(DoctorDashboardActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
