package com.genius.employee.adapter;

//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.fragment.EducationFragment;
import com.genius.employee.fragment.FamilyFragment;
import com.genius.employee.model.EducationModel;
import com.genius.employee.model.FamilyModel;

import java.util.ArrayList;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.MyViewHolder> {
    ArrayList<EducationModel>educationList=new ArrayList();
    Fragment fragment;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.education_raw,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvQualification.setText(educationList.get(i).getCount()+". "+educationList.get(i).getQualification());
        myViewHolder.tvInstitute.setText(educationList.get(i).getInstitute());

        if (educationList.get(i).getSpecification().equals("null")||educationList.get(i).getSpecification().equals("0")||educationList.get(i).getSpecification().equals("")){
            myViewHolder.tvSpecification.setText("N/A");
        }else {
            myViewHolder.tvSpecification.setText(educationList.get(i).getSpecification());
        }


        if (educationList.get(i).getInstitute().equals("null")||educationList.get(i).getInstitute().equals("0")||educationList.get(i).getInstitute().equals("")){
            myViewHolder.tvInstitute.setText("N/A");
        }else {
            myViewHolder.tvInstitute.setText(educationList.get(i).getInstitute());
        }

        myViewHolder.tvMarks.setText(educationList.get(i).getMarks());
        myViewHolder.tvYear.setText(educationList.get(i).getYear());

        myViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EducationFragment)fragment).eduEditPopUp(i);
            }
        });



        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EducationFragment)fragment).deleteAlert(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvQualification,tvSpecification,tvInstitute,tvYear,tvMarks;
        ImageView imgEdit,imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQualification=(TextView)itemView.findViewById(R.id.tvQualification);
            tvSpecification=(TextView)itemView.findViewById(R.id.tvSpecification);
            tvInstitute=(TextView)itemView.findViewById(R.id.tvInstitute);
            tvYear=(TextView)itemView.findViewById(R.id.tvYear);
            tvMarks=(TextView)itemView.findViewById(R.id.tvMarks);

            imgEdit=(ImageView) itemView.findViewById(R.id.imgEdit);
            imgDelete=(ImageView) itemView.findViewById(R.id.imgDelete);
        }
    }

    public EducationAdapter(ArrayList<EducationModel> educationList,Fragment fragment) {
        this.educationList = educationList;
        this.fragment=fragment;
    }
}
