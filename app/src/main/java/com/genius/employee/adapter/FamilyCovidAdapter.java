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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.FamilyMemeberCovidStatusActivity;
import com.genius.employee.model.EducationModel;
import com.genius.employee.model.FamilyCovidModel;

import java.util.ArrayList;

public class FamilyCovidAdapter extends RecyclerView.Adapter<FamilyCovidAdapter.MyViewHolder> {
    ArrayList<FamilyCovidModel>itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.family_view_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvRelationship.setText(itemList.get(i).getRealtionship());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FamilyMemeberCovidStatusActivity)context).familyDetailsShow(itemList.get(i).getCovidStatus(),itemList.get(i).getCovidDate(),itemList.get(i).getCovidFileName(),itemList.get(i).getCovidFile(),itemList.get(i).getVaccineStatus(),itemList.get(i).getVaccineDate(),itemList.get(i).getVaccineFileName(),itemList.get(i).getVaccineFile());
            }
        });












    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvRelationship;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRelationship=(TextView)itemView.findViewById(R.id.tvRelationship);


        }
    }

    public FamilyCovidAdapter(ArrayList<FamilyCovidModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
