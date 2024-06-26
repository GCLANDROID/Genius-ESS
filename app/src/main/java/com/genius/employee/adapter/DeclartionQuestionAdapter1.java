package com.genius.employee.adapter;

import android.content.Context;
/*import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;*/
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

import java.util.ArrayList;

public class DeclartionQuestionAdapter1 extends RecyclerView.Adapter<DeclartionQuestionAdapter1.MyViewHolder> {
    ArrayList<Declarition2Model> itemList = new ArrayList();
    Context context;
    String rate1;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.declarition_raw, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final Declarition2Model qeuModel = itemList.get(i);
        myViewHolder.tvQuestion.setText(itemList.get(i).getQuestion());

        if (itemList.get(i).isSelected()) {


        } else {


        }


        // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);
        myViewHolder.llAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","1");
                    myViewHolder.llAgreeTick.setVisibility(View.VISIBLE);
                    myViewHolder.llDisAgreeTick.setVisibility(View.GONE);

                    itemList.get(i).setSelected(true);
                    itemList.get(i).setAnswervalue("1");


                    notifyDataSetChanged();
                    ((Declation2Activity) context).updateStatus1(i, true);



                } else {

                    ((Declation2Activity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();


                }

            }
        });

        myViewHolder.llDisAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qeuModel.setSelected(!qeuModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (qeuModel.isSelected()) {

                    Log.d("ratestar","2");
                    myViewHolder.llAgreeTick.setVisibility(View.GONE);
                    myViewHolder.llDisAgreeTick.setVisibility(View.VISIBLE);



                    itemList.get(i).setAnswervalue("0");
                    itemList.get(i).setSelected(true);

                    ((Declation2Activity) context).updateStatus1(i, true);
                    notifyDataSetChanged();



                } else {

                    ((Declation2Activity) context).updateStatus1(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        LinearLayout llAgree,llAgreeTick,llDisAgree,llDisAgreeTick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            tvQuestion = (TextView) itemView.findViewById(R.id.tvQuestion);


            llAgree=(LinearLayout)itemView.findViewById(R.id.llAgree);
            llAgreeTick=(LinearLayout)itemView.findViewById(R.id.llAgreeTick);
            llDisAgreeTick=(LinearLayout)itemView.findViewById(R.id.llDisAgreeTick);
            llDisAgree=(LinearLayout)itemView.findViewById(R.id.llDisAgree);


        }
    }

    public DeclartionQuestionAdapter1(ArrayList<Declarition2Model> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
