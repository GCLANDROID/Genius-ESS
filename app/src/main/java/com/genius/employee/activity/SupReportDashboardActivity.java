package com.genius.employee.activity;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;
import com.genius.employee.model.EmployeeListModel;

public class SupReportDashboardActivity extends AppCompatActivity {
    LinearLayout llTeam,llReport,llTracking;
    ImageView imgBack,imgHome;
    String lebelId,deptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_report_dashboard);
        initView();
        onClick();
    }
    private void initView(){
        llTeam=(LinearLayout)findViewById(R.id.llTeam);
        llReport=(LinearLayout)findViewById(R.id.llReport);
        llTracking=(LinearLayout)findViewById(R.id.llTracking);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        lebelId=getIntent().getStringExtra("lebelId");
        deptId=getIntent().getStringExtra("deptId");
    }
    private void onClick(){
            llTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SupReportDashboardActivity.this,TeamReportDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            llReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SupReportDashboardActivity.this, NumberVisitActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            imgHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),EDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            llTracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SupReportDashboardActivity.this, EmployeeTreeListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("lebelId",lebelId);
                    intent.putExtra("deptId",deptId);
                    startActivity(intent);
                }
            });
    }
}
