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
import com.genius.employee.model.FamilyModel;

import java.util.ArrayList;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.MyViewHolder> {
    ArrayList<ComponentModel>itemList=new ArrayList();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

       myViewHolder.tvCompName.setText(itemList.get(i).getComponentName());
       myViewHolder.tvActualAmt.setText(itemList.get(i).getActualAmt());
        myViewHolder.tvGrossAmt.setText(itemList.get(i).getGrossAmt());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompName,tvGrossAmt,tvActualAmt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCompName=(TextView)itemView.findViewById(R.id.tvCompName);
            tvGrossAmt=(TextView)itemView.findViewById(R.id.tvGrossAmt);
            tvActualAmt=(TextView)itemView.findViewById(R.id.tvActualAmt);
        }
    }

    public ComponentAdapter(ArrayList<ComponentModel> itemList) {
        this.itemList = itemList;
    }
}
