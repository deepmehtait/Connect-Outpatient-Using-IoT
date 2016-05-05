package iot.connect.com.connectoutpatient.activity.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.HashMap;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.activity.doctor.DoctorEditPatientMedication;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;

/**
 * Created by Deep on 23-Apr-16.
 */
public class DaysOfWeekAdapter extends BaseAdapter {

    ArrayList<dayAndMedication> daysOfWeekDatas = new ArrayList<dayAndMedication>();
    Context context;
    String pusername;
    private static LayoutInflater inflater = null;

    public DaysOfWeekAdapter(Context context, ArrayList<dayAndMedication> daysOfWeekDatas,String pusername) {
        this.context = context;
        this.daysOfWeekDatas = daysOfWeekDatas;
        this.pusername=pusername;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return daysOfWeekDatas.size();
    }

    @Override
    public dayAndMedication getItem(int position) {
        // TODO Auto-generated method stub
        return daysOfWeekDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        ImageView dayImage;
        TextView MedicatioName,Dosage, timetext,manufacture;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = inflater.inflate(R.layout.patient_medication_list_view, null);

        HashMap<String,Integer> colrmap=new HashMap<String,Integer>();
        colrmap.put("Daily", Color.parseColor("#FF5722"));
        colrmap.put("Monday",Color.parseColor("#8BC34A"));
        colrmap.put("Tuesday",Color.parseColor("#795548"));
        colrmap.put("Wednesday",Color.parseColor("#03A9F4"));
        colrmap.put("Thursday",Color.parseColor("#FFEB3B"));
        colrmap.put("Friday",Color.parseColor("#E91E63"));
        colrmap.put("Saturday",Color.parseColor("#607D8B"));
        colrmap.put("Sunday",Color.parseColor("#009688"));


        holder.MedicatioName = (TextView) row.findViewById(R.id.MedicatioName);
        holder.MedicatioName.setText("Name: "+daysOfWeekDatas.get(position).getName());
        holder.Dosage = (TextView) row.findViewById(R.id.Dosage);
        holder.Dosage.setText("Dosage: " + daysOfWeekDatas.get(position).getDosage());
        holder.timetext = (TextView) row.findViewById(R.id.timetext);
        holder.timetext.setText("timetext: " + daysOfWeekDatas.get(position).getTime());
        holder.manufacture = (TextView) row.findViewById(R.id.manufacture);
        holder.manufacture.setText("manufacture: " + daysOfWeekDatas.get(position).getCompany());
        holder.dayImage=(ImageView)row.findViewById(R.id.dayImage);

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(daysOfWeekDatas.get(position).getDay().substring(0,2), colrmap.get(daysOfWeekDatas.get(position).getDay()));
        holder.dayImage.setImageDrawable(drawable1);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context, DoctorEditPatientMedication.class);
                i.putExtra("patientid",pusername);
                i.putExtra("Medicationname",daysOfWeekDatas.get(position).getName());
                i.putExtra("dosage",daysOfWeekDatas.get(position).getDosage());
                i.putExtra("manufacture",daysOfWeekDatas.get(position).getCompany());
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                //Toast.makeText(context,"Name: "+daysOfWeekDatas.get(position).getName(),Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }


}
