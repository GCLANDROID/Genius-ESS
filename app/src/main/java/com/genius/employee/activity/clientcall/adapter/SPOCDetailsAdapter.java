package com.genius.employee.activity.clientcall.adapter;

//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.PhysicalCallRegActivity;
import com.genius.employee.activity.clientcall.model.AddSPOCModel;
import com.genius.employee.activity.clientcall.model.SPOCDetailsModel;

import java.util.ArrayList;


public class SPOCDetailsAdapter extends RecyclerView.Adapter<SPOCDetailsAdapter.MyViewHolder> {
    ArrayList<SPOCDetailsModel>itemList=new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.added_spoc_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

      myViewHolder.tvSPOCName.setText(itemList.get(i).getSpocName());

      myViewHolder.llViewDetails.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             // ((PhysicalCallRegActivity)context).showSPOCDetails(i);
          }
      });

      myViewHolder.imgCross.setVisibility(View.GONE);




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSPOCName;
        LinearLayout llViewDetails;
        ImageView imgCross;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSPOCName=(TextView)itemView.findViewById(R.id.tvSPOCName);
            llViewDetails=(LinearLayout)itemView.findViewById(R.id.llViewDetails);
            imgCross=(ImageView)itemView.findViewById(R.id.imgCross);


        }
    }

    public SPOCDetailsAdapter(ArrayList<SPOCDetailsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
