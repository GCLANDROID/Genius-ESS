package com.genius.employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.model.MarkInViewModel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarkReportAdapter extends RecyclerView.Adapter<MarkReportAdapter.MyViewHolder> {
    ArrayList<MarkInViewModel>itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.markinreport_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {

      myViewHolder.tvDate.setText(itemList.get(i).getDate());
      myViewHolder.tvIn.setText(itemList.get(i).getTime());
      myViewHolder.tvOut.setText(itemList.get(i).getPunchOutTime());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView tvIn,tvDate,tvOut,tvAccess;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOut=(TextView)itemView.findViewById(R.id.tvOut);
            tvIn=(TextView)itemView.findViewById(R.id.tvIn);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvAccess=(TextView)itemView.findViewById(R.id.tvAccess);


        }
    }

    public MarkReportAdapter(ArrayList<MarkInViewModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
