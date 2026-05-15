package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.employee.R;
import com.genius.employee.activity.MarkInManageActivity;

import java.util.ArrayList;

public class ClientCallDashboardActivity extends AppCompatActivity {
    LinearLayout llPhysical,llPhysicalSelected,llVirtual,llVirtualSelected;
    LinearLayout llVendor,llVendorSelected,llClient,llClientSelected,llOther,llOtherSelected;
    LinearLayout llOtherOption;
    int callTypeFlag=0;
    int scheduleTypeFlag=0;
    ArrayList<String>otherOptionList=new ArrayList<>();
    Spinner spOtherOption;
    TextView tvRegister;
    String otherOptionStr="";
    String scheduleTypeStr="";
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_call_dashboard);
        initView();
        onClick();
    }

    private void initView(){
        llPhysical=findViewById(R.id.llPhysical);
        llPhysicalSelected=findViewById(R.id.llPhysicalSelected);

        llVirtual=findViewById(R.id.llVirtual);
        llVirtualSelected=findViewById(R.id.llVirtualSelected);

        llVendor=findViewById(R.id.llVendor);
        llVendorSelected=findViewById(R.id.llVendorSelected);

        llClient=findViewById(R.id.llClient);
        llClientSelected=findViewById(R.id.llClientSelected);

        llOther=findViewById(R.id.llOther);
        llOtherSelected=findViewById(R.id.llOtherSelected);

        llOtherOption=findViewById(R.id.llOtherOption);
        spOtherOption=findViewById(R.id.spOtherOption);

        otherOptionList.add("Please select Other Option");
        otherOptionList.add("PF Office");
        otherOptionList.add("Income Tax Dept.");
        otherOptionList.add("High Court");
        otherOptionList.add("Others.");



        ArrayAdapter<String> spinnerMonthArrayAdapter = new ArrayAdapter<String>
                (ClientCallDashboardActivity.this, android.R.layout.simple_spinner_item,
                        otherOptionList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOtherOption.setAdapter(spinnerMonthArrayAdapter);

        tvRegister=findViewById(R.id.tvRegister);

        imgHome=findViewById(R.id.imgHome);
        imgBack=findViewById(R.id.imgBack);

    }

    private void onClick(){
        llPhysical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llPhysicalSelected.getVisibility()==View.GONE){
                    llPhysicalSelected.setVisibility(View.VISIBLE);
                    llVirtualSelected.setVisibility(View.GONE);
                    callTypeFlag=1;
                }
            }
        });


        llVirtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llVirtualSelected.getVisibility()==View.GONE){
                    llPhysicalSelected.setVisibility(View.GONE);
                    llVirtualSelected.setVisibility(View.VISIBLE);
                    callTypeFlag=2;
                }
            }
        });


        llVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llVendorSelected.getVisibility()==View.GONE){
                    llVendorSelected.setVisibility(View.VISIBLE);
                    llClientSelected.setVisibility(View.GONE);
                    llOtherSelected.setVisibility(View.GONE);
                    llOtherOption.setVisibility(View.GONE);
                    scheduleTypeStr="Vendor";
                    otherOptionStr="";
                    scheduleTypeFlag=1;
                }
            }
        });

        llClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llClientSelected.getVisibility()==View.GONE){
                    llVendorSelected.setVisibility(View.GONE);
                    llClientSelected.setVisibility(View.VISIBLE);
                    llOtherSelected.setVisibility(View.GONE);
                    llOtherOption.setVisibility(View.GONE);
                    scheduleTypeStr="Client";
                    otherOptionStr="";
                    scheduleTypeFlag=2;
                }
            }
        });

        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llOtherSelected.getVisibility()==View.GONE){
                    llVendorSelected.setVisibility(View.GONE);
                    llClientSelected.setVisibility(View.GONE);
                    llOtherSelected.setVisibility(View.VISIBLE);
                    llOtherOption.setVisibility(View.VISIBLE);
                    scheduleTypeStr="Other Offical Work";
                    scheduleTypeFlag=3;
                }
            }
        });
        spOtherOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0){
                    otherOptionStr=otherOptionList.get(i);
                }
                else {
                    otherOptionStr="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callTypeFlag==0) {
                    Toast.makeText(ClientCallDashboardActivity.this, "Please select Call Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scheduleTypeFlag==0) {
                    Toast.makeText(ClientCallDashboardActivity.this, "Please select Schedule Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scheduleTypeFlag==3) {
                    if (spOtherOption.getSelectedItemPosition()==0) {
                        Toast.makeText(ClientCallDashboardActivity.this, "Please select Other Option", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (callTypeFlag==2){
                    Intent intent=new Intent(ClientCallDashboardActivity.this,VirtualCallRegistrationActivity.class);
                    intent.putExtra("scheduleTypeStr",scheduleTypeStr);
                    intent.putExtra("otherOption",otherOptionStr);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }else if (callTypeFlag==1){
                    Intent intent=new Intent(ClientCallDashboardActivity.this,PhysicalCallRegActivity.class);
                    intent.putExtra("scheduleTypeStr",scheduleTypeStr);
                    intent.putExtra("otherOption",otherOptionStr);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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