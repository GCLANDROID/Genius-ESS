package com.genius.employee.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.model.DeptSpinnerModel;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClintOfficeActivity extends AppCompatActivity {
    ImageView imgBack,imgHome;
    LinearLayout llNext,llLoader,llMain;
    ArrayList<DeptSpinnerModel> modelOfficeList=new ArrayList();
    ArrayList<String>officeList=new ArrayList();
    Spinner spOffice;
    Pref pref;
    EditText etOffice;
    LinearLayout llSpOffice;
    TextView tvOther;
    String clintid;
    String clientname;
    LinearLayout llNoData,llSkip;
    AlertDialog alertDialog;
    NetworkConnectionCheck connectionCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clint_office);
        initialize();
        onClick();
    }
    private void initialize(){
        pref=new Pref(getApplicationContext());
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        llNext=(LinearLayout)findViewById(R.id.llNext);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        spOffice=(Spinner)findViewById(R.id.spOffice);

        etOffice=(EditText)findViewById(R.id.etOffice);
        llSpOffice=(LinearLayout)findViewById(R.id.llSpOffice);


        clintid=getIntent().getStringExtra("clientid");
        pref.saveFClintId(clintid);
        clientname=getIntent().getStringExtra("clientname");
        pref.saveClintOfficeName(clientname);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        llSkip=(LinearLayout)findViewById(R.id.llSkip);
        connectionCheck=new NetworkConnectionCheck(ClintOfficeActivity.this);
        if (pref.getDomainId().equals("ITR")||pref.getDomainId().equals("FSSR")||pref.getDomainId().equals("PSS")){
            setofficeForRMS();
        }else {
            setoffice();
        }

    }

    private void onClick(){
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        etOffice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etOffice.getText().toString().length()>0){
                     llSpOffice.setVisibility(View.GONE);

                     pref.saveOfficeName(etOffice.getText().toString());
                     pref.saveFOfficeId("0");
                }else {
                    llSpOffice.setVisibility(View.VISIBLE);

                }

            }
        });
        spOffice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pref.saveOfficeId("");
                pref.saveOfficeName("");
                if (position>0) {
                    pref.saveFOfficeId(modelOfficeList.get(position).getItemId());
                    pref.saveOfficeName(modelOfficeList.get(position).getItemName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pref.getOfficeName().equals("")) {
                    Intent intent = new Intent(ClintOfficeActivity.this, ContactPersonActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Please select or enter office name",Toast.LENGTH_LONG).show();
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),FeedBackDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        llSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClintOfficeActivity.this, ContactPersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                pref.saveFOfficeId("0");
                pref.saveOfficeName("0");

            }
        });
    }


    private void setoffice() {

        String surl = "http://111.93.182.174/GeniusiOSApi/api//gcl_CommonDDL?ddltype=14001&id1="+pref.getEmpConId()+"&id3="+pref.getFeedBackMasterId()+"&id2="+clintid+"&SecurityCode="+pref.getDomainId();
        Log.d("officeapi",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                        officeList.clear();
                        modelOfficeList.clear();
                        officeList.add("Please select Client Office");
                        modelOfficeList.add(new DeptSpinnerModel("0", "0"));

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            // String responseText = job1.optString("responseText");
                            boolean ResponseStatus=job1.optBoolean("responseStatus");
                            if (ResponseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("value");
                                    String id = obj.optString("id");
                                    officeList.add(value);
                                    DeptSpinnerModel deptmodel=new DeptSpinnerModel(id,value);
                                    modelOfficeList.add(deptmodel);

                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ClintOfficeActivity.this, android.R.layout.simple_spinner_item,
                                                officeList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spOffice.setAdapter(spinnerArrayAdapter);


                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ClintOfficeActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                internetAlert();

                //Toast.makeText(ClintOfficeActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue= Volley.newRequestQueue(ClintOfficeActivity.this);
        requestQueue.add(stringRequest);


    }

    private void setofficeForRMS() {

        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/GetRMSClientOfficeDe?ClientID="+clintid+"&UserName="+pref.getFeedbackEmpId()+"&SourceType="+pref.getDomainId();
        Log.d("officeapi",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.GONE);
                        officeList.clear();
                        modelOfficeList.clear();
                        officeList.add("Please select Client Office");
                        modelOfficeList.add(new DeptSpinnerModel("0", "0"));

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            // String responseText = job1.optString("responseText");
                            int ResponseCode=job1.optInt("ResponseCode");
                            if (ResponseCode==1) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("ResponseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("RMSClientOfficeIDName");
                                    String id = obj.optString("RMSClientOfficeID");
                                    officeList.add(value);
                                    DeptSpinnerModel deptmodel=new DeptSpinnerModel(id,value);
                                    modelOfficeList.add(deptmodel);

                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (ClintOfficeActivity.this, android.R.layout.simple_spinner_item,
                                                officeList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spOffice.setAdapter(spinnerArrayAdapter);


                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ClintOfficeActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                internetAlert();

                //Toast.makeText(ClintOfficeActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {};

        RequestQueue requestQueue= Volley.newRequestQueue(ClintOfficeActivity.this);
        requestQueue.add(stringRequest);
    }

    private void internetAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClintOfficeActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.internetconnectiondialog, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        if (connectionCheck.isNetworkAvailable()){
            alertDialog.dismiss();
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }
}
