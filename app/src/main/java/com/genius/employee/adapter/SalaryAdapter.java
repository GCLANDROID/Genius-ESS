package com.genius.employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.PayslipActivity;
import com.genius.employee.activity.WebPayslipActivity;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.SalaryModel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.MyViewHolder> {
    ArrayList<SalaryModel> salryList = new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salary_raw, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        myViewHolder.tvMonth.setText(salryList.get(i).getMonth());
        myViewHolder.tvYear.setText(salryList.get(i).getYear());
        myViewHolder.tvMGross.setText(salryList.get(i).getMonthlyGross());
        myViewHolder.tvAGross.setText(salryList.get(i).getActualGross());
        myViewHolder.tvDeduction.setText(salryList.get(i).getDeduction());
        myViewHolder.tvMonthlyNet.setText(salryList.get(i).getMontlyNet());
        myViewHolder.tvEsi.setText(salryList.get(i).getEsiDeduction());
        myViewHolder.tvPF.setText(salryList.get(i).getPfDeduction());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, WebPayslipActivity.class);
                intent.putExtra("imageurl",salryList.get(i).getWeburl());
                intent.putExtra("month",salryList.get(i).getMonth());
                intent.putExtra("year",salryList.get(i).getYear());
                intent.putExtra("flag","PaySlip");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return salryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMonth, tvYear, tvMGross, tvAGross, tvDeduction, tvMonthlyNet, tvEsi, tvPF;
        LinearLayout llWEB;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
            tvMGross = (TextView) itemView.findViewById(R.id.tvMGross);
            tvAGross = (TextView) itemView.findViewById(R.id.tvAGross);
            tvDeduction = (TextView) itemView.findViewById(R.id.tvDeduction);
            tvMonthlyNet = (TextView) itemView.findViewById(R.id.tvMonthlyNet);
            tvEsi = (TextView) itemView.findViewById(R.id.tvEsi);
            tvPF = (TextView) itemView.findViewById(R.id.tvPF);
            llWEB = (LinearLayout) itemView.findViewById(R.id.llWEB);


        }
    }

    public SalaryAdapter(ArrayList<SalaryModel> salryList, Context context) {
        this.salryList = salryList;
        this.context = context;
    }
}
