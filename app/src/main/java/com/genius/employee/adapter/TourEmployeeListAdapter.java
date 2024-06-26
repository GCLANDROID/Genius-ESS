package com.genius.employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.TrackingReportActivity;
import com.genius.employee.activity.VisitDetailsActivity;
import com.genius.employee.model.EmployeeListModel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TourEmployeeListAdapter extends RecyclerView.Adapter<TourEmployeeListAdapter.MyViewHolder> {
    ArrayList<EmployeeListModel>itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emp_list_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {

      myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
      myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(context, TrackingReportActivity.class);
              Log.d("empId",itemList.get(i).getEmpId());
              intent.putExtra("empId",itemList.get(i).getEmpId());
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);

          }
      });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmpName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);

        }
    }

    public TourEmployeeListAdapter(ArrayList<EmployeeListModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
