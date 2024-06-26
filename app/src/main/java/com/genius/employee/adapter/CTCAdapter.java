package com.genius.employee.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.employee.R;
import com.genius.employee.activity.CuurentCTCActivity;
import com.genius.employee.model.CTCModel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CTCAdapter extends RecyclerView.Adapter<CTCAdapter.MyViewHolder> {
    ArrayList<CTCModel> CTCList =new ArrayList();
    Context context;
    String outimage="";
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ctc_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDate.setText(CTCList.get(i).getDate());
        myViewHolder.tvMonthlyGross.setText(CTCList.get(i).getMonthlyGross());
        myViewHolder.tvYearlyGross.setText(CTCList.get(i).getYearlyGross());
        myViewHolder.tvDesignartion.setText(CTCList.get(i).getDesignation());
        myViewHolder.tvMonthlyCTC.setText(CTCList.get(i).getMonthlyCTC());
        myViewHolder.tvYearlyCTC.setText(CTCList.get(i).getYearlyCTC());





    }

    @Override
    public int getItemCount() {
        return CTCList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate,tvMonthlyGross,tvYearlyGross,tvGrowth,tvDesignartion,tvMonthlyCTC,tvYearlyCTC;
        LinearLayout llWEB;
        ImageView imgGlobe;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvMonthlyGross=(TextView)itemView.findViewById(R.id.tvMonthlyGross);
            tvYearlyGross=(TextView)itemView.findViewById(R.id.tvYearlyGross);
            tvGrowth=(TextView)itemView.findViewById(R.id.tvGrowth);
            llWEB=(LinearLayout)itemView.findViewById(R.id.llWEB);
            imgGlobe=(ImageView)itemView.findViewById(R.id.imgGlobe);
            tvDesignartion=(TextView)itemView.findViewById(R.id.tvDesignartion);
            tvMonthlyCTC=(TextView)itemView.findViewById(R.id.tvMonthlyCTC);
            tvYearlyCTC=(TextView)itemView.findViewById(R.id.tvYearlyCTC);




        }
    }

    public CTCAdapter(ArrayList<CTCModel> CTCList, Context context) {
        this.CTCList = CTCList;
        this.context = context;
    }
}
