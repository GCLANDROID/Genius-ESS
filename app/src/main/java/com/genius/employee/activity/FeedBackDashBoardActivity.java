package com.genius.employee.activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;
import com.genius.employee.utility.NetworkConnectionCheck;


public class FeedBackDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage,llReport;
    NetworkConnectionCheck connectionCheck;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_dash_board);
        initialize();
        onClick();
    }
    private void initialize(){
        connectionCheck=new NetworkConnectionCheck(FeedBackDashBoardActivity.this);
        llManage=(LinearLayout)findViewById(R.id.llManage);
        llReport=(LinearLayout)findViewById(R.id.llReport);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void onClick(){
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isGPSEnabled()) {
                    if (connectionCheck.isNetworkAvailable()) {
                        Intent intent = new Intent(FeedBackDashBoardActivity.this, ClientListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        connectionCheck.getNetworkActiveAlert().show();
                    }
                }else {
                    connectionCheck.getSettingsAlert().show();
                }
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackDashBoardActivity.this, FeedBackreportActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FeedBackDashBoardActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
