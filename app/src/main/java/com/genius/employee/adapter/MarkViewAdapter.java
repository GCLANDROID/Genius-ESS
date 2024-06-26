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

import com.genius.employee.R;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.MarkInViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarkViewAdapter extends RecyclerView.Adapter<MarkViewAdapter.MyViewHolder> {
    ArrayList<MarkInViewModel>itemList=new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.markinview_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvTime.setText(itemList.get(i).getTime());

        if (itemList.get(i).getAccessForm().equals("M")){


            myViewHolder.tvAdress.setText(itemList.get(i).getLocation());
        }else {

            myViewHolder.tvAdress.setText("Punched from Web Portal");
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAdress,tvTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAdress=(TextView)itemView.findViewById(R.id.tvAdress);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);




        }
    }

    public MarkViewAdapter(ArrayList<MarkInViewModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

    }
}
