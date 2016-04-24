package iot.connect.com.connectoutpatient.activity.doctor;

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
import iot.connect.com.connectoutpatient.activity.doctor.adapter.DoctorMyPatientAdapter;

/**
 * Created by Deep on 19-Apr-16.
 */
public class DoctorMyPatientListActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ListView mypatientList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_mypatient_list);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        recyclerView=(RecyclerView)findViewById(R.id.drawer_recyclerView);
        mypatientList=(ListView)findViewById(R.id.mypatientListView);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setTitle("My Patient List");
        List<String> rows = new ArrayList<>();
        rows.add("Dashboard");
        rows.add("My Patients");
        rows.add("Settings");
        DrawerAdapterDoctor drawerAdapter = new DrawerAdapterDoctor(getApplicationContext(),rows,"Doctor@gmail.com","https:");
        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> name=new ArrayList<String>();
        name.add("Patient 1");
        name.add("Patient 2");
        name.add("Patient 3");
        name.add("Patient 4");
        name.add("Patient 5");
        name.add("Patient 6");
        name.add("Patient 1");
        name.add("Patient 2");
        name.add("Patient 3");
        name.add("Patient 4");
        name.add("Patient 5");
        name.add("Patient 6");
        ArrayList<String> url=new ArrayList<String>();
        url.add("http://52.8.186.40/modules/users/client/img/profile/default.png");
        url.add("http://handh.headlinesandhero.netdna-cdn.com/wordP/wp-content/uploads/2015/11/Andreea-Cristina1.jpg");
        url.add("http://www.famousbirthdays.com/faces/cerny-amanda-image.jpg");
        url.add("http://52.8.186.40/modules/users/client/img/profile/default.png");
        url.add("http://handh.headlinesandhero.netdna-cdn.com/wordP/wp-content/uploads/2015/11/Andreea-Cristina1.jpg");
        url.add("http://www.famousbirthdays.com/faces/cerny-amanda-image.jpg");
        url.add("http://52.8.186.40/modules/users/client/img/profile/default.png");
        url.add("http://handh.headlinesandhero.netdna-cdn.com/wordP/wp-content/uploads/2015/11/Andreea-Cristina1.jpg");
        url.add("http://www.famousbirthdays.com/faces/cerny-amanda-image.jpg");
        url.add("http://52.8.186.40/modules/users/client/img/profile/default.png");
        url.add("http://handh.headlinesandhero.netdna-cdn.com/wordP/wp-content/uploads/2015/11/Andreea-Cristina1.jpg");
        url.add("http://www.famousbirthdays.com/faces/cerny-amanda-image.jpg");
        mypatientList.setAdapter(new DoctorMyPatientAdapter(getApplicationContext(),name,url));

    }
}