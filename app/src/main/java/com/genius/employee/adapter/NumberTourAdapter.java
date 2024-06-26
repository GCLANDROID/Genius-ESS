package com.genius.employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.APiHitActivity;
import com.genius.employee.activity.DailyReportActivity;
import com.genius.employee.activity.NumberVisitActivity;
import com.genius.employee.activity.TourViewReportActivity;
import com.genius.employee.model.NumberTourModel;

import java.util.ArrayList;


public class NumberTourAdapter extends  RecyclerView.Adapter<NumberTourAdapter.MyViewHolder> {
    ArrayList<NumberTourModel> itemList = new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.number_tour_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvNumber.setText(itemList.get(i).getNumber());

        myViewHolder.llNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DailyReportActivity.class);
                intent.putExtra("attdate", itemList.get(i).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.llTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TourViewReportActivity.class);
                intent.putExtra("attdate", itemList.get(i).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        myViewHolder.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, APiHitActivity.class);
                intent.putExtra("attdate", itemList.get(i).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        double d = ((NumberVisitActivity) context).distance(i);
        String sDisttance = String.format("%.3f", d);
        myViewHolder.tvDistance.setText(sDisttance + "KM");


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvNumber, tvDistance;
        LinearLayout llMap, llTour, llNormal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            llNormal = (LinearLayout) itemView.findViewById(R.id.llNormal);
            llMap = (LinearLayout) itemView.findViewById(R.id.llMap);
            llTour = (LinearLayout) itemView.findViewById(R.id.llTour);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);

        }
    }

    public NumberTourAdapter(ArrayList<NumberTourModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
