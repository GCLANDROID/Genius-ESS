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
import com.genius.employee.model.FamilyModel;
import com.genius.employee.model.LeaveModel;

import java.util.ArrayList;

public class FamilyAdapter extends  RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {
    ArrayList<FamilyModel>familyList=new ArrayList();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.family_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

       myViewHolder.tvName.setText(familyList.get(i).getName());
       myViewHolder.tvRealation.setText(familyList.get(i).getRealation());

       if (familyList.get(i).getInformation().equals("null")||familyList.get(i).getInformation().equals("")||familyList.get(i).getInformation().equals("0")){
           myViewHolder.tvInformation.setText("N/A");
       }else {
           myViewHolder.tvInformation.setText(familyList.get(i).getInformation());
       }
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvRealation,tvInformation;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvRealation=(TextView)itemView.findViewById(R.id.tvRealation);
            tvInformation=(TextView)itemView.findViewById(R.id.tvInformation);
        }
    }

    public FamilyAdapter(ArrayList<FamilyModel> familyList) {
        this.familyList = familyList;
    }
}
