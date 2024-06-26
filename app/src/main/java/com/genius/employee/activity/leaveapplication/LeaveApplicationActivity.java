package com.genius.employee.activity.leaveapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
/*import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;*/
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.genius.employee.R;
import com.genius.employee.activity.EDashBoardActivity;
import com.genius.employee.utility.Pref;


public class LeaveApplicationActivity extends AppCompatActivity {
    LinearLayout llApplication,llApproval,llDetails;
    ImageView imgBack,imgHome;
    TextView tvApproval,tvDetails,tvApllication,tvToolBar;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);
        initView();
        loadApplicationFragment();
        onClick();
    }


    private void initView(){
        pref=new Pref(getApplicationContext());
        llApplication=(LinearLayout)findViewById(R.id.llApplication);
        llApproval=(LinearLayout)findViewById(R.id.llApproval);
        llDetails=(LinearLayout)findViewById(R.id.llDetails);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvApllication=(TextView)findViewById(R.id.tvApllication);
        tvDetails=(TextView)findViewById(R.id.tvDetails);
        tvApproval=(TextView)findViewById(R.id.tvApproval);
        tvToolBar=(TextView)findViewById(R.id.tvToolBar);

    }
    private void onClick(){
        llApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApplicationFragment();
            }
        });

        llApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadApproverFragment();
            }
        });

        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetailsFragment();
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
                Intent intent=new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void loadApplicationFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApplicationFragment pfragment=new ApplicationFragment();
        transaction.replace(R.id.frameLayout, pfragment);
        transaction.commit();


        llApplication.setBackgroundColor(Color.parseColor("#006CFF"));
        llDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llApproval.setBackgroundColor(Color.parseColor("#FFFFFF"));


        tvApllication.setTextColor(Color.parseColor("#FFFFFF"));
        tvDetails.setTextColor(Color.parseColor("#006CFF"));
        tvApproval.setTextColor(Color.parseColor("#006CFF"));
   }

    public void loadApproverFragment() {
        /*FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ApproverFragment efr=new ApproverFragment();
        transaction.replace(R.id.frameLayout, efr);
        transaction.commit();*/
        llApplication.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llApproval.setBackgroundColor(Color.parseColor("#006CFF"));

        tvApllication.setTextColor(Color.parseColor("#006CFF"));
        tvDetails.setTextColor(Color.parseColor("#006CFF"));
        tvApproval.setTextColor(Color.parseColor("#FFFFFF"));
    }


    public void loadDetailsFragment() {
        /*FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DetailsFragment htfragment=new DetailsFragment();
        transaction.replace(R.id.frameLayout, htfragment);
        transaction.commit();*/


        llApplication.setBackgroundColor(Color.parseColor("#FFFFFF"));
        llDetails.setBackgroundColor(Color.parseColor("#006CFF"));
        llApproval.setBackgroundColor(Color.parseColor("#FFFFFF"));


        tvApllication.setTextColor(Color.parseColor("#006CFF"));
        tvDetails.setTextColor(Color.parseColor("#FFFFFF"));
        tvApproval.setTextColor(Color.parseColor("#006CFF"));

    }

    public void  approverVisibility(){
        llApproval.setVisibility(View.VISIBLE);
    }

    public void  approverHidden(){
        llApproval.setVisibility(View.GONE);
    }
}
