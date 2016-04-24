package iot.connect.com.connectoutpatient.activity.patient;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medications);
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
        DrawerAdapterPatient drawerAdapter = new DrawerAdapterPatient(getApplicationContext(),rows,"Patient@gmail.com","https:");
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dayAndMedication dam=new dayAndMedication();
        dam.setDay("monday");
        dam.setMorning(" Take medication 1, 2, 3");
        dam.setNoon(" Take medication 3, 2");
        dam.setEvening(" Take medication 4, 5, 1");
        ArrayList<dayAndMedication> al=new ArrayList<dayAndMedication>();
        al.add(dam);
        al.add(dam);
        al.add(dam);
        al.add(dam);
        al.add(dam);
        al.add(dam);
        al.add(dam);
        al.add(dam);
        dayMedication=(ListView)findViewById(R.id.DaysListView);
        dayMedication.setAdapter(new DaysOfWeekAdapter(getApplicationContext(),al));

    }
}
