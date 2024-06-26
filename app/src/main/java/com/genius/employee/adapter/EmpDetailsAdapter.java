package com.genius.employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.FeedBackActivity;
import com.genius.employee.activity.WFHAddressDetailsActivity;
import com.genius.employee.activity.WFHEmpListActivity;
import com.genius.employee.activity.WFHTeamReportActivity;
import com.genius.employee.model.EmpDetailsModel;
import com.genius.employee.model.MarkInViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmpDetailsAdapter extends RecyclerView.Adapter<EmpDetailsAdapter.MyViewHolder> {
    ArrayList<EmpDetailsModel>itemList=new ArrayList();
    Context context;
    String showDate;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emp_raw_details,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        myViewHolder.tvDept.setText(itemList.get(i).getDept());
        myViewHolder.tvLocation.setText(itemList.get(i).getLocation());
        myViewHolder.tvPhn.setText(itemList.get(i).getMobile());
        myViewHolder.tvEmpID.setText(itemList.get(i).getEmpId());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WFHTeamReportActivity.class);
                intent.putExtra("empId",itemList.get(i).getEmpId());
                intent.putExtra("empName",itemList.get(i).getEmpName());
                intent.putExtra("SecureID",itemList.get(i).getSecureID());
                intent.putExtra("dDate",showDate);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.tvApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, WFHAddressDetailsActivity.class);
                intent.putExtra("empID",itemList.get(i).getEmpId());
                intent.putExtra("empName",itemList.get(i).getEmpName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });





        if (itemList.get(i).isStatus()){
            myViewHolder.tvStatus.setText("Marked");
            myViewHolder.tvStatus.setTextColor(Color.parseColor("#02D64E"));
        }else {
            myViewHolder.tvStatus.setText("Not Mark");
            myViewHolder.tvStatus.setTextColor(Color.parseColor("#cc0512"));
        }

        myViewHolder.lnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+itemList.get(i).getMobile()));
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvEmpName,tvDept,tvLocation,tvStatus,tvPhn,tvEmpID,tvApproval;

        LinearLayout lnPhone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvEmpID=(TextView)itemView.findViewById(R.id.tvEmpID);
            tvDept=(TextView)itemView.findViewById(R.id.tvDept);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            tvPhn=(TextView)itemView.findViewById(R.id.tvPhn);

            tvApproval=(TextView)itemView.findViewById(R.id.tvApproval);

            lnPhone=(LinearLayout)itemView.findViewById(R.id.lnPhone);


        }
    }

    public EmpDetailsAdapter(ArrayList<EmpDetailsModel> itemList, Context context,String showDate) {
        this.itemList = itemList;
        this.context = context;
        this.showDate=showDate;
    }

    public void updateList(ArrayList<EmpDetailsModel> list){
        itemList = list;
        notifyDataSetChanged();
    }
}
