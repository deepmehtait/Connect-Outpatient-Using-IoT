package iot.connect.com.connectoutpatient.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Calendar;

import iot.connect.com.connectoutpatient.R;

/**
 * Created by Deep on 15-Apr-16.
 */
public class GraphActivity extends AppCompatActivity {
    ImageButton imageButton;
    int year_x, month_x, date_x;
    static final int DILOG_ID = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        final Calendar  c=Calendar.getInstance();
        year_x= c.get(Calendar.YEAR);
        month_x=c.get(Calendar.MONTH);
        date_x=c.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();
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
                Toast.makeText(GraphActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
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
        public void onDateSet(DatePicker view,int year, int monthOfYear,int dayOfYear){
            year_x=year;
            month_x=monthOfYear+1;
            date_x=dayOfYear;
            Toast.makeText(getApplicationContext(),year_x+"/"+month_x+"/"+date_x,Toast.LENGTH_SHORT).show();
        }
    };
}