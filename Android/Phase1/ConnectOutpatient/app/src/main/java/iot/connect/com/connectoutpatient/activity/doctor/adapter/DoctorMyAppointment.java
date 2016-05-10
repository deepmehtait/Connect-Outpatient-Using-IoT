package iot.connect.com.connectoutpatient.activity.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.ViewAppoinment;
import iot.connect.com.connectoutpatient.utils.AppBaseURL;

/**
 * Created by Deep on 05-May-16.
 */
public class DoctorMyAppointment extends BaseAdapter {

    ArrayList<ViewAppoinment> viewAppoinments = new ArrayList<ViewAppoinment>();
    Context context;
    private static LayoutInflater inflater = null;

    public DoctorMyAppointment(Context context, ArrayList<ViewAppoinment> mpld) {
        this.context = context;
        this.viewAppoinments = mpld;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewAppoinments.size();
    }

    @Override
    public ViewAppoinment getItem(int position) {
        // TODO Auto-generated method stub
        return viewAppoinments.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView patientName, Time, Date, location;
        ImageView patientImage;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = inflater.inflate(R.layout.appointment_list_view, null);
        holder.patientName = (TextView) row.findViewById(R.id.patientName);
        holder.patientName.setText(viewAppoinments.get(position).getPatientName());
        holder.Time = (TextView) row.findViewById(R.id.Time);
        holder.Time.setText("Time: " + viewAppoinments.get(position).getTime());
        holder.Date = (TextView) row.findViewById(R.id.Date);
        holder.Date.setText("Date: " + viewAppoinments.get(position).getDate().substring(0, 10));
        holder.patientImage = (ImageView) row.findViewById(R.id.patientImage);
        Picasso.with(row.getContext()).load(AppBaseURL.BaseURL + viewAppoinments.get(position).getPatientProfileImageURL()).error(R.drawable.noimg).into(holder.patientImage);
        holder.location = (TextView) row.findViewById(R.id.location);
        holder.location.setText("Hospital: " + viewAppoinments.get(position).getLocationName() +
                "\n" + viewAppoinments.get(position).getAddress()
                + ",\n"
                + viewAppoinments.get(position).getCity() + "," + viewAppoinments.get(position).getState() + "," + viewAppoinments.get(position).getZipcode());
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DoctorAppointmentEditDelete.class);
                i.putExtra("patientID", viewAppoinments.get(position).getPatientId());
                i.putExtra("patientName", viewAppoinments.get(position).getPatientName());
                i.putExtra("_id", viewAppoinments.get(position).get_id());
                i.putExtra("doctorID", viewAppoinments.get(position).getDoctorId());
                i.putExtra("patientURL", viewAppoinments.get(position).getPatientProfileImageURL());
                i.putExtra("date", viewAppoinments.get(position).getDate());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        return row;
    }

}
