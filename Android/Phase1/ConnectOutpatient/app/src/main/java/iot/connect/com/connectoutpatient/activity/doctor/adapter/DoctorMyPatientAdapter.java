package iot.connect.com.connectoutpatient.activity.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import iot.connect.com.connectoutpatient.R;

/**
 * Created by Deep on 22-Apr-16.
 */
public class DoctorMyPatientAdapter extends BaseAdapter {
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> photoUrl =new ArrayList<String>();
    Context context;
    private static LayoutInflater inflater=null;

    public DoctorMyPatientAdapter(Context context,ArrayList<String> name,ArrayList<String> url){
        this.context=context;
        this.name=name;
        this.photoUrl=url;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.size();
    }
    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return name.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public class Holder {
        TextView patientName;
        ImageView patientPhoto;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = inflater.inflate(R.layout.doctor_patient_list_view, null);
        holder.patientName = (TextView) row.findViewById(R.id.patientName);
        holder.patientName.setText(name.get(position));
        holder.patientPhoto = (ImageView) row.findViewById(R.id.patientImage);
        Picasso.with(row.getContext()).load(photoUrl.get(position)).into(holder.patientPhoto);
        return row;
    }
}
