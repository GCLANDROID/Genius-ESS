package com.genius.employee.adapter;

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
import com.genius.employee.activity.AnsReportActivity;
import com.genius.employee.model.FeedBackReportModule;

import java.util.ArrayList;


public class FeedBackReportAdapter extends RecyclerView.Adapter<FeedBackReportAdapter.MyViewHolder> {
    ArrayList<FeedBackReportModule>itemList=new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feedback_report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvTime.setText(itemList.get(i).getTime());
        myViewHolder.tvClientName.setText(itemList.get(i).getClintName());
        myViewHolder.tvOfficename.setText(itemList.get(i).getClintOffName());
        myViewHolder.tvContactPerson.setText(itemList.get(i).getCpName());
        myViewHolder.tvDes.setText(itemList.get(i).getCpDes());
        myViewHolder.tvEmailId.setText(itemList.get(i).getCpEmail());
        myViewHolder.tvPhnNumber.setText(itemList.get(i).getCpMob());
        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        myViewHolder.llFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AnsReportActivity.class);
                intent.putExtra("grpId",itemList.get(i).getGrpAnswer());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.tvRemark.setText(itemList.get(i).getRemark());
        if (itemList.get(i).getRemark().equals("")){
            myViewHolder.llRemark.setVisibility(View.GONE);
        }else {
            myViewHolder.llRemark.setVisibility(View.VISIBLE);
        }






    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvClientName,tvTime,tvOfficename,tvContactPerson,tvDes,tvEmailId,tvPhnNumber,tvAddress,tvRemark;
        LinearLayout llClient,llDate,llFeedBack,llRemark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            llClient=(LinearLayout)itemView.findViewById(R.id.llClient);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvClientName=(TextView)itemView.findViewById(R.id.tvClientName);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvOfficename=(TextView)itemView.findViewById(R.id.tvOfficename);
            tvContactPerson=(TextView)itemView.findViewById(R.id.tvContactPerson);
            tvDes=(TextView)itemView.findViewById(R.id.tvDes);
            tvEmailId=(TextView)itemView.findViewById(R.id.tvEmailId);
            tvPhnNumber=(TextView)itemView.findViewById(R.id.tvPhnNumber);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvRemark=(TextView)itemView.findViewById(R.id.tvRemark);

            llDate=(LinearLayout)itemView.findViewById(R.id.llDate);
            llFeedBack=(LinearLayout)itemView.findViewById(R.id.llFeedBack);
            llRemark=(LinearLayout)itemView.findViewById(R.id.llRemark);



        }
    }

    public FeedBackReportAdapter(ArrayList<FeedBackReportModule> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
