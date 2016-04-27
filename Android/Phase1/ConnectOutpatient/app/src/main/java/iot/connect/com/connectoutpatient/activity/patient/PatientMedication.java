package iot.connect.com.connectoutpatient.activity.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.adapter.DaysOfWeekAdapter;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;

/**
 * Created by Deep on 23-Apr-16.
 */
public class PatientMedication extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ListView dayMedication;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medications);
        sharedpreferences = getSharedPreferences("ConnectIoT", getApplicationContext().MODE_PRIVATE);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        recyclerView=(RecyclerView)findViewById(R.id.drawer_recyclerView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("My Medications");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Medications");
        rows.add("Settings");
        String email=sharedpreferences.getString("email","");
        String pic=sharedpreferences.getString("profilepic","http://www.sourcecoi.com/sites/default/files/team/defaultpic_0.png");
        DrawerAdapterPatient drawerAdapter = new DrawerAdapterPatient(getApplicationContext(),rows,email,pic);
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayAndMedication dam=new dayAndMedication();
        dam.setDay("Monday");
        dam.setMorning(" Take medication 1, 2, 3");
        dam.setNoon(" Take medication 3, 2");
        dam.setEvening(" Take medication 4, 5, 1");
        dayAndMedication dam1=new dayAndMedication();
        dam1.setDay("Tuesday");
        dam1.setMorning(" Take medication 1, 2, 3");
        dam1.setNoon(" Take medication 3, 2");
        dam1.setEvening(" Take medication 4, 5, 1");
        dayAndMedication dam2=new dayAndMedication();
        dam2.setDay("Wednesday");
        dam2.setMorning(" Take medication 1, 2, 3");
        dam2.setNoon(" Take medication 3, 2");
        dam2.setEvening(" Take medication 4, 5, 1");
        dayAndMedication dam3=new dayAndMedication();
        dam3.setDay("Thursday");
        dam3.setMorning(" Take medication 1, 2, 3");
        dam3.setNoon(" Take medication 3, 2");
        dam3.setEvening(" Take medication 4, 5, 1");
        dayAndMedication dam4=new dayAndMedication();
        dam4.setDay("Friday");
        dam4.setMorning(" Take medication 1, 2, 3");
        dam4.setNoon(" Take medication 3, 2");
        dam4.setEvening(" Take medication 4, 5, 1");
        dayAndMedication dam5=new dayAndMedication();
        dam5.setDay("Saturday");
        dam5.setMorning(" Take medication 1, 2, 3");
        dam5.setNoon(" Take medication 3, 2");
        dam5.setEvening(" Take medication 4, 5, 1");
        dayAndMedication dam6=new dayAndMedication();
        dam6.setDay("Sunday");
        dam6.setMorning(" Take medication 1, 2, 3");
        dam6.setNoon(" Take medication 3, 2");
        dam6.setEvening(" Take medication 4, 5, 1");
        ArrayList<dayAndMedication> al=new ArrayList<dayAndMedication>();
        al.add(dam);
        al.add(dam1);
        al.add(dam2);
        al.add(dam3);
        al.add(dam4);
        al.add(dam5);
        al.add(dam6);

        dayMedication=(ListView)findViewById(R.id.DaysListView);
        dayMedication.setAdapter(new DaysOfWeekAdapter(getApplicationContext(),al));

    }

    // Handle back button event fired.
    @Override
    public void onBackPressed()
    {
        // Go To Dashboard
        Intent i=new Intent(getApplicationContext(),PatientDashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
