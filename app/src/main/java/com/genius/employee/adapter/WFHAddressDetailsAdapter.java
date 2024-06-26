package com.genius.employee.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.WFHAddressDetailsActivity;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.WFHAddressDetailsModel;

import java.util.ArrayList;
import java.util.Locale;

public class WFHAddressDetailsAdapter extends RecyclerView.Adapter<WFHAddressDetailsAdapter.MyViewHolder> {
    ArrayList<WFHAddressDetailsModel>itemList=new ArrayList();
    Context context;
    String uri;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wfh_address_row,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        if (itemList.get(i).getAddressType().equals("1")){
            myViewHolder.tvAddressType.setText("Present Address");
        }else if (itemList.get(i).getAddressType().equals("2")){
            myViewHolder.tvAddressType.setText("Permanent Address");

        }else if (itemList.get(i).getAddressType().equals("3")){
            myViewHolder.tvAddressType.setText("Both Address");

        }


        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        myViewHolder.tvCoordinates.setText(itemList.get(i).getLatitude()+" , "+itemList.get(i).getLongitude());

        myViewHolder.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemList.get(i).getAddressType().equals("1")){
                    uri = "http://maps.google.com/maps?q=loc:" + Double.parseDouble(itemList.get(i).getLatitude()) + "," + Double.parseDouble(itemList.get(i).getLongitude()) + " (" + "Present Address Details" + ")";

                }else if (itemList.get(i).getAddressType().equals("2")){
                    uri = "http://maps.google.com/maps?q=loc:" + Double.parseDouble(itemList.get(i).getLatitude()) + "," + Double.parseDouble(itemList.get(i).getLongitude()) + " (" + "Permanent Address Details" + ")";

                }else if (itemList.get(i).getAddressType().equals("3")){
                    uri = "http://maps.google.com/maps?q=loc:" + Double.parseDouble(itemList.get(i).getLatitude()) + "," + Double.parseDouble(itemList.get(i).getLongitude()) + " (" + "Both Present & Permanent Address Details" + ")";

                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.llApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WFHAddressDetailsActivity)context).WFHAddressApproval("1",itemList.get(i).getAddressType());
            }
        });

        myViewHolder.llRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WFHAddressDetailsActivity)context).rejectDialog("0",itemList.get(i).getAddressType());
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressType,tvCoordinates,tvAddress;
        LinearLayout llMap,llApprove,llRejected;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressType=(TextView) itemView.findViewById(R.id.tvAddressType);
            tvCoordinates=(TextView) itemView.findViewById(R.id.tvCoordinates);
            tvAddress=(TextView) itemView.findViewById(R.id.tvAddress);

            llMap=(LinearLayout) itemView.findViewById(R.id.llMap);
            llApprove=(LinearLayout) itemView.findViewById(R.id.llApprove);
            llRejected=(LinearLayout) itemView.findViewById(R.id.llRejected);

        }
    }

    public WFHAddressDetailsAdapter(ArrayList<WFHAddressDetailsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
