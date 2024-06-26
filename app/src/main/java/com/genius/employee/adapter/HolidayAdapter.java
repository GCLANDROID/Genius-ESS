package com.genius.employee.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.model.HolidayModel;

import java.util.ArrayList;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {
    ArrayList<HolidayModel>itemList=new ArrayList();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holiday_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvCount.setText(""+itemList.get(i).getCount());
        myViewHolder.tvName.setText(itemList.get(i).getName());
        myViewHolder.tvDay.setText(itemList.get(i).getDay());
        if (itemList.get(i).getIsColor().equalsIgnoreCase("1")){
            myViewHolder.llItem.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E31111")));
        }else {
            myViewHolder.llItem.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#006CFF")));
        }




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        TextView tvName,tvDate,tvDay,tvCount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvDay=(TextView)itemView.findViewById(R.id.tvDay);
            llItem=(LinearLayout)itemView.findViewById(R.id.llItem);
            tvCount=(TextView) itemView.findViewById(R.id.tvCount);

        }
    }

    public HolidayAdapter(ArrayList<HolidayModel> itemList) {
        this.itemList = itemList;
    }
}
