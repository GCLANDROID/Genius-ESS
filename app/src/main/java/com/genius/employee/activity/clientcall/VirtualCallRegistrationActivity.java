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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.employee.R;
import com.genius.employee.activity.ClientListActivity;
import com.genius.employee.activity.DailyActivityDashBoardActivity;
import com.genius.employee.activity.ForgotPasswordActivity;
import com.genius.employee.activity.clientcall.adapter.AddSPOCAdapter;
import com.genius.employee.activity.clientcall.model.AddSPOCModel;

import java.util.ArrayList;

public class VirtualCallRegistrationActivity extends AppCompatActivity {
    LinearLayout llAddSPOC,llMain,llSPOCPopup;
    ImageView imgCross;
    String scheduleTypeStr,otherOption;
    TextView tvClientTitle;
    EditText etClientName;
    ArrayList<AddSPOCModel>spocList=new ArrayList<>();
    EditText etSPOCName,etMobile,etEmail,etDepartment,etDesignation;
    TextView tvSaveSPOC;
    RecyclerView rvSPOC;
    TextView tvAddSpocTitle,tvSave;
    AlertDialog alertDialog1;
    ImageView imgBack,imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_call_registration);
        initView();
        onclick();
    }

    private void initView(){
        llAddSPOC=findViewById(R.id.llAddSPOC);
        llMain=findViewById(R.id.llMain);
        llSPOCPopup=findViewById(R.id.llSPOCPopup);

        imgCross=findViewById(R.id.imgCross);
        scheduleTypeStr=getIntent().getStringExtra("scheduleTypeStr");
        tvClientTitle=findViewById(R.id.tvClientTitle);
        tvClientTitle.setText("Name of the "+scheduleTypeStr);

        etClientName=findViewById(R.id.etClientName);
        otherOption=getIntent().getStringExtra("otherOption");
        if (otherOption.equalsIgnoreCase("Others.")){
            etClientName.setHint("Please specify the name of the "+scheduleTypeStr+"");
        }else {
            etClientName.setText(otherOption);
        }

        etSPOCName=findViewById(R.id.etSPOCName);
        etMobile=findViewById(R.id.etMobile);
        etEmail=findViewById(R.id.etEmail);
        etDepartment=findViewById(R.id.etDepartment);
        etDesignation=findViewById(R.id.etDesignation);

        tvAddSpocTitle=findViewById(R.id.tvAddSpocTitle);

        tvSaveSPOC=findViewById(R.id.tvSaveSPOC);
        rvSPOC=findViewById(R.id.rvSPOC);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(VirtualCallRegistrationActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSPOC.setLayoutManager(layoutManager);
        tvSave=findViewById(R.id.tvSave);

        imgHome=findViewById(R.id.imgHome);
        imgBack=findViewById(R.id.imgBack);


    }

    private void onclick(){
        llAddSPOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSPOCPopup.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
            }
        });

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSPOCPopup.setVisibility(View.GONE);
                llMain.setVisibility(View.VISIBLE);
            }
        });

        tvSaveSPOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSPOCName.getText().toString().length()>0){
                    if (etMobile.getText().toString().length()>0){
                        if (etEmail.getText().toString().length()>0){
                            AddSPOCModel model=new AddSPOCModel();
                            model.setSpocName(etSPOCName.getText().toString());
                            model.setSpocMob(etMobile.getText().toString());
                            model.setSpocEmail(etEmail.getText().toString());
                            model.setSpocDept(etDepartment.getText().toString());
                            model.setSpocDesignation(etDesignation.getText().toString());

                            spocList.add(model);

                            llSPOCPopup.setVisibility(View.GONE);
                            llMain.setVisibility(View.VISIBLE);
                            rvSPOC.setAdapter(new AddSPOCAdapter( spocList,VirtualCallRegistrationActivity.this));
                            tvAddSpocTitle.setText("Add Another SPOC Information");
                            etSPOCName.setText("");
                            etMobile.setText("");
                            etEmail.setText("");
                            etDepartment.setText("");
                            etDesignation.setText("");

                        }else {
                            etEmail.setError("Please enter SPOC Email");
                            etEmail.requestFocus();
                        }

                    }else {
                        etMobile.setError("Please enter SPOC Mobile Number");
                        etMobile.requestFocus();
                    }

                }else {
                    etSPOCName.setError("Please enter SPOC Name");
                    etSPOCName.requestFocus();
                }
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VirtualCallRegistrationActivity.this, ClientCallReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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


    public void showSPOCDetails(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(VirtualCallRegistrationActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.added_spoc_details_popup, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCross=dialogView.findViewById(R.id.imgCross);
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();

            }
        });
        TextView tvName = (TextView) dialogView.findViewById(R.id.tvName);
        tvName.setText(spocList.get(pos).getSpocName());

        TextView tvMob = (TextView) dialogView.findViewById(R.id.tvMob);
        tvMob.setText(spocList.get(pos).getSpocMob());

        TextView tvEmail = (TextView) dialogView.findViewById(R.id.tvEmail);
        tvEmail.setText(spocList.get(pos).getSpocEmail());

        TextView tvDept = (TextView) dialogView.findViewById(R.id.tvDept);
        tvDept.setText(spocList.get(pos).getSpocDept());


        TextView tvDesignation = (TextView) dialogView.findViewById(R.id.tvDesignation);
        tvDesignation.setText(spocList.get(pos).getSpocDesignation());

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();


    }


}