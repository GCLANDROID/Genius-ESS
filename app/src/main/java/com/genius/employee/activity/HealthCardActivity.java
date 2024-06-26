package com.genius.employee.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HealthCardActivity extends AppCompatActivity {
    Spinner spBloodGrp,spDisease,spCovid,spDonate;
    EditText etDisease,etRemarks;
    LinearLayout llDisease,llDate,llDonate,llClickDate;
    TextView tvDate;
    ArrayList<String>bloodgrpList=new ArrayList<String>();
    ArrayList<String>yesnoList=new ArrayList<String>();
    String bloodGrp="";
    Pref pref;
    String disease="";
    String covid="";
    String donate="";
    String sdate="";
    AlertDialog alerDialog1;
    Button btnSave;
    String diseaseId,covidId,donateId;
    ImageView imgBack,imgHome;
    TextView tvEmpName,tvBranch,tvDOB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_card);
        initView();

        onClick();
    }

    private void initView(){
        pref=new Pref(HealthCardActivity.this);
        spBloodGrp=(Spinner)findViewById(R.id.spBloodGrp);
        spDisease=(Spinner)findViewById(R.id.spDisease);
        spCovid=(Spinner)findViewById(R.id.spCovid);
        spDonate=(Spinner)findViewById(R.id.spDonate);

        etDisease=(EditText)findViewById(R.id.etDisease);
        etRemarks=(EditText)findViewById(R.id.etRemarks);

        llDisease=(LinearLayout)findViewById(R.id.llDisease);
        llDate=(LinearLayout)findViewById(R.id.llDate);
        llDonate=(LinearLayout)findViewById(R.id.llDonate);
        llClickDate=(LinearLayout)findViewById(R.id.llClickDate);

        tvDate=(TextView)findViewById(R.id.tvDate);

        bloodgrpList.add("Please Select");
        bloodgrpList.add("A+");
        bloodgrpList.add("A-");
        bloodgrpList.add("B+");
        bloodgrpList.add("B-");
        bloodgrpList.add("AB+");
        bloodgrpList.add("AB-");
        bloodgrpList.add("O+");
        bloodgrpList.add("O-");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (HealthCardActivity.this, android.R.layout.simple_spinner_item,
                        bloodgrpList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBloodGrp.setAdapter(spinnerArrayAdapter);

        yesnoList.add("Please Select");
        yesnoList.add("Yes");
        yesnoList.add("No");

        ArrayAdapter<String> diseaseAdapter = new ArrayAdapter<String>
                (HealthCardActivity.this, android.R.layout.simple_spinner_item,
                        yesnoList); //selected item will look like a spinner set from XML
        diseaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDisease.setAdapter(diseaseAdapter);

        ArrayAdapter<String> covidAdapter = new ArrayAdapter<String>
                (HealthCardActivity.this, android.R.layout.simple_spinner_item,
                        yesnoList); //selected item will look like a spinner set from XML
        covidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCovid.setAdapter(covidAdapter);

        ArrayAdapter<String> donateAdapter = new ArrayAdapter<String>
                (HealthCardActivity.this, android.R.layout.simple_spinner_item,
                        yesnoList); //selected item will look like a spinner set from XML
        donateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDonate.setAdapter(donateAdapter);

        btnSave=(Button)findViewById(R.id.btnSave);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        tvDOB=(TextView)findViewById(R.id.tvDOB);
        tvBranch=(TextView)findViewById(R.id.tvBranch);

    }

    private void onClick(){
        spBloodGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    bloodGrp=bloodgrpList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    disease=yesnoList.get(i);
                    if (disease.equals("Yes")){
                        llDisease.setVisibility(View.VISIBLE);
                        diseaseId="1";
                    }else {
                        llDisease.setVisibility(View.GONE);
                        diseaseId="0";
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spCovid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    covid=yesnoList.get(i);
                    if (covid.equals("Yes")){
                        llDate.setVisibility(View.VISIBLE);

                        covidId="1";
                    }else {
                        llDate.setVisibility(View.GONE);

                        covidId="0";
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDonate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    donate=yesnoList.get(i);
                    if (donate.equals("Yes")){
                        donateId="1";
                    }else {
                        donateId="0";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        llClickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bloodGrp.equals("")){
                    if (!disease.equals("")){
                        if (!covid.equals("")){
                            if (etRemarks.getText().toString().length()>0){
                                if (!donate.equals("")) {
                                    chronicDiseaseChecking();
                                }else {
                                    Toast.makeText(HealthCardActivity.this,"Please Select is available to donate Blood or Not",Toast.LENGTH_LONG).show();

                                }
                            }else {
                                Toast.makeText(HealthCardActivity.this,"Please enter remark",Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(HealthCardActivity.this,"Please Select Covid Effected or Not",Toast.LENGTH_LONG).show();

                        }

                    }else {
                        Toast.makeText(HealthCardActivity.this,"Please Select Choronic Diseases option",Toast.LENGTH_LONG).show();

                    }

                }else {
                    Toast.makeText(HealthCardActivity.this,"Please Select Blood Group",Toast.LENGTH_LONG).show();
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
                Intent intent=new Intent(HealthCardActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void showDateDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                StringBuffer strBuf = new StringBuffer();
                strBuf.append("Select date is ");
                strBuf.append(year);
                strBuf.append("-");
                strBuf.append(month + 1);
                strBuf.append("-");
                strBuf.append(dayOfMonth);


            }
        };

        // Get current year, month and day.
        Calendar now = Calendar.getInstance();
        final int year2 = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        // Create the new DatePickerDialog instance.
        /*DatePickerDialog datePickerDialog = new DatePickerDialog(SalesManageActivity.this, android.R.style.Theme_Holo_Dialog, onDateSetListener, year, month, day);*/
        final DatePickerDialog dialog = new DatePickerDialog(HealthCardActivity.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                sdate = (m + 1) + "/" + d + "/" + y;
                tvDate.setText(sdate);



                tvDate.setText(sdate);

                //  pref.saveDOJ(sdate);


            }
        }, year2, month, day);


        // Set dialog icon and title.
        dialog.setIcon(R.drawable.clockicon);
        dialog.setTitle("Please select date.");
        dialog.getDatePicker().setMaxDate((long) (System.currentTimeMillis() - 1000));

        // Popup the dialog.

        dialog.show();
    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HealthCardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                Intent intent = new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void chronicDiseaseChecking(){
        if (disease.equals("Yes")){
            if (etDisease.getText().toString().length()>0){
                covidChecking();
            }else {
                Toast.makeText(HealthCardActivity.this,"Please mention the disease",Toast.LENGTH_LONG).show();
            }
        }else {
            covidChecking();
        }
    }

    private void covidChecking(){
        if (covid.equals("Yes")){
            if (!sdate.equals("")){




            }else {
                Toast.makeText(HealthCardActivity.this,"Please Select Recovery Date",Toast.LENGTH_LONG).show();
            }

        }else {


        }
    }


}