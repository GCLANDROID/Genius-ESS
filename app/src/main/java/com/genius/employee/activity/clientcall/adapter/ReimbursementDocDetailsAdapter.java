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
import com.genius.employee.activity.clientcall.model.ReimbursementDocModel;
import com.genius.employee.activity.clientcall.model.SPOCDetailsModel;

import java.util.ArrayList;


public class ReimbursementDocDetailsAdapter extends RecyclerView.Adapter<ReimbursementDocDetailsAdapter.MyViewHolder> {
    ArrayList<ReimbursementDocModel>itemList=new ArrayList();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reimbursement_docs_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

      myViewHolder.tvDocName.setText(itemList.get(i).getDocName());

      myViewHolder.llViewDetails.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

          }
      });






    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocName;
        LinearLayout llViewDetails;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocName=(TextView)itemView.findViewById(R.id.tvDocName);
            llViewDetails=(LinearLayout)itemView.findViewById(R.id.llViewDetails);



        }
    }

    public ReimbursementDocDetailsAdapter(ArrayList<ReimbursementDocModel> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
