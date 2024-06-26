package com.genius.employee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.genius.employee.model.MarkInViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeamMarkReportAdapter extends RecyclerView.Adapter<TeamMarkReportAdapter.MyViewHolder> {
    ArrayList<MarkInViewModel>itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_report_markinreport_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {

      myViewHolder.tvAddress.setText(itemList.get(i).getLocation());
      myViewHolder.tvIn.setText(itemList.get(i).getTime());

      if (itemList.get(i).getFlag().equals("0")){
          myViewHolder.tvAccess.setVisibility(View.VISIBLE);
          if (itemList.get(i).getAccessForm().equals("M")){
              myViewHolder.tvAccess.setText("Mobile");
              Picasso.with(context).load(itemList.get(i).getImageUrl()).into(myViewHolder.imgPic);

          }else {
              myViewHolder.tvAddress.setText("Web");
              myViewHolder.tvAccess.setText("Web");
              Picasso.with(context).load(R.drawable.webicon).into(myViewHolder.imgPic);
          }
      }else {
          myViewHolder.tvAccess.setVisibility(View.GONE);
      }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView tvIn,tvAddress,tvAccess;
         ImageView imgPic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvIn=(TextView)itemView.findViewById(R.id.tvIn);
            tvAccess=(TextView)itemView.findViewById(R.id.tvAccess);
            imgPic=(ImageView)itemView.findViewById(R.id.imgPic);


        }
    }

    public TeamMarkReportAdapter(ArrayList<MarkInViewModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
