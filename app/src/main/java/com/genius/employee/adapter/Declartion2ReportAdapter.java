package com.genius.employee.adapter;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.Declation2Activity;
import com.genius.employee.model.Declarition2Model;
import com.genius.employee.model.Declarition2ReportModel;

import java.util.ArrayList;

public class Declartion2ReportAdapter extends RecyclerView.Adapter<Declartion2ReportAdapter.MyViewHolder> {
    ArrayList<Declarition2ReportModel> itemList = new ArrayList();
    Context context;
    String rate1;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.declaritionreport_raw, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


        myViewHolder.tvQuestion.setText(itemList.get(i).getQuestion());

        if (itemList.get(i).getAnswervalue().equals("1")){
            myViewHolder.tvStatus.setText("Agree");
        }else {
            myViewHolder.tvStatus.setText("Dis Agree");
        }


        // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion,tvStatus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);




        }
    }

    public Declartion2ReportAdapter(ArrayList<Declarition2ReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
