package com.genius.employee.adapter;

/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.model.VisitingLocationModel;

import java.util.ArrayList;


public class VisitingLocationAdapter extends RecyclerView.Adapter<VisitingLocationAdapter.MyViewHolder> {
    ArrayList<VisitingLocationModel>itemList=new ArrayList();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visiting_loaction_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvLocation.setText(itemList.get(i).getLocation());
        myViewHolder.tvTime.setText(itemList.get(i).getTime()+"-"+"Check-In");


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation,tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);


        }
    }

    public VisitingLocationAdapter(ArrayList<VisitingLocationModel> itemList) {
        this.itemList = itemList;
    }
}
