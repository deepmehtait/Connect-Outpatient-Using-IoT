package iot.connect.com.connectoutpatient.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import iot.connect.com.connectoutpatient.R;


/**
 * Created by Deep on 19-Apr-16.
 */public class DrawerAdapterPatient extends RecyclerView.Adapter<DrawerAdapterPatient.ViewHolder>{
    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;

    private List<String> rows;
    Context context;
    String userName,userImageUrl;

    public DrawerAdapterPatient(Context context, List<String> rows, String userName, String userImageUrl) {
        this.rows = rows;
        this.context=context;
        this.userName=userName;
        this.userImageUrl=userImageUrl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header_patient, parent, false);
            return new ViewHolder(view, viewType);
        } else if (viewType == ROW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            return new ViewHolder(view, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder.viewType==HEADER_TYPE){
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.headerImageView);
            holder.headerTextView.setText(userName);
        }
        if (holder.viewType == ROW_TYPE) {
            int location=position-1;
            if(location==0){
                String rowText = rows.get(location);
                holder.textView.setText(rowText);
                Picasso.with(context).load(R.drawable.ic_dashboard_black_24dp).into(holder.imageView);

            }else if(location==1){

                String rowText = rows.get(location);
                holder.textView.setText(rowText);
                Picasso.with(context).load(R.drawable.ic_settings_black_24dp).into(holder.imageView);

            }
        }
    }

    @Override
    public int getItemCount() {
        return rows.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        return ROW_TYPE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected int viewType;

         ImageView imageView,headerImageView;
         TextView textView,headerTextView;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;
            if(viewType== HEADER_TYPE){
                headerImageView=(ImageView)itemView.findViewById(R.id.drawer_header_image);
                headerTextView=(TextView)itemView.findViewById(R.id.drawer_header_text);
            }

            if (viewType == ROW_TYPE) {

                imageView=(ImageView)itemView.findViewById(R.id.drawer_row_icon);
                textView=(TextView)itemView.findViewById(R.id.drawer_row_text);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition()-1;
                        if(position==0){
                            Intent i=new Intent(itemView.getContext(),PatientDashboardActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            itemView.getContext().startActivity(i);
                        }else if(position==1){
                            Intent i=new Intent(itemView.getContext(),PatientSettingsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            itemView.getContext().startActivity(i);
                        }
                        //Toast.makeText(itemView.getContext(),"post-"+position,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
