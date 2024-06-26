package com.genius.employee.adapter;

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
import com.genius.employee.model.LeaveModel;

import java.util.ArrayList;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {
    ArrayList<LeaveModel>leaveList=new ArrayList();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvLeaveCount.setText(leaveList.get(i).getLeaveCount());
        myViewHolder.tvLeaveName.setText(leaveList.get(i).getLeaveName());


    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvLeaveName,tvLeaveCount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveName=(TextView)itemView.findViewById(R.id.tvLeaveName);
            tvLeaveCount=(TextView)itemView.findViewById(R.id.tvLeaveCount);
        }
    }

    public LeaveAdapter(ArrayList<LeaveModel> leaveList) {
        this.leaveList = leaveList;
    }
}
