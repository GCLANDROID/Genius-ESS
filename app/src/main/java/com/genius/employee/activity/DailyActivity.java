package com.genius.employee.activity;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.genius.employee.R;
import com.genius.employee.utility.Pref;

import java.util.ArrayList;
import java.util.List;

public class DailyActivity extends AppCompatActivity {

    Pref pref;
    Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        init();
        onClick();
    }

    private void init(){
        pref=new Pref(getApplicationContext());
        btnGo=(Button)findViewById(R.id.btnGo);




    }

    private void onClick(){
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DailyActivity.this,DailyInTimeActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
