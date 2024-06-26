package com.genius.employee.adapter;

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
import com.genius.employee.model.AnsReportModule;

import java.util.ArrayList;


public class AnsReportAdapter extends RecyclerView.Adapter<AnsReportAdapter.MyViewHolder> {
    ArrayList<AnsReportModule>itemList=new ArrayList();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ans_report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvAns.setText(itemList.get(i).getAnswer());
        if (itemList.get(i).getRate().equals("1")){
            myViewHolder.llRate1.setVisibility(View.VISIBLE);
        }
        if (itemList.get(i).getRate().equals("2")){
            myViewHolder.llRate2.setVisibility(View.VISIBLE);

        }
        if (itemList.get(i).getRate().equals("3")){
            myViewHolder.llRate3.setVisibility(View.VISIBLE);

        }

        if (itemList.get(i).getRate().equals("4")){
            myViewHolder.llRate4.setVisibility(View.VISIBLE);

        }

        if (itemList.get(i).getRate().equals("5")){
            myViewHolder.llRate5.setVisibility(View.VISIBLE);

        }




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAns;
        LinearLayout llRate5,llRate4,llRate3,llRate2,llRate1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAns=(TextView)itemView.findViewById(R.id.tvAns);
            llRate1=(LinearLayout)itemView.findViewById(R.id.llRate1);
            llRate2=(LinearLayout)itemView.findViewById(R.id.llRate2);
            llRate3=(LinearLayout)itemView.findViewById(R.id.llRate3);
            llRate4=(LinearLayout)itemView.findViewById(R.id.llRate4);
            llRate5=(LinearLayout)itemView.findViewById(R.id.llRate5);


        }
    }

    public AnsReportAdapter(ArrayList<AnsReportModule> itemList) {
        this.itemList = itemList;
    }
}
