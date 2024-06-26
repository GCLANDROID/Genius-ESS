package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.employee.R;
import com.genius.employee.adapter.Declartion2ReportAdapter;
import com.genius.employee.adapter.DeclartionQuestionAdapter;
import com.genius.employee.adapter.DeclartionQuestionAdapter1;
import com.genius.employee.model.Declarition2Model;
import com.genius.employee.model.Declarition2ReportModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.kyanogen.signatureview.SignatureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Declation2ReportActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<Declarition2ReportModel>itemList=new ArrayList<Declarition2ReportModel>();

    Button btnSubmit;

    LinearLayout llLoader;
    ScrollView scMain;
    Pref pref;
    ImageView imgBack,imgHome;
    LinearLayout llDeclare,llStatement;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declation2);
        initView();
        onClick();

    }

    private void initView(){
        pref=new Pref(Declation2ReportActivity.this);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Declation2ReportActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        rvItem.setLayoutManager(layoutManager);


       llDeclare=(LinearLayout)findViewById(R.id.llDeclare);
       llStatement=(LinearLayout)findViewById(R.id.llStatement);
       llDeclare.setVisibility(View.GONE);
        llStatement.setVisibility(View.GONE);

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setText("Ok");
        btnSubmit.setVisibility(View.VISIBLE);



        llLoader=(LinearLayout)findViewById(R.id.llLoader);

        scMain=(ScrollView)findViewById(R.id.scMain);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);






    }






    private void setAdapter(){
        Declartion2ReportAdapter decAdapter =new Declartion2ReportAdapter(itemList, Declation2ReportActivity.this);
        rvItem.setAdapter(decAdapter);
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
                Intent intent=new Intent(Declation2ReportActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Declation2ReportActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }



}
