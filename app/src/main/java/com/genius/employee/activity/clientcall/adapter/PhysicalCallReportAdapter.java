package com.genius.employee.activity.clientcall.adapter;

//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.PhyClinetCallReportDetailsActivity;
import com.genius.employee.activity.clientcall.model.PhysicalCallReportModel;

import java.util.ArrayList;


public class PhysicalCallReportAdapter extends RecyclerView.Adapter<PhysicalCallReportAdapter.MyViewHolder> {
    ArrayList<PhysicalCallReportModel>itemList=new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.physical_call_details_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

       myViewHolder.tvClientName.setText(itemList.get(i).getClientName());
        myViewHolder.tvDistance.setText("Total Distance: "+itemList.get(i).getDistance());
        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        if (itemList.get(i).getForeignTrip()==1){
            myViewHolder.imgFlight.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgFlight.setVisibility(View.GONE);
        }

        myViewHolder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PhyClinetCallReportDetailsActivity.class);
                intent.putExtra("returntrip",itemList.get(i).getForeignTrip());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvClientName,tvDate,tvDistance,tvViewDetails;
        ImageView imgFlight;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName=(TextView)itemView.findViewById(R.id.tvClientName);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvDistance=(TextView)itemView.findViewById(R.id.tvDistance);
            tvViewDetails=(TextView)itemView.findViewById(R.id.tvViewDetails);

            imgFlight=itemView.findViewById(R.id.imgFlight);



        }
    }

    public PhysicalCallReportAdapter(ArrayList<PhysicalCallReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
