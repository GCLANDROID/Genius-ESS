package com.genius.employee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/*
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
*/
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.adapter.FeedBackReportAdapter;
import com.genius.employee.model.DeptSpinnerModel;
import com.genius.employee.model.FeedBackReportModule;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class FeedBackreportActivity extends AppCompatActivity {
    ArrayList<FeedBackReportModule> itemList = new ArrayList();
    RecyclerView rvItem;
    Pref pref;
    ImageView imgBack, imgHome;
    LinearLayout llLoader, llMain, llNoData;
    AlertDialog alert1;
    int MY_SOCKET_TIMEOUT_MS = 180000;
    ImageView imgSearch;
    Button btnSearch;
    Spinner spClintName, spYear, spMonth;
    ArrayList<String> year = new ArrayList();
    String pastyear, cuyear, futyear;
    ArrayList<String>month=new ArrayList();
    ArrayList<DeptSpinnerModel> modelClintList=new ArrayList();
    ArrayList<String>clintList=new ArrayList();
    LinearLayout llSearch;
    int flag;
    String clintId="0";
    String finYear="0";
    String cuumonth="0";
    String getMonth;
    String financialYear;
    TextView tvClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_backreport);
        initialize();
        if (pref.getDomainId().equals("PSS")||pref.getDomainId().equals("FSSR")||pref.getDomainId().equals("ITR")){
            getReportListForRrms();
            spClintName.setVisibility(View.GONE);
            tvClient.setVisibility(View.GONE);
        }else {
            setoffice();
            spClintName.setVisibility(View.VISIBLE);
            tvClient.setVisibility(View.VISIBLE);
        }
        onClick();
    }

    private void initialize() {
        pref = new Pref(FeedBackreportActivity.this);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(FeedBackreportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        spClintName = (Spinner) findViewById(R.id.spClintName);
        spYear = (Spinner) findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);
        int y = Calendar.getInstance().get(Calendar.YEAR);


        Calendar c = Calendar.getInstance();
        int cuumonth = c.get(Calendar.MONTH)+1;
        Log.d("cuumonth", String.valueOf(cuumonth));
        if (cuumonth==1){
            getMonth="January";
        }else  if (cuumonth==2){
            getMonth="February";
        }else  if (cuumonth==3){
            getMonth="March";
        }else  if (cuumonth==4){
            getMonth="April";
        }else  if (cuumonth==5){
            getMonth="May";
        }else  if (cuumonth==6){
            getMonth="June";
        }else  if (cuumonth==7){
            getMonth="July";
        }else  if (cuumonth==8){
            getMonth="August";
        }else  if (cuumonth==9){
            getMonth="September";
        }else  if (cuumonth==10){
            getMonth="October";
        }else  if (cuumonth==11){
            getMonth="November";
        }else  if (cuumonth==12){
            getMonth="December";
        }


        cuyear = String.valueOf(y);
        int pasty=y-1;
        int futy=y+1;
        String curyear= String.valueOf(y);
        String poastyear= String.valueOf(pasty);
        String futyear= String.valueOf(futy);
        String cuufin=curyear+"-"+futyear;
        String pastfin=poastyear+"-"+curyear;
        year.add("ALL");
        year.add(cuufin);
        year.add(pastfin);


        if(getMonth.equals("January")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+curyear;
        }else if (getMonth.equals("February")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+curyear;
        }else if (getMonth.equals("March")){
            int futureyear = y - 1;
            financialYear = futureyear+"-"+curyear;
        }else {
            int futureyear = y + 1;
            financialYear = curyear+"-"+futureyear;
        }
        Log.d("financialYear",financialYear);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int inydex = dataAdapter.getPosition(financialYear);
        spYear.setAdapter(dataAdapter);
        spYear.setSelection(inydex);




        month.add("ALL");
        month.add("January");
        month.add("February");
        month.add("March");
        month.add("April");
        month.add("May");
        month.add("June");
        month.add("July");
        month.add("August");
        month.add("September");
        month.add("October");
        month.add("November");
        month.add("December");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = dataAdapter1.getPosition(getMonth);
        spMonth.setAdapter(dataAdapter1);
        spMonth.setSelection(index);

        llSearch=(LinearLayout)findViewById(R.id.llSearch);
        tvClient=(TextView)findViewById(R.id.tvClient);



    }

    private void setoffice() {

        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_CommonDDL?ddltype=12&id1=" + pref.getEmpConId() + "&id2=" + pref.getMasterId() + "&id3=0&SecurityCode=" + pref.getDomainId();
        Log.d("officeapi",surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        llLoader.setVisibility(View.VISIBLE);
                        llMain.setVisibility(View.GONE);
                        llNoData.setVisibility(View.GONE);
                        llSearch.setVisibility(View.GONE);
                        clintList.clear();
                        modelClintList.clear();
                        clintList.add("ALL");
                        modelClintList.add(new DeptSpinnerModel("0", "0"));

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
                                    clintList.add(value);
                                    DeptSpinnerModel deptmodel=new DeptSpinnerModel(id,value);
                                    modelClintList.add(deptmodel);

                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (FeedBackreportActivity.this, android.R.layout.simple_spinner_item,
                                                clintList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spClintName.setAdapter(spinnerArrayAdapter);
                                getReportList();


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedBackreportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //Toast.makeText(ClintOfficeActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
                flag=1;
                intertalert();
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void getReportList() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/get_ClientSurveyReport?GroupAnswerID=0&AEMConsultantId=" + pref.getEmpConId() + "&AEMClientId="+clintId+"&BranchId=0&AEMClientOfficeId=0&FinancialYear="+finYear+"&Month="+cuumonth+"&UserId=" + pref.getFeedbackEmpId() + "&Operation=2&SecurityCode=" + pref.getDomainId();
        Log.d("clintname", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
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
                                    String CreatedOn = obj.optString("CreatedOn");
                                    String CreatedTime = obj.optString("CreatedTime");
                                    String ClientName = obj.optString("ClientName");
                                    String Office = obj.optString("Office");
                                    String CPName = obj.optString("CPName");
                                    String CPDesignation = obj.optString("CPDesignation");
                                    String CPEmailId = obj.optString("CPEmailId");
                                    String CPPhone = obj.optString("CPPhone");
                                    String Address = obj.optString("Address");
                                    String GroupAnswerID = obj.optString("GroupAnswerID");
                                    String Remarks = obj.optString("Remarks");


                                    FeedBackReportModule cmodule = new FeedBackReportModule(CreatedOn, CreatedTime, ClientName, Office, CPName, CPDesignation, CPEmailId, CPPhone, Address, GroupAnswerID, Remarks);
                                    itemList.add(cmodule);

                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                llSearch.setVisibility(View.GONE);
                                rvItem.setVisibility(View.VISIBLE);
                                FeedBackReportAdapter cAdapter = new FeedBackReportAdapter(itemList, FeedBackreportActivity.this);
                                rvItem.setAdapter(cAdapter);


                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                llSearch.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedBackreportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                flag=2;
                intertalert();

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

    private void getReportListForRrms() {
        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/GetClientFeedbackDe?FinancialYear="+finYear+"&Month="+cuumonth+"&UserName="+pref.getFeedbackEmpId()+"&SourceType="+pref.getDomainId();
        Log.d("report", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
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
                                    String CreatedOn = obj.optString("CreatedOn");
                                    String CreatedTime = obj.optString("CreatedTime");
                                    String ClientName = obj.optString("ClientName");
                                    String Office = obj.optString("Office");
                                    String CPName = obj.optString("RMSCPName");
                                    String CPDesignation = obj.optString("RMSCPDesignation");
                                    String CPEmailId = obj.optString("RMSCPEmailId");
                                    String CPPhone = obj.optString("RMSCPPhone");
                                    String Address = obj.optString("Address");
                                    String GroupAnswerID = obj.optString("GroupAnswerID");
                                    String Remarks = obj.optString("Remarks");


                                    FeedBackReportModule cmodule = new FeedBackReportModule(CreatedOn, CreatedTime, ClientName, Office, CPName, CPDesignation, CPEmailId, CPPhone, Address, GroupAnswerID, Remarks);
                                    itemList.add(cmodule);

                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                llSearch.setVisibility(View.GONE);
                                rvItem.setVisibility(View.VISIBLE);
                                FeedBackReportAdapter cAdapter = new FeedBackReportAdapter(itemList, FeedBackreportActivity.this);
                                rvItem.setAdapter(cAdapter);
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                llSearch.setVisibility(View.GONE);

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedBackreportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                flag=2;
                intertalert();

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

    private void intertalert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FeedBackreportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.internetconnectiondialog, null);
        dialogBuilder.setView(dialogView);

        alert1 = dialogBuilder.create();
        alert1.setCancelable(false);
        Window window = alert1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alert1.show();
    }

    private void onClick() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedBackreportActivity.this, FeedBackDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llSearch.setVisibility(View.VISIBLE);
            }
        });

        spClintName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clintId=modelClintList.get(position).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    finYear = year.get(position);
                }else {
                    finYear="0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    cuumonth = month.get(position);
                }else {
                    cuumonth="0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pref.getDomainId().equals("PSS")||pref.getDomainId().equals("FSSR")||pref.getDomainId().equals("ITR")){
                    getReportListForRrms();

                }else {
                    getReportList();

                }
            }
        });
    }

}
