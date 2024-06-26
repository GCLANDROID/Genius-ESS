package com.genius.employee.activity;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;

public class ReimbursementDashboardActivity extends AppCompatActivity {
    LinearLayout llCliam,llReport,llApproval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement_dashboard);
        initView();
        onClick();
    }

    private void initView(){
        llCliam=(LinearLayout)findViewById(R.id.llCliam);
        llReport=(LinearLayout)findViewById(R.id.llReport);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
    }

    private void onClick(){
        llCliam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReimbursementDashboardActivity.this,CliamManageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}