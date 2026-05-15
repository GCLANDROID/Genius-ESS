package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.adapter.ReimbursementDocDetailsAdapter;
import com.genius.employee.activity.clientcall.adapter.SPOCDetailsAdapter;
import com.genius.employee.activity.clientcall.model.ReimbursementDocModel;
import com.genius.employee.activity.clientcall.model.SPOCDetailsModel;

import java.util.ArrayList;

public class PhyClinetCallReportDetailsActivity extends AppCompatActivity {
    RecyclerView rvSPOC,rvDoc;
    ArrayList<SPOCDetailsModel>spocList=new ArrayList<>();
    ArrayList<ReimbursementDocModel>reimDocList=new ArrayList<>();
    int returntrip;
    LinearLayout llReturnTrip,llAddTrip,llExp,llHotel;
    TextView tvAddRetTrip;
    Switch swHotel,swOtherExp;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phy_clinet_call_report_details);
        initView();
        onClick();
    }

    private void initView(){
        returntrip=getIntent().getIntExtra("returntrip",0);
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        rvSPOC=findViewById(R.id.rvSPOC);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PhyClinetCallReportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSPOC.setLayoutManager(layoutManager);
        spocList.add(new SPOCDetailsModel("Rakesh Jasu","9803468909","rk@gmail.com","",""));
        spocList.add(new SPOCDetailsModel("Shibu Sen","9804468909","sk@gmail.com","IT","Manager"));

        SPOCDetailsAdapter spcadapter=new SPOCDetailsAdapter(spocList, PhyClinetCallReportDetailsActivity.this);
        rvSPOC.setAdapter(spcadapter);


        rvDoc=findViewById(R.id.rvDoc);
        LinearLayoutManager doclayoutManager
                = new LinearLayoutManager(PhyClinetCallReportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvDoc.setLayoutManager(doclayoutManager);

        reimDocList.add(new ReimbursementDocModel("Flight Ticket"));
        reimDocList.add(new ReimbursementDocModel("Cab Service"));

        ReimbursementDocDetailsAdapter reimdapter=new ReimbursementDocDetailsAdapter(reimDocList, PhyClinetCallReportDetailsActivity.this);
        rvDoc.setAdapter(reimdapter);

        llReturnTrip=findViewById(R.id.llReturnTrip);
        if (returntrip==1){
            llReturnTrip.setVisibility(LinearLayout.VISIBLE);
        }

        llAddTrip=findViewById(R.id.llAddTrip);
        tvAddRetTrip=findViewById(R.id.tvAddRetTrip);

        llExp=findViewById(R.id.llExp);
        llHotel=findViewById(R.id.llHotel);

        swHotel=findViewById(R.id.swHotel);
        swOtherExp=findViewById(R.id.swOtherExp);


    }

    private void onClick(){
        tvAddRetTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llAddTrip.setVisibility(View.VISIBLE);
                tvAddRetTrip.setVisibility(View.GONE);
            }
        });
        swHotel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    llHotel.setVisibility(View.VISIBLE);
                }else {
                    llHotel.setVisibility(View.GONE);
                }
            }
        });

        swOtherExp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    llExp.setVisibility(View.VISIBLE);
                }else {
                    llExp.setVisibility(View.GONE);
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
    }
}