package com.genius.employee.activity;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Declation1Activity extends AppCompatActivity {
    EditText etName,etDept,etLocation,etAddress,etMob,etEmail,etAge,etLeaveMonth;
    Spinner spChildren,spLeave;
    LinearLayout llLeaveMonth,llAge;
    Button btnSubmit;
    ArrayList<String>yesnoList=new ArrayList<String>();
    String children="";
    String leave="";
    LinearLayout llLoader;
    ScrollView scMain;
    Pref pref;
    ImageView imgBack,imgHome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declation1);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(Declation1Activity.this);
        etName=(EditText)findViewById(R.id.etName);
        etDept=(EditText)findViewById(R.id.etDept);
        etLocation=(EditText)findViewById(R.id.etLocation);
        etAddress=(EditText)findViewById(R.id.etAddress);
        etMob=(EditText)findViewById(R.id.etMob);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etAge=(EditText)findViewById(R.id.etAge);
        etLeaveMonth=(EditText)findViewById(R.id.etLeaveMonth);

        btnSubmit=(Button)findViewById(R.id.btnSubmit);

        llLeaveMonth=(LinearLayout)findViewById(R.id.llLeaveMonth);
        llAge=(LinearLayout)findViewById(R.id.llAge);

        spChildren=(Spinner)findViewById(R.id.spChildren);
        spLeave=(Spinner)findViewById(R.id.spLeave);

        yesnoList.add("No");
        yesnoList.add("Yes");



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (Declation1Activity.this, android.R.layout.simple_spinner_item,
                        yesnoList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChildren.setAdapter(spinnerArrayAdapter);


        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>
                (Declation1Activity.this, android.R.layout.simple_spinner_item,
                        yesnoList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLeave.setAdapter(spinnerArrayAdapter1);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        scMain=(ScrollView)findViewById(R.id.scMain);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
    }

    private void onClick(){
        spLeave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    leave=yesnoList.get(i);

                if (leave.equals("Yes")){
                    llLeaveMonth.setVisibility(View.VISIBLE);
                }else {
                    llLeaveMonth.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    children=yesnoList.get(i);

                if (children.equals("Yes")){
                    llAge.setVisibility(View.VISIBLE);
                }else {
                    llLeaveMonth.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postfunction();
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
                Intent intent=new Intent(Declation1Activity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void postfunction(){
        if (children.equals("Yes")){
            if (etAge.getText().toString().length()>0){
                postDec();

            }else {
                Toast.makeText(Declation1Activity.this,"Please enter number of  children",Toast.LENGTH_LONG).show();
            }
        }else if (leave.equals("Yes")){
            if (etLeaveMonth.getText().toString().length()>0){
                postDec();

            }else {
                Toast.makeText(Declation1Activity.this,"Please enter your maternity or paternity leave month",Toast.LENGTH_LONG).show();
            }
        }else if (children.equals("Yes") && leave.equals("Yes")){
            if (etAge.getText().toString().length()>0){
                if (etLeaveMonth.getText().toString().length()>0){
                    postDec();

                }else {
                    Toast.makeText(Declation1Activity.this,"Please enter your maternity or paternity leave month",Toast.LENGTH_LONG).show();


                }

            }else {
                Toast.makeText(Declation1Activity.this,"Please enter your children age",Toast.LENGTH_LONG).show();

            }
        }else {
            postDec();
        }
    }

    private void postDec(){
        if (etName.getText().toString().length()>0){
            if (etDept.getText().toString().length()>0){
                if (etLocation.getText().toString().length()>0){
                    if (etAddress.getText().toString().length()>2){
                        if (etMob.getText().toString().length()>9){
                            if (etEmail.getText().toString().length()>0){
                                if (etEmail.getText().toString().contains("@")){
                                    if (!children.equals("")){
                                        if (!leave.equals("")){
                                            Intent intent=new Intent(Declation1Activity.this,Declation2Activity.class);
                                            intent.putExtra("childbelow",children);
                                            intent.putExtra("noChild",etAge.getText().toString());
                                            intent.putExtra("leave",leave);
                                            intent.putExtra("leaveMonth",etLeaveMonth.getText().toString());
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(Declation1Activity.this,"Please select have you any paternity or maternity leave type",Toast.LENGTH_LONG).show();
                                        }

                                    }else {
                                        Toast.makeText(Declation1Activity.this,"Please select any 5 yrs below children",Toast.LENGTH_LONG).show();
                                    }

                                }else {
                                    Toast.makeText(Declation1Activity.this,"Please enter valid email address",Toast.LENGTH_LONG).show();
                                }

                            }else {
                                Toast.makeText(Declation1Activity.this,"Please enter email id",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(Declation1Activity.this,"Please enter mobilen number",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(Declation1Activity.this,"Please enter address",Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(Declation1Activity.this,"Please enter location details",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(Declation1Activity.this,"Please enter department ",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Declation1Activity.this,"Please enter name",Toast.LENGTH_LONG).show();
        }
    }



}
