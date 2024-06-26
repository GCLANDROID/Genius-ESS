package com.genius.employee.activity;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.adapter.MarkReportAdapter;
import com.genius.employee.adapter.MarkViewAdapter;
import com.genius.employee.model.MarkInViewModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MarkInReportActivity extends AppCompatActivity {
    LinearLayout llLoader,llMain,llNoData;
    RecyclerView rvItem;
    Spinner spYear,spMonth;
    ArrayList<String>yearList=new ArrayList();
    ArrayList<String>monthList=new ArrayList();
    Button btnShow;
    String year="";
    String month="";
    int iMonth;
    Pref pref;
    ArrayList<MarkInViewModel>itemList=new ArrayList();
    ImageView imgBack,imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_in_report);
        initView();
        onClick();
    }

    private void initView(){
        pref=new Pref(MarkInReportActivity.this);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String y= String.valueOf(year);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));
        spYear=(Spinner)findViewById(R.id.spYear);
        spMonth=(Spinner)findViewById(R.id.spMonth);

        yearList.add("2020");
        yearList.add("2021");
        yearList.add("2022");
        yearList.add("2023");
        yearList.add("2024");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (MarkInReportActivity.this, android.R.layout.simple_spinner_item,
                        yearList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = spinnerArrayAdapter.getPosition(y);

        spYear.setAdapter(spinnerArrayAdapter);
        spYear.setSelection(index);

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        Log.d("month_name",month_name);


        monthList.add("Please select");
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        ArrayAdapter<String> spinnerMonthArrayAdapter = new ArrayAdapter<String>
                (MarkInReportActivity.this, android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int mindex = spinnerMonthArrayAdapter.getPosition(month_name);
        spMonth.setAdapter(spinnerMonthArrayAdapter);
        spMonth.setSelection(mindex);

        btnShow=(Button)findViewById(R.id.btnShow);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);




    }

    private void onClick(){
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    year=yearList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    month=monthList.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!year.equals("")) {
                    if (!month.equals("")) {
                        getItem();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please select month",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please select Year",Toast.LENGTH_LONG).show();
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
                Intent intent=new Intent(MarkInReportActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void  getItem(){

        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = APi.sGetOfflineDailyLogActivityApi+"AEMEmployeeID="+pref.getSecureEmpId()+"&Year="+year+"&Month="+month+"&AttendanceDate=0&Operation=6";
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        itemList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);

                                    String PunchInTime = obj.optString("PunchInTime");
                                    String AddressIN = obj.optString("AddressIN");
                                    String RemarksIN=obj.optString("RemarksIN");
                                    String FNameIN=obj.optString("FNameIN");
                                    String imgUrlIN=obj.optString("imgUrlIN");
                                    String date=obj.optString("PunchOut");
                                    String PunchOutTime=obj.optString("PunchOutTime");

                                    MarkInViewModel obj2 = new MarkInViewModel(AddressIN,PunchInTime,imgUrlIN,FNameIN,RemarksIN,date);
                                    obj2.setPunchOutTime(PunchOutTime);;
                                    obj2.setFlag("1");
                                    itemList.add(obj2);


                                }
                                setAdapter();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);

                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MarkInReportActivity.this);
        requestQueue.add(stringRequest);
    }
    private void setAdapter(){
        MarkReportAdapter vAdapter=new MarkReportAdapter(itemList,getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }
}
