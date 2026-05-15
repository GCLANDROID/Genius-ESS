package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.adapter.PublicDocListAdapter;
import com.genius.employee.utility.APi;

public class PhysicalEndMeetingActivity extends AppCompatActivity {
    RecyclerView rvDocument;
    LinearLayout llPublicReim,llPrivateReim,llTollDoc,llTollParking;
    Switch swToll,swParking;
    ImageView imgBack,imgHome;
    AlertDialog alertDialog1;
    AlertDialog addtripDialog;
    int navflag = 0;
    int tripFlag=0;
    TextView tvSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_end_meeting);
        initView();
        onClick();
    }

    private void initView(){
        llPublicReim=findViewById(R.id.llPublicReim);
        llPrivateReim=findViewById(R.id.llPrivateReim);
        llTollDoc=findViewById(R.id.llTollDoc);
        llTollParking=findViewById(R.id.llTollParking);
        rvDocument=findViewById(R.id.rvDocument);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PhysicalEndMeetingActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDocument.setLayoutManager(layoutManager);
        PublicDocListAdapter adapter=new PublicDocListAdapter(PhysicalEndMeetingActivity.this);
        rvDocument.setAdapter(adapter);

        if (APi.transportMode==1 && APi.SharingFlag==1){
            llPrivateReim.setVisibility(View.VISIBLE);

        }

        if (APi.transportMode==2 ){
            llPublicReim.setVisibility(View.VISIBLE);
            if (APi.SharingFlag==2){
                llPublicReim.setVisibility(View.GONE);
            }

        }
        swParking=findViewById(R.id.swParking);
        swToll=findViewById(R.id.swToll);

        imgHome=findViewById(R.id.imgHome);
        imgBack=findViewById(R.id.imgBack);
        tvSave=findViewById(R.id.tvSave);
    }

    private void onClick(){
        swToll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    llTollDoc.setVisibility(View.VISIBLE);
                }else {
                    llTollDoc.setVisibility(View.GONE);
                }
            }
        });

        swParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    llTollParking.setVisibility(View.VISIBLE);
                }else {
                    llTollParking.setVisibility(View.GONE);
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndMeetingpopUp();
            }
        });
    }

    public void showEndMeetingpopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PhysicalEndMeetingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.end_meeting_popup, null);
        dialogBuilder.setView(dialogView);

        TextView tvSave=dialogView.findViewById(R.id.tvSave);
        Switch swAddTrip=dialogView.findViewById(R.id.swAddTrip);
        swAddTrip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tvSave.setText("Add Another Trip");
                    navflag =1;
                }else {
                    tvSave.setText("End Daily Activity");
                    navflag=0;
                }
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navflag==1){
                    showAddTrippopUp();
                }else {
                    Intent intent=new Intent(PhysicalEndMeetingActivity.this,ClientCallReportActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();


    }

    public void showAddTrippopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PhysicalEndMeetingActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.trip_add_option_popup, null);
        dialogBuilder.setView(dialogView);

        LinearLayout llOffice=dialogView.findViewById(R.id.llOffice);
        LinearLayout llOfficeSelected=dialogView.findViewById(R.id.llOfficeSelected);

        LinearLayout llClient=dialogView.findViewById(R.id.llClient);
        LinearLayout llClientSelected=dialogView.findViewById(R.id.llClientSelected);

        LinearLayout llOfficeAddress=dialogView.findViewById(R.id.llOfficeAddress);

        llOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOfficeSelected.getVisibility()==View.GONE){
                    llOfficeSelected.setVisibility(View.VISIBLE);
                    llClientSelected.setVisibility(View.GONE);
                    llOfficeAddress.setVisibility(View.VISIBLE);
                    tripFlag=1;
                }
            }
        });

        llClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llClientSelected.getVisibility()==View.GONE){
                    llOfficeSelected.setVisibility(View.GONE);
                    llClientSelected.setVisibility(View.VISIBLE);
                    llOfficeAddress.setVisibility(View.GONE);
                    tripFlag=0;
                }
            }
        });
        TextView tvSave=dialogView.findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tripFlag==0){
                    Intent intent=new Intent(PhysicalEndMeetingActivity.this,ClientCallDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(PhysicalEndMeetingActivity.this,ReachedOfficeLogActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        ImageView imgCross=dialogView.findViewById(R.id.imgCross);
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtripDialog.dismiss();
            }
        });



        addtripDialog = dialogBuilder.create();
        addtripDialog.setCancelable(true);
        Window window = addtripDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        addtripDialog.show();


    }
}