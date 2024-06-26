package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.adapter.FamilyCovidAdapter;
import com.genius.employee.adapter.FamilyCovidReportAdapter;
import com.genius.employee.model.FamilyCovidModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CovidStatusReportActivity extends AppCompatActivity {
    TextView tvCovidStatus,tvCovidDate,tvcovidDocName,tvVaccineStatus,tvVaccineDate,tvVaccineDoc;
    Pref pref;
    RecyclerView rvItem;
    ArrayList<FamilyCovidModel>itemList=new ArrayList<FamilyCovidModel>();
    ProgressDialog progressDialog;
    ImageView imgBack,imgHome;
    TextView tvAdd;
    String StatusFile;
    String VaccinationFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_status_report);

        initView();
        onClick();
    }


    private void initView(){
        pref=new Pref(CovidStatusReportActivity.this);
        progressDialog=new ProgressDialog(CovidStatusReportActivity.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        tvCovidStatus=(TextView)findViewById(R.id.tvCovidStatus);
        tvCovidDate=(TextView)findViewById(R.id.tvCovidDate);
        tvcovidDocName=(TextView)findViewById(R.id.tvcovidDocName);
        tvVaccineStatus=(TextView)findViewById(R.id.tvVaccineStatus);
        tvVaccineDate=(TextView)findViewById(R.id.tvVaccineDate);
        tvVaccineDoc=(TextView)findViewById(R.id.tvVaccineDoc);

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new LinearLayoutManager(CovidStatusReportActivity.this, LinearLayoutManager.VERTICAL, false));

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvAdd=(TextView)findViewById(R.id.tvAdd);


    }



    private void onClick(){

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CovidStatusReportActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CovidStatusReportActivity.this,FamilyMemeberCovidStatusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        tvcovidDocName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StatusFile.equals("")){
                    Uri uri = Uri.parse(StatusFile); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        tvVaccineDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VaccinationFile.equals("")){
                    Uri uri = Uri.parse(VaccinationFile); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

    }


}