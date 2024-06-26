package com.genius.employee.adapter;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class SeconDoseMemberAdapter extends RecyclerView.Adapter<SeconDoseMemberAdapter.EducationCustomHolder> {
   JSONArray mEducationArrayList;
    public Context ec;

    public SeconDoseMemberAdapter(JSONArray mEducationArrayList, Context ec) {
        this.mEducationArrayList = mEducationArrayList;
        this.ec = ec;
    }

    @NonNull
    @Override
    public EducationCustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(parent.getContext()).inflate(R.layout.secondose_member_raw,parent,false);
        EducationCustomHolder obj=new EducationCustomHolder(row);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull EducationCustomHolder holder, int position) {
        final JSONObject jsonObject = mEducationArrayList.optJSONObject(position);
        holder.tvRelationship.setText(jsonObject.optString("realation"));
        holder.tvVaccineName.setText(jsonObject.optString("vaccine"));
        holder.tvDate.setText(jsonObject.optString("date"));


    }

    @Override
    public int getItemCount() {
        return mEducationArrayList.length();
    }

    public class EducationCustomHolder extends RecyclerView.ViewHolder {
        TextView tvRelationship,tvVaccineName,tvDate;
        public EducationCustomHolder(@NonNull View itemView) {
            super(itemView);
            tvRelationship=itemView.findViewById(R.id.tvRelationship);
            tvVaccineName=itemView.findViewById(R.id.tvVaccineName);
            tvDate=itemView.findViewById(R.id.tvDate);

        }
    }
}
