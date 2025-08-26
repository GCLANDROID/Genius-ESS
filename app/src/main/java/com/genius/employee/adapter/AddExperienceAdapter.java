package com.genius.employee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.genius.employee.R;
import com.genius.employee.activity.ExperienceActivity;
import com.genius.employee.model.AddExperienceModel;

import java.util.ArrayList;

public class AddExperienceAdapter extends RecyclerView.Adapter<AddExperienceAdapter.MyViewHolder>{
    Context context;
    ArrayList<AddExperienceModel> experienceList;

    public AddExperienceAdapter(Context context, ArrayList<AddExperienceModel> experienceList) {
        this.context = context;
        this.experienceList = experienceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.experience_view_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtCompanyName.setText(experienceList.get(position).getOrganizationName());
        holder.txtFromDate.setText(experienceList.get(position).getFromDate());
        holder.txtToDate.setText(experienceList.get(position).getToDate());
        holder.txtDesignation.setText(experienceList.get(position).getDesignation());
        holder.txtExperience.setText(experienceList.get(position).getExperience() +(experienceList.get(position).getExperience() == 1 ? " month" : " months"));
        holder.txtCTC.setText(experienceList.get(position).getCtc());
        holder.txtLocation.setText(experienceList.get(position).getLocation());
        holder.imgPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExperienceActivity) context).openAddExperiencePopup(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return experienceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtCompanyName,txtFromDate,txtToDate,txtDesignation,txtExperience,txtCTC,txtLocation;
        ImageView imgPencil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCompanyName = itemView.findViewById(R.id.txtCompanyName);
            txtFromDate = itemView.findViewById(R.id.txtFromDate);
            txtToDate = itemView.findViewById(R.id.txtToDate);
            txtDesignation = itemView.findViewById(R.id.txtDesignation);
            txtExperience = itemView.findViewById(R.id.txtExperience);
            txtCTC = itemView.findViewById(R.id.txtCTC);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            imgPencil = itemView.findViewById(R.id.imgPencil);
        }
    }
}
