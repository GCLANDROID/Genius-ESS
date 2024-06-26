package com.genius.employee.activity;

import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.adapter.HolidayAdapter;
import com.genius.employee.model.HolidayModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HolidayActivity extends AppCompatActivity {
    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    ArrayList<HolidayModel> itemList = new ArrayList();
    ImageView imgBack, imgHome;
    String year;
    Pref pref;
    String HolidayDate;
    TextView tvToolBar,tvRemaning;
    ArrayList<String>remaninfList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday);
        initView();
        getItem();
        onClick();
    }

    private void initView() {
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        pref=new Pref(HolidayActivity.this);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        int y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        tvToolBar=(TextView) findViewById(R.id.tvToolBar);
        tvRemaning=(TextView)findViewById(R.id.tvRemaning);
        tvToolBar.setText("Holiday List - "+year);
    }


    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);

        String surl = APi.sGetHolidayApi+"CompanyID=1090000001&BranchID="+pref.getBranchId()+"&Year="+year;
        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        itemList.clear();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i =0; i <responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String HolidayName = obj.optString("HolidayName");
                                    String HolidayDayName = obj.optString("HolidayDayName");
                                    try {
                                        HolidayDate = Util.changeAnyDateFormat(obj.optString("HolidayDate"),"dd-MMM-yyyy","dd \nMMM");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String IsColor=obj.optString("IsColor");

                                    HolidayModel aModel = new HolidayModel(HolidayName, HolidayDate, HolidayDayName);
                                    aModel.setIsColor(IsColor);
                                    aModel.setCount(i+1);
                                    itemList.add(aModel);
                                    if (IsColor.equalsIgnoreCase("1")){
                                        remaninfList.add(HolidayDate);
                                    }


                                }
                                int remaning=itemList.size()-remaninfList.size();

                                tvRemaning.setText(remaning+" holiday(s) left out of "+itemList.size());


                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                llNoData.setVisibility(View.GONE);

                                HolidayAdapter hAdapter=new HolidayAdapter(itemList);
                                rvItem.setAdapter(hAdapter);


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
                Toast.makeText(HolidayActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                Intent intent=new Intent(HolidayActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
