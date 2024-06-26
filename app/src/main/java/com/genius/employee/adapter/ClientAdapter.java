package com.genius.employee.adapter;

import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.ClintOfficeActivity;
import com.genius.employee.model.ClientModule;

import java.util.ArrayList;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.MyViewHolder> {
    ArrayList<ClientModule>itemList=new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.client_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvClientName.setText(itemList.get(i).getClientName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clintid=itemList.get(i).getId();
                String name=itemList.get(i).getClientName();
                Intent intent = new Intent(context, ClintOfficeActivity.class);
                intent.putExtra("clientname",name);
                intent.putExtra("clientid",clintid);
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
        TextView tvClientName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClientName=(TextView)itemView.findViewById(R.id.tvClientName);
        }
    }

    public ClientAdapter(ArrayList<ClientModule> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public void updateList(ArrayList<ClientModule> list){
        itemList = list;
        notifyDataSetChanged();
    }
}
