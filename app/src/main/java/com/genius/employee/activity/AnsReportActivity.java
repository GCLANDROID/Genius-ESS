package com.genius.employee.activity;

import android.os.Bundle;
/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView*/;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.adapter.AnsReportAdapter;
import com.genius.employee.model.AnsReportModule;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnsReportActivity extends AppCompatActivity {
    ArrayList<AnsReportModule> itemList = new ArrayList();
    RecyclerView rvItem;
    Pref pref;
    LinearLayout llLoader, llMain, llNoData;
    int MY_SOCKET_TIMEOUT_MS=180000;
    String grpId;
    ImageView imgcancel;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ans_report);
        this.setFinishOnTouchOutside(false);
        initialize();
        if (pref.getDomainId().equals("PSS")||pref.getDomainId().equals("FSSR")||pref.getDomainId().equals("ITR")){
            getReportListForRms();
        }else {
            getReportList();
        }
        onClick();
    }

    private void initialize(){
        pref = new Pref(AnsReportActivity.this);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(AnsReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        grpId=getIntent().getStringExtra("grpId");
        imgcancel=(ImageView)findViewById(R.id.imgcancel);
    }


    private void getReportList() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/get_ClientSurveyReport?GroupAnswerID="+grpId+"&AEMConsultantId="+pref.getEmpConId()+"&AEMClientId=0&BranchId=0&AEMClientOfficeId=0&FinancialYear=0&Month=0&UserId="+pref.getFeedbackEmpId()+"&Operation=3&SecurityCode="+pref.getDomainId();
        Log.d("ansurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Question=obj.optString("Question");
                                    String Rate=obj.optString("Rate");

                                    AnsReportModule cmodule = new AnsReportModule(Question,Rate);
                                    itemList.add(cmodule);

                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                rvItem.setVisibility(View.VISIBLE);
                                AnsReportAdapter cAdapter = new AnsReportAdapter(itemList);
                                rvItem.setAdapter(cAdapter);


                            } else {

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                          //  Toast.makeText(AnsReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void getReportListForRms() {
        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/GetClientFeedbackAnswerDe?GroupAnswerID="+grpId+"&SourceType="+pref.getDomainId();
        Log.d("ansurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            int ResponseCode = job1.optInt("ResponseCode");
                            if (ResponseCode==1) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("ResponseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String Question=obj.optString("RMSQuestion");
                                    String Rate=obj.optString("Rate");

                                    AnsReportModule cmodule = new AnsReportModule(Question,Rate);
                                    itemList.add(cmodule);

                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                rvItem.setVisibility(View.VISIBLE);
                                AnsReportAdapter cAdapter = new AnsReportAdapter(itemList);
                                rvItem.setAdapter(cAdapter);


                            } else {

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(AnsReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void onClick(){
        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
