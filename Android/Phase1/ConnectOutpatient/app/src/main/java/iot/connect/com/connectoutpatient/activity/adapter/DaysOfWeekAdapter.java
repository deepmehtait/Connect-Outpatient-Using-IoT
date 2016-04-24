package iot.connect.com.connectoutpatient.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import iot.connect.com.connectoutpatient.R;
import iot.connect.com.connectoutpatient.modals.DaysOfWeekData;
import iot.connect.com.connectoutpatient.modals.dayAndMedication;

/**
 * Created by Deep on 23-Apr-16.
 */
public class DaysOfWeekAdapter extends BaseAdapter {

    ArrayList<dayAndMedication> daysOfWeekDatas = new ArrayList<dayAndMedication>();
    Context context;
    private static LayoutInflater inflater = null;

    public DaysOfWeekAdapter(Context context, ArrayList<dayAndMedication> daysOfWeekDatas) {
        this.context = context;
        this.daysOfWeekDatas = daysOfWeekDatas;
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
        TextView dayName, morningMedication, noonMedication, eveningMedication;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View row;
        row = inflater.inflate(R.layout.days_list_view, null);

        holder.dayName = (TextView) row.findViewById(R.id.daysofWeek);
        holder.dayName.setText(daysOfWeekDatas.get(position).getDay());
        holder.morningMedication = (TextView) row.findViewById(R.id.morning);
        holder.morningMedication.setText("Morning: " + daysOfWeekDatas.get(position).getMorning());
        holder.noonMedication = (TextView) row.findViewById(R.id.noon);
        holder.noonMedication.setText("Noon: " + daysOfWeekDatas.get(position).getNoon());
        holder.eveningMedication = (TextView) row.findViewById(R.id.evening);
        holder.eveningMedication.setText("Evening: " + daysOfWeekDatas.get(position).getEvening());
        return row;
    }


}
