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
import com.genius.employee.activity.clientcall.VirtualCallRegistrationActivity;
import com.genius.employee.activity.clientcall.model.AddSPOCModel;

import java.util.ArrayList;


public class PublicDocListAdapter extends RecyclerView.Adapter<PublicDocListAdapter.MyViewHolder> {

    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.public_doc_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {






    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);



        }
    }

    public PublicDocListAdapter( Context context) {

        this.context=context;
    }
}
