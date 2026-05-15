package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.genius.employee.R;
import com.genius.employee.activity.clientcall.adapter.SPOCDetailsAdapter;
import com.genius.employee.activity.clientcall.model.SPOCDetailsModel;

import java.util.ArrayList;

public class VirtualCallReportDetailsActivity extends AppCompatActivity {
    RecyclerView rvSPOC;
    ArrayList<SPOCDetailsModel> spocList=new ArrayList<>();
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_call_report_details);
        initView();
        onClick();
    }

    private void initView(){
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        rvSPOC=findViewById(R.id.rvSPOC);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VirtualCallReportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSPOC.setLayoutManager(layoutManager);
        spocList.add(new SPOCDetailsModel("Rakesh Jasu","9803468909","rk@gmail.com","",""));
        spocList.add(new SPOCDetailsModel("Shibu Sen","9804468909","sk@gmail.com","IT","Manager"));
        SPOCDetailsAdapter spcadapter=new SPOCDetailsAdapter(spocList, VirtualCallReportDetailsActivity.this);
        rvSPOC.setAdapter(spcadapter);

    }

    private void onClick(){
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