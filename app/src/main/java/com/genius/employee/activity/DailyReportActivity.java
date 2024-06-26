package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
/*import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.genius.employee.adapter.DailyActivityReportAdapter;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.DailyReportModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DailyReportActivity extends AppCompatActivity {
    ArrayList<DailyReportModel> reportList = new ArrayList();
    RecyclerView rvItem;
    Pref pref;
    Spinner spYear, spMonth;
    ArrayList<SpinnerModel> modelYearList = new ArrayList();
    ArrayList<String> yearList = new ArrayList();
    String yearid = "";
    String monthId = "0";
    AlertDialog alertDialog;
    ArrayList<SpinnerModel> modelMonthList = new ArrayList();
    ArrayList<String> monthList = new ArrayList();
    int MY_SOCKET_TIMEOUT_MS = 60000;
    LinearLayout llSearch;
    LinearLayout llNoData;
    TextView tvWeb;
    String image_url;
    LinearLayout llWEB;
    String imgurl;
    ImageView imgBack, imgHome;
    LinearLayout llGLogo;
    LinearLayout llCoonection;
    LinearLayout llNoMonth, llMonth;
    ImageView imgSearch;
    LinearLayout llShow;
    String responseID;
    int MonthId;
    LinearLayout llLoader, llMain;
    int month;
    String formattedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        init();
        onClick();


    }

    private void init() {
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(DailyReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(layoutManager);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        tvWeb = (TextView) findViewById(R.id.tvWeb);
        llWEB = (LinearLayout) findViewById(R.id.llWEB);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        llGLogo = (LinearLayout) findViewById(R.id.llGLogo);
        llCoonection = (LinearLayout) findViewById(R.id.llCoonection);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        spYear = (Spinner) findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);
        llMonth = (LinearLayout) findViewById(R.id.llMonth);
        llNoMonth = (LinearLayout) findViewById(R.id.llNoMonth);
        llShow = (LinearLayout) findViewById(R.id.llShow);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
         month = c.get(Calendar.MONTH)+1;
        Log.d("month", String.valueOf(month));

        formattedDate = getIntent().getStringExtra("attdate").replaceAll("\\s+", "%20");
        getItem();
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
                Intent intent = new Intent(DailyReportActivity.this, EDashBoardActivity.class);
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0){
                    monthId="1";
                }else if (position==1){
                    monthId="2";
                }
                else if (position==2){
                    monthId="3";
                }
                else if (position==3){
                    monthId="4";
                }
                else if (position==4){
                    monthId="5";
                }
                else if (position==5){
                    monthId="6";
                }
                else if (position==6){
                    monthId="7";
                }
                else if (position==7){
                    monthId="8";
                }
                else if (position==8){
                    monthId="9";
                }
                else if (position==9){
                    monthId="10";
                }
                else if (position==10){
                    monthId="11";
                }
                else if (position==11){
                    monthId="12";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llGLogo.setVisibility(View.GONE);
        llCoonection.setVisibility(View.GONE);

        String surl = APi.sGetClientVisitReportByDateApi+"EmployeeId="+pref.getSecureEmpId()+"&logDate="+formattedDate;
        Log.d("docreporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        reportList.clear();

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
                                    String ClientName = obj.optString("ClientName");
                                    String ClientContactPerson = obj.optString("ClientContactPerson");
                                    String STATUS = obj.optString("STATUS");
                                    String RegisterRemark = obj.optString("RegisterRemark");
                                    String ENTRY_MARK_LAT = obj.optString("ENTRY_MARK_LAT");
                                    String ENTRY_MARK_LON = obj.optString("ENTRY_MARK_LON");
                                    String ENTRY_ADDRESS = obj.optString("ENTRY_ADDRESS");
                                    String VisitPurposeName = obj.optString("VisitPurposeName");
                                    String OutMarkAddress = obj.optString("OutMarkAddress");
                                    String OutMarkLat = obj.optString("OutMarkLat");
                                    String OutMarkLon = obj.optString("OutMarkLon");
                                    String ContactNumber = obj.optString("ContactNumber");
                                    String ContactEmail = obj.optString("ContactEmail");
                                    String RegisteredOn = obj.optString("RegisteredOn");
                                    String EnteredOn = obj.optString("EnteredOn");
                                    String OutMarkedOn = obj.optString("OutMarkedOn");

                                    DailyReportModel aModel = new DailyReportModel(RegisteredOn, ClientName, EnteredOn, ENTRY_ADDRESS, OutMarkedOn, OutMarkAddress, RegisterRemark, RegisteredOn, STATUS, ENTRY_MARK_LAT, ENTRY_MARK_LON, OutMarkLat, OutMarkLon, ",", ClientContactPerson, VisitPurposeName, ContactNumber, ContactEmail, "", "");
                                    reportList.add(aModel);


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
                Toast.makeText(DailyReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public double distance(int i) {
        LatLng src = new LatLng(Double.parseDouble(reportList.get(i).getReglat()), Double.parseDouble(reportList.get(i).getRegLong()));
        LatLng des = new LatLng(Double.parseDouble(reportList.get(i).getInLat()), Double.parseDouble(reportList.get(i).getInLong()));
        double distance = CalculationByDistance(src, des);
        return distance;

    }

    public static double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        //  Toast.makeText(getApplicationContext(),"Radious Value:  "+valueResult+"  KM: "+kmInDec+" Meter: "+meterInDec,Toast.LENGTH_LONG).show();
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        return Radius * c;
    }

    private void setAdapter() {
        DailyActivityReportAdapter dAdapter = new DailyActivityReportAdapter(reportList, DailyReportActivity.this);
        rvItem.setAdapter(dAdapter);
    }


}
