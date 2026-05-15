package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.adapter.PhysicalCallReportAdapter;
import com.genius.employee.activity.clientcall.adapter.VirtualCallReportAdapter;
import com.genius.employee.activity.clientcall.model.PhysicalCallReportModel;
import com.genius.employee.activity.clientcall.model.VirtualCallReportModel;

import java.util.ArrayList;

public class ClientCallReportActivity extends AppCompatActivity {
    RecyclerView rvPhysical,rvVirtual;
    ArrayList<PhysicalCallReportModel>phyList=new ArrayList<>();
    ArrayList<VirtualCallReportModel>virtualList=new ArrayList<>();

    LinearLayout llDaily,llDay,llDaySelected,llDailySelected,llReport,llDateWise;
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_call_report);
        initView();
        onClick();
    }

    private void initView(){

        llDaily=findViewById(R.id.llDaily);
        llDailySelected=findViewById(R.id.llDailySelected);


        llDay=findViewById(R.id.llDay);
        llDaySelected=findViewById(R.id.llDaySelected);

        llReport=findViewById(R.id.llReport);
        llDateWise=findViewById(R.id.llDateWise);

        rvPhysical=findViewById(R.id.rvPhysical);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(ClientCallReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvPhysical.setLayoutManager(layoutManager);
        phyList.add(new PhysicalCallReportModel("IFB Pvt Ltd.","20 KM","14 May 2026",0));
        phyList.add(new PhysicalCallReportModel("METSO INDIA PRIVATE LIMITED.","200 KM","10 May 2026",1));
        PhysicalCallReportAdapter reportAdapter=new PhysicalCallReportAdapter(phyList,ClientCallReportActivity.this);
        rvPhysical.setAdapter(reportAdapter);


        rvVirtual=findViewById(R.id.rvVirtual);
        LinearLayoutManager layoutManagerVirtual
                = new LinearLayoutManager(ClientCallReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvVirtual.setLayoutManager(layoutManagerVirtual);

        virtualList.add(new VirtualCallReportModel("ABP PVT LTD.","1 hr. 30 min","14 May 2026"));
        virtualList.add(new VirtualCallReportModel("KHADIM INDIA LTD.","2 hr. 30 min","14 May 2026"));
        VirtualCallReportAdapter virtualreportAdapter=new VirtualCallReportAdapter(virtualList,ClientCallReportActivity.this);
        rvVirtual.setAdapter(virtualreportAdapter);

        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);




    }

    private void onClick(){
        llDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llDailySelected.getVisibility()==View.GONE){
                    llDaySelected.setVisibility(View.GONE);
                    llDailySelected.setVisibility(View.VISIBLE);
                    llReport.setVisibility(View.VISIBLE);
                    llDateWise.setVisibility(View.GONE);
                }
            }
        });


        llDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llDaySelected.getVisibility()==View.GONE){
                    llDaySelected.setVisibility(View.VISIBLE);
                    llDailySelected.setVisibility(View.GONE);
                    llReport.setVisibility(View.VISIBLE);
                    llDateWise.setVisibility(View.VISIBLE);
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}