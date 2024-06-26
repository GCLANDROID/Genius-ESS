package com.genius.employee.adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.FamilyMemeberCovidStatusActivity;
import com.genius.employee.model.FamilyCovidModel;

import java.util.ArrayList;

public class FamilyCovidReportAdapter extends RecyclerView.Adapter<FamilyCovidReportAdapter.MyViewHolder> {
    ArrayList<FamilyCovidModel>itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.family_member_covid_report,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvRelationship.setText(itemList.get(i).getRealtionship());
        myViewHolder.tvCovidStatus.setText(itemList.get(i).getCovidStatus());
        myViewHolder.tvCovidDate.setText(itemList.get(i).getCovidDate());
        myViewHolder.tvcovidDocName.setText(itemList.get(i).getCovidFileName());
        myViewHolder.tvVaccineDoc.setText(itemList.get(i).getVaccineFileName());
        myViewHolder.tvScndVaccineDoc.setText(itemList.get(i).getScndDoseFileName());
        myViewHolder.tvBoosterDose.setText(itemList.get(i).getBoosterDose());

        myViewHolder.tvcovidDocName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemList.get(i).getCovidFile().equals("")){
                    Uri uri = Uri.parse(itemList.get(i).getCovidFile()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            }
        });

        myViewHolder.tvVaccineDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemList.get(i).getVaccineFile().equals("")){
                    Uri uri = Uri.parse(itemList.get(i).getVaccineFile()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            }
        });

        myViewHolder.tvScndVaccineDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemList.get(i).getScndDoseFile().equals("")){
                    Uri uri = Uri.parse(itemList.get(i).getScndDoseFile()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            }
        });


        myViewHolder.tvBoosterDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemList.get(i).getBoosterDose().equals("")){
                    Uri uri = Uri.parse(itemList.get(i).getBoosterFile()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRelationship,tvCovidStatus,tvCovidDate,tvcovidDocName,tvVaccineDoc,tvScndVaccineDoc,tvBoosterDose;
        LinearLayout ll1stDose,ll2ndDose,llCovidFile,llBoosterDose;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRelationship=(TextView)itemView.findViewById(R.id.tvRelationship);
            tvCovidStatus=(TextView)itemView.findViewById(R.id.tvCovidStatus);
            tvCovidDate=(TextView)itemView.findViewById(R.id.tvCovidDate);
            tvcovidDocName=(TextView)itemView.findViewById(R.id.tvcovidDocName);
            tvVaccineDoc=(TextView)itemView.findViewById(R.id.tvVaccineDoc);
            tvScndVaccineDoc=(TextView)itemView.findViewById(R.id.tvScndVaccineDoc);
            ll2ndDose=(LinearLayout)itemView.findViewById(R.id.ll2ndDose);
            ll1stDose=(LinearLayout)itemView.findViewById(R.id.ll1stDose);
            llCovidFile=(LinearLayout)itemView.findViewById(R.id.llCovidFile);
            llBoosterDose=(LinearLayout)itemView.findViewById(R.id.llBoosterDose);
            tvBoosterDose=(TextView)itemView.findViewById(R.id.tvBoosterDose);



        }
    }

    public FamilyCovidReportAdapter(ArrayList<FamilyCovidModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
