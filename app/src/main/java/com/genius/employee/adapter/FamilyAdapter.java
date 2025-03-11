package com.genius.employee.adapter;

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
import com.genius.employee.fragment.FamilyFragment;
import com.genius.employee.model.FamilyModel;
import com.genius.employee.model.LeaveModel;

import java.util.ArrayList;

public class FamilyAdapter extends  RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {
    ArrayList<FamilyModel>familyList=new ArrayList();
    FamilyFragment familyFragment;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.family_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

       myViewHolder.tvName.setText(familyList.get(i).getName());
       myViewHolder.tvRealation.setText(familyList.get(i).getRealation());

       if (familyList.get(i).getInformation().equals("null")||familyList.get(i).getInformation().equals("")||familyList.get(i).getInformation().equals("0")){
           myViewHolder.tvInformation.setText("N/A");
       }else {
           myViewHolder.tvInformation.setText(familyList.get(i).getInformation());
       }

       if (familyList.get(i).getDependent()==1){
           myViewHolder.tvDependent.setText("Yes");
       }else {
           myViewHolder.tvDependent.setText("No");
       }



        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FamilyFragment)familyFragment).familyEditPopUp(i);
            }
        });


        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FamilyFragment)familyFragment).deleteAlert(i);
            }
        });

       myViewHolder.tvmemberCount.setText(familyList.get(i).getCount()+". "+familyList.get(i).getRealation());

    }



    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvRealation,tvInformation,tvDependent,tvmemberCount;
        ImageView imgEdit,imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvRealation=(TextView)itemView.findViewById(R.id.tvRealation);
            tvInformation=(TextView)itemView.findViewById(R.id.tvInformation);
            tvDependent=(TextView)itemView.findViewById(R.id.tvDependent);
            imgEdit=(ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete=(ImageView)itemView.findViewById(R.id.imgDelete);
            tvmemberCount=(TextView) itemView.findViewById(R.id.tvmemberCount);
        }
    }

    public FamilyAdapter(ArrayList<FamilyModel> familyList,FamilyFragment familyFragment) {
        this.familyList = familyList;
        this.familyFragment=familyFragment;
    }
}
