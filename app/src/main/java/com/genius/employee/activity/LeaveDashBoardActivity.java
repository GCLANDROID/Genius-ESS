package com.genius.employee.activity;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;

public class LeaveDashBoardActivity extends AppCompatActivity {
    LinearLayout llApplication,llBalance,llApplicationD,llBalanceD,llApplicationD1,llBalanceD1;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_dash_board);
        initialize();
        onClick();
    }

    private void initialize(){
        llApplication=(LinearLayout)findViewById(R.id.llApplication);
        llBalance=(LinearLayout)findViewById(R.id.llBalance);

        llApplicationD=(LinearLayout)findViewById(R.id.llApplicationD);
        llBalanceD=(LinearLayout)findViewById(R.id.llBalanceD);

        llApplicationD1=(LinearLayout)findViewById(R.id.llApplicationD1);
        llBalanceD1=(LinearLayout)findViewById(R.id.llBalanceD1);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void onClick(){
        llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llApplicationD.setVisibility(View.GONE);
                llApplicationD1.setVisibility(View.VISIBLE);

                llBalanceD.setVisibility(View.VISIBLE);
                llBalanceD1.setVisibility(View.GONE);
            }
        });

        llBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBalanceD.setVisibility(View.GONE);
                llBalanceD1.setVisibility(View.VISIBLE);

                llApplicationD.setVisibility(View.VISIBLE);
                llApplicationD1.setVisibility(View.GONE);
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
                Intent intent=new Intent(LeaveDashBoardActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
