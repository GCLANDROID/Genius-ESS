package com.genius.employee.adapter;

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
import com.genius.employee.fragment.ExperienceFragment;
import com.genius.employee.fragment.FamilyFragment;
import com.genius.employee.model.ExperienceModel;
import com.genius.employee.model.FamilyModel;
import com.genius.employee.utility.Util;

import java.util.ArrayList;

public class ExperienceAdapter extends  RecyclerView.Adapter<ExperienceAdapter.MyViewHolder> {
    ArrayList<ExperienceModel>itemList=new ArrayList();
    ExperienceFragment fragment;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.experience_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.tvOrg.setText(itemList.get(i).getCount()+". "+itemList.get(i).getOrganisationName());
        myViewHolder.tvFromDate.setText(Util.changeAnyDateFormat(itemList.get(i).getFromDate(),"dd-MM-yyyy","dd MMM,yyyy"));
        myViewHolder.tvToDate.setText(Util.changeAnyDateFormat(itemList.get(i).getTodate(),"dd-MM-yyyy","dd MMM,yyyy"));
        myViewHolder.tvExperience.setText(itemList.get(i).getExperience());
        myViewHolder.tvDesignation.setText(itemList.get(i).getDesignation());
        myViewHolder.tvJobLocation.setText(itemList.get(i).getJobLocation());


        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExperienceFragment)fragment).expEditPopUp(i);
            }
        });


        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExperienceFragment)fragment).deleteAlert(i);
            }
        });



    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrg,tvFromDate,tvToDate,tvDesignation,tvExperience,tvJobLocation;
        ImageView imgEdit,imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrg=(TextView)itemView.findViewById(R.id.tvOrg);
            tvFromDate=(TextView)itemView.findViewById(R.id.tvFromDate);
            tvToDate=(TextView)itemView.findViewById(R.id.tvToDate);
            tvDesignation=(TextView)itemView.findViewById(R.id.tvDesignation);
            imgEdit=(ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete=(ImageView)itemView.findViewById(R.id.imgDelete);
            tvExperience=(TextView) itemView.findViewById(R.id.tvExperience);
            tvJobLocation=(TextView) itemView.findViewById(R.id.tvJobLocation);
        }
    }

    public ExperienceAdapter(ArrayList<ExperienceModel> itemList, ExperienceFragment fragment) {
        this.itemList = itemList;
        this.fragment=fragment;
    }
}
