package com.genius.employee.activity.clientcall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.employee.R;

public class PhysicalCallVisitLogActivity extends AppCompatActivity {
    TextView tvSave;
    AlertDialog alertDialog1;
    int navFalg=0;
    ImageView imgProgress;
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_call_visit_log);
        initView();
        onClick();
    }

    private void initView(){
        imgProgress=findViewById(R.id.imgProgress);
        tvSave=findViewById(R.id.tvSave);

        imgHome=findViewById(R.id.imgHome);
        imgBack=findViewById(R.id.imgBack);
    }

    private void onClick(){
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navFalg==1){
                    Intent intent=new Intent(PhysicalCallVisitLogActivity.this, PhysicalEndMeetingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else {
                    showReachedpopUp();
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

    public void showReachedpopUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PhysicalCallVisitLogActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.client_reached_popup, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCross=dialogView.findViewById(R.id.imgCross);
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();

            }
        });
        TextView tvSaved=dialogView.findViewById(R.id.tvSave);
        tvSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
                imgProgress.setVisibility(View.VISIBLE);
                navFalg=1;
                tvSave.setText("End Meeting");
            }
        });

        alertDialog1 = dialogBuilder.create();
        alertDialog1.setCancelable(true);
        Window window = alertDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog1.show();


    }
}