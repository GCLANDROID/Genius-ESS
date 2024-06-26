package com.genius.employee.adapter;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.DailyReportActivity;
import com.genius.employee.model.DailyReportModel;

import java.util.ArrayList;

public class DailyActivityReportAdapter extends RecyclerView.Adapter<DailyActivityReportAdapter.MyViewHolder> {
    ArrayList<DailyReportModel>itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.daily_report_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvDate.setText(itemList.get(i).getVisitingDate());
        myViewHolder.tvClientName.setText(itemList.get(i).getVisitingClientName());
        myViewHolder.tvInLocation.setText(itemList.get(i).getInLocation());
        myViewHolder.tvInTime.setText(itemList.get(i).getInTime());
        myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());
        myViewHolder.tvOutLocation.setText(itemList.get(i).getOutLocation());
        myViewHolder.tvRemarks.setText(itemList.get(i).getRemarks());
        myViewHolder.tvRegTime.setText(itemList.get(i).getRegTime());
        myViewHolder.tvContactPerson.setText(itemList.get(i).getContactperson());
        if (itemList.get(i).getStatus().equals("OPEN")){
            myViewHolder.imgRed.setVisibility(View.GONE);
            myViewHolder.imgGreen.setVisibility(View.GONE);
        }else {
            myViewHolder.imgRed.setVisibility(View.GONE);
            myViewHolder.imgGreen.setVisibility(View.GONE);
        }

        myViewHolder.tvPurpose.setText(itemList.get(i).getVisitingPurpose());
        myViewHolder.tvContactNumber.setText(itemList.get(i).getContactnumber());
        myViewHolder.tvEmailId.setText(itemList.get(i).getEmail());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvClientName,tvInTime,tvInLocation,tvOutTime,tvOutLocation,tvRemarks,tvContactPerson,tvRegTime,tvTotalDistance,tvEmailId,tvContactNumber,tvPurpose;
        ImageView imgGreen,imgRed;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvClientName=(TextView)itemView.findViewById(R.id.tvClientName);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvInLocation=(TextView)itemView.findViewById(R.id.tvInLocation);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvOutLocation=(TextView)itemView.findViewById(R.id.tvOutLocation);
            tvRemarks=(TextView)itemView.findViewById(R.id.tvRemarks);
            tvContactPerson=(TextView)itemView.findViewById(R.id.tvContactPerson);
            tvRegTime=(TextView)itemView.findViewById(R.id.tvRegTime);
            tvTotalDistance=(TextView)itemView.findViewById(R.id.tvTotalDistance);
            tvEmailId=(TextView)itemView.findViewById(R.id.tvEmailId);
            tvContactNumber=(TextView)itemView.findViewById(R.id.tvContactNumber);
            tvPurpose=(TextView)itemView.findViewById(R.id.tvPurpose);

            imgGreen=(ImageView)itemView.findViewById(R.id.imgGreen);
            imgRed=(ImageView)itemView.findViewById(R.id.imgRed);

        }
    }

    public DailyActivityReportAdapter(ArrayList<DailyReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
