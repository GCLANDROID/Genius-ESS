package com.genius.employee.activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Declation1ReportActivity extends AppCompatActivity {
    EditText etName, etDept, etLocation, etAddress, etMob, etEmail, etAge, etLeaveMonth;
    TextView tvLeave, tvbelow5;
    LinearLayout llLeaveMonth, llAge;
    Button btnSubmit;
    ArrayList<String> yesnoList = new ArrayList<String>();
    String children = "";
    String leave = "";
    LinearLayout llLoader;
    ScrollView scMain;
    Pref pref;
    ImageView imgBack, imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declation1report);
        initView();
        onClick();
    }

    private void initView() {
        pref = new Pref(Declation1ReportActivity.this);
        etName = (EditText) findViewById(R.id.etName);
        etDept = (EditText) findViewById(R.id.etDept);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etMob = (EditText) findViewById(R.id.etMob);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAge = (EditText) findViewById(R.id.etAge);
        etLeaveMonth = (EditText) findViewById(R.id.etLeaveMonth);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        llLeaveMonth = (LinearLayout) findViewById(R.id.llLeaveMonth);
        llAge = (LinearLayout) findViewById(R.id.llAge);

        tvbelow5 = (TextView) findViewById(R.id.tvbelow5);
        tvLeave = (TextView) findViewById(R.id.tvLeave);





        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        scMain = (ScrollView) findViewById(R.id.scMain);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        btnSubmit.setText("Next");
    }

    private void onClick() {


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Declation1ReportActivity.this,Declation2ReportActivity.class);
                startActivity(intent);
                finish();

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
                Intent intent = new Intent(Declation1ReportActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



}
