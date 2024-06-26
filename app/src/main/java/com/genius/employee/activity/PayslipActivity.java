package com.genius.employee.activity;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.adapter.ComponentAdapter;
import com.genius.employee.adapter.DeductionAdapter;
import com.genius.employee.model.ComponentModel;
import com.genius.employee.model.DeductionModel;

import java.util.ArrayList;

public class PayslipActivity extends AppCompatActivity {
    ArrayList<ComponentModel>componentItem=new ArrayList<>();
    ArrayList<DeductionModel>deductionItem=new ArrayList<>();
    RecyclerView rvComponent,rvDeduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip);
        initView();
    }

    private void initView(){
        rvComponent = (RecyclerView) findViewById(R.id.rvComponent);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PayslipActivity.this, LinearLayoutManager.VERTICAL, false);
        rvComponent.setLayoutManager(layoutManager);

        rvDeduction = (RecyclerView) findViewById(R.id.rvDeduction);
        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(PayslipActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDeduction.setLayoutManager(layoutManager1);

        setComponentItem();
    }

    private void setComponentItem(){
        componentItem.add(new ComponentModel("BASIC","10000","9000"));
        componentItem.add(new ComponentModel("HRA","8000","7890"));
        componentItem.add(new ComponentModel("TRANSPORT ALLOWANCE","800","800"));
        componentItem.add(new ComponentModel("SKILL DEVELOPMENT ALLOWANCE","900","900"));
        componentItem.add(new ComponentModel("CHILDREN EDUCATION ALLOWANCE","1900","800"));

        ComponentAdapter compAdapter=new ComponentAdapter(componentItem);
        rvComponent.setAdapter(compAdapter);

        setDeduction();
    }

    private void setDeduction(){
        deductionItem.add(new DeductionModel("PT","100"));
        deductionItem.add(new DeductionModel("PF","1156"));

        DeductionAdapter compAdapter=new DeductionAdapter(deductionItem);
        rvDeduction.setAdapter(compAdapter);

    }
}