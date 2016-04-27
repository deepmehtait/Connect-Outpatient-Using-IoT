package iot.connect.com.connectoutpatient.activity.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.MyPatientListDetails;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;

/**
 * Created by Deep on 22-Apr-16.
 */
public class DoctorMyPatientAdapter extends BaseAdapter {
    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> photoUrl =new ArrayList<String>();
    ArrayList<MyPatientListDetails> mpld=new ArrayList<MyPatientListDetails>();
    Context context;
    private static LayoutInflater inflater=null;

    /*public DoctorMyPatientAdapter(Context context,ArrayList<String> name,ArrayList<String> url){
        this.context=context;
        this.name=name;
        this.photoUrl=url;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/
    public DoctorMyPatientAdapter(Context context,ArrayList<MyPatientListDetails> mpld){
        this.context=context;
        this.mpld=mpld;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mpld.size();
    }
    @Override
    public MyPatientListDetails getItem(int position) {
        // TODO Auto-generated method stub
        return mpld.get(position);
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
        holder.patientName.setText(mpld.get(position).getDisplayName());
        holder.patientPhoto = (ImageView) row.findViewById(R.id.patientImage);
        Picasso.with(row.getContext()).load(AppBaseURL.BaseURL+mpld.get(position).getProfileImageURL()).error(R.drawable.noimg).into(holder.patientPhoto);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Name-"+mpld.get(position).getDisplayName(),Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    }
}
