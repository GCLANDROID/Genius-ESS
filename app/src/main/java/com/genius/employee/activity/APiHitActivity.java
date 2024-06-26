package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.model.VisitingLocationModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APiHitActivity extends AppCompatActivity {
    Pref pref;
    ArrayList<VisitingLocationModel>addressList=new ArrayList();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_hit);
        pref=new Pref(getApplicationContext());
        String formattedDate = getIntent().getStringExtra("attdate").replaceAll("\\s+", "%20");
        loadNames(formattedDate);
    }


    private void loadNames(String date) {
        //names.clear();
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        String surl = APi.sUrl+"ClientVisit/GetClientVisitReportByDate?EmployeeId="+pref.getEmpId()+"&logDate="+date;
        Log.d("mapactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        pd.dismiss();

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

                                    String EnteredOn = obj.optString("EnteredOn");
                                    String ENTRY_ADDRESS = obj.optString("ENTRY_ADDRESS");
                                    String ENTRY_MARK_LON=obj.optString("ENTRY_MARK_LON");
                                    String ENTRY_MARK_LAT=obj.optString("ENTRY_MARK_LAT");

                                    VisitingLocationModel obj2 = new VisitingLocationModel(ENTRY_ADDRESS,EnteredOn,ENTRY_MARK_LAT,ENTRY_MARK_LON);
                                    addressList.add(obj2);


                                }
                                ArrayList<String>myList=new ArrayList();
                                Intent intent=new Intent(APiHitActivity.this,MapReportActivity.class);
                                intent.putExtra("myList",addressList);
                                startActivity(intent);
                                finish();


                            } else {

                                pd.show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.show();


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(APiHitActivity.this);
        requestQueue.add(stringRequest);
    }
}
