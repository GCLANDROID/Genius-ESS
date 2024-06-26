package com.genius.employee.adapter;

//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.model.ComponentModel;
import com.genius.employee.model.DeductionModel;

import java.util.ArrayList;

public class DeductionAdapter extends RecyclerView.Adapter<DeductionAdapter.MyViewHolder> {
    ArrayList<DeductionModel>itemList=new ArrayList();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deduction_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

       myViewHolder.tvCompName.setText(itemList.get(i).getCompName());
       myViewHolder.tvAmt.setText(itemList.get(i).getAmt());



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompName,tvAmt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCompName=(TextView)itemView.findViewById(R.id.tvCompName);
            tvAmt=(TextView)itemView.findViewById(R.id.tvAmt);

        }
    }

    public DeductionAdapter(ArrayList<DeductionModel> itemList) {
        this.itemList = itemList;
    }
}
