package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.genius.employee.adapter.TrackMapListAdapter;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.model.TrackingDetailsModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrackingReportActivity extends AppCompatActivity {

    private static final String TAG = "MyApps";
    ImageView imgBack, imgHome;
    LinearLayout llNoData;
    String EmpId;
    Spinner spYear, spMonth;
    LinearLayout llLoader, llMain;
    String parsedDistance;
    Date date2,date1;


    ArrayList<TrackingDetailsModel> trackingList = new ArrayList();


    ArrayList<SpinnerModel> modelYearList = new ArrayList();
    ArrayList<String> yearList = new ArrayList();

    ArrayList<SpinnerModel> modelMonthList = new ArrayList();
    ArrayList<String> monthList = new ArrayList();

    int MY_SOCKET_TIMEOUT_MS = 60000;

    String yearid = "";
    String monthId = "0";
    String responseID;
    int MonthId;
    LinearLayout llGLogo;
    LinearLayout llCoonection;
    LinearLayout llShow;
    String url;
    RecyclerView rvMap;
    String entryMarkLat, entryMarkLon, entryAddress, outMarkAddress, outMarkLat, outMarkLon, registeredOn, enteredOn;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_report);


        initialize();
        setYearItem();
        onClick();


    }


    private void initialize() {
        pref=new Pref(TrackingReportActivity.this);

        EmpId = getIntent().getStringExtra("empId");
        Log.d(TAG, "EmployeeId:" + EmpId);


        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        spYear = (Spinner) findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llGLogo = (LinearLayout) findViewById(R.id.llGLogo);
        llCoonection = (LinearLayout) findViewById(R.id.llCoonection);
        llShow = (LinearLayout) findViewById(R.id.llShow);
        rvMap = (RecyclerView) findViewById(R.id.rvMap);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(TrackingReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvMap.setHasFixedSize(true);
        rvMap.setLayoutManager(layoutManager);


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
                Intent intent = new Intent(TrackingReportActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearid = modelYearList.get(position).getItemId();
                modelMonthList.clear();
                monthList.clear();
                setMonthItem(yearid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                monthId = modelMonthList.get(position).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        llShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!yearid.equals("")) {
                    if (!monthId.equals("")) {
                        getItem1();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please select month", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select year", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void getItem1() {

        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llGLogo.setVisibility(View.GONE);
        llCoonection.setVisibility(View.GONE);

        url = APi.sUrl+"ClientVisit/GetClientVisitReportByMonth?EmployeeId=" + EmpId + "&FinYear=" + yearid + "&monthID=" + monthId;
        Log.d("printurl", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        trackingList.clear();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    entryMarkLat = obj.optString("ENTRY_MARK_LAT");
                                    entryMarkLon = obj.optString("ENTRY_MARK_LON");
                                    entryAddress = obj.optString("ENTRY_ADDRESS");
                                    outMarkAddress = obj.optString("OutMarkAddress");
                                    outMarkLat = obj.optString("OutMarkLat");
                                    outMarkLon = obj.optString("OutMarkLon");
                                    registeredOn = obj.optString("RegisteredOn");
                                    enteredOn = obj.optString("EnteredOn");

                                    TrackingDetailsModel aModel = new TrackingDetailsModel(entryMarkLat, entryMarkLon, entryAddress, outMarkAddress, outMarkLat, outMarkLon, registeredOn, enteredOn);
                                    trackingList.add(aModel);


                                }


                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);

                                llNoData.setVisibility(View.GONE);
                                llGLogo.setVisibility(View.GONE);
                                llCoonection.setVisibility(View.GONE);
                                setAdapter();


                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);

                                llNoData.setVisibility(View.VISIBLE);
                                llGLogo.setVisibility(View.GONE);
                                llCoonection.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);

                llNoData.setVisibility(View.GONE);
                llGLogo.setVisibility(View.GONE);
                llCoonection.setVisibility(View.VISIBLE);
                Toast.makeText(TrackingReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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


    private void setAdapter() {

        TrackMapListAdapter trackMapListAdapter = new TrackMapListAdapter(trackingList, TrackingReportActivity.this);
        rvMap.setAdapter(trackMapListAdapter);

    }


    private void setYearItem() {

        String surl = APi.sGetFinancialYearApi;
        Log.d("compurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llGLogo.setVisibility(View.GONE);
        llCoonection.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        llLoader.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        llNoData.setVisibility(View.GONE);
                        llGLogo.setVisibility(View.VISIBLE);
                        llCoonection.setVisibility(View.GONE);
                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            responseID = job1.optString("responseID");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String YearName = obj.optString("YearName");
                                    yearid = obj.optString("YearID");
                                    yearList.add(YearName);
                                    SpinnerModel mainDocModule = new SpinnerModel(YearName, yearid);
                                    modelYearList.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TrackingReportActivity.this, android.R.layout.simple_spinner_item,
                                                yearList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                int index = spinnerArrayAdapter.getPosition(responseID);
                                Log.d("indexr", String.valueOf(index));
                                spYear.setAdapter(spinnerArrayAdapter);
                                spYear.setSelection(index);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TrackingReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llGLogo.setVisibility(View.GONE);
                llCoonection.setVisibility(View.GONE);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
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


    }


    private void setMonthItem(String yid) {

        String surl = APi.sGetAttendanceMonthApi+"YearID=" + yid;
        Log.d("compurl", surl);
        final ProgressDialog progressDialog = new ProgressDialog(TrackingReportActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        progressDialog.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {



                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String MonthName = obj.optString("MonthName");
                                    String MonthID = obj.optString("MonthID");
                                    monthList.add(MonthName);
                                    SpinnerModel mainDocModule = new SpinnerModel(MonthName, MonthID);
                                    modelMonthList.add(mainDocModule);

                                }
                                spMonth.setVisibility(View.VISIBLE);
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (TrackingReportActivity.this, android.R.layout.simple_spinner_item,
                                                monthList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spMonth.setSelection(0);
                                spMonth.setAdapter(spinnerArrayAdapter);


                            } else {
                                spMonth.setVisibility(View.GONE);
                                monthList.clear();
                                modelMonthList.clear();


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TrackingReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
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


    }

    public String timeget(int pos) {
        String time = null;
        String menu = trackingList.get(pos).getRegisteredOn();
        String[] separated = menu.split(" ");

        String s1 = separated[0];
        String s2 = separated[1];
        String s3=separated[2];
        time=s1+"-"+s2+"-"+"-"+s3;

        return time;
    }





}
