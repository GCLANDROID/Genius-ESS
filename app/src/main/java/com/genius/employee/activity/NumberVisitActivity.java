package com.genius.employee.activity;

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.adapter.NumberTourAdapter;
import com.genius.employee.model.DailyReportModel;
import com.genius.employee.model.NumberTourModel;
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

public class NumberVisitActivity extends AppCompatActivity {
    ArrayList<NumberTourModel> itemList = new ArrayList();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_visit);
        init();
        setYearItem();
        onClick();
    }



    private void init() {
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(NumberVisitActivity.this, LinearLayoutManager.VERTICAL, false);
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
        imgSearch=(ImageView)findViewById(R.id.imgSearch);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        Log.d("month", String.valueOf(month));
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
                Intent intent = new Intent(NumberVisitActivity.this, EDashBoardActivity.class);
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
                llGLogo.setVisibility(View.VISIBLE);
                llCoonection.setVisibility(View.GONE);
            }
        });


        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearid = modelYearList.get(position).getItemId();
                modelMonthList.clear();
                monthList.clear();
                setMonthItem();
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

        llShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!yearid.equals("")) {
                    if (!monthId.equals("")) {
                        getItem();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please select month", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please select year", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llGLogo.setVisibility(View.GONE);
        llCoonection.setVisibility(View.GONE);

        String surl = APi.sGetClientTourViewApi+"EmployeeId=" + pref.getSecureEmpId() + "&FinYearID=" + yearid + "&Month=" + monthId;
        Log.d("docreporturl", surl);
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
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String LOG_DATE=obj.optString("LOG_DATE");
                                    String NumberOfVisit=obj.optString("NumberOfVisit");
                                    String PunchLat=obj.optString("PunchLat");
                                    String PunchLon=obj.optString("PunchLon");
                                    String OutMarkLat=obj.optString("OutMarkLat");
                                    String OutMarkLon=obj.optString("OutMarkLon");
                                    NumberTourModel aModel = new NumberTourModel(LOG_DATE,NumberOfVisit,PunchLat,PunchLon,OutMarkLat,OutMarkLon);
                                    itemList.add(aModel);


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
                Toast.makeText(NumberVisitActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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
    private void setAdapter(){
        NumberTourAdapter vAdapter=new NumberTourAdapter(itemList,NumberVisitActivity.this);
        rvItem.setAdapter(vAdapter);
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
                                        (NumberVisitActivity.this, android.R.layout.simple_spinner_item,
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
                            Toast.makeText(NumberVisitActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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


    private void setMonthItem() {

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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (NumberVisitActivity.this, android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(spinnerArrayAdapter);
        if (month==1) {
            spMonth.setSelection(0);
        }else if (month==2){
            spMonth.setSelection(1);
        }
        else if (month==3){
            spMonth.setSelection(2);
        }
        else if (month==4){
            spMonth.setSelection(3);
        }
        else if (month==5){
            spMonth.setSelection(4);
        }
        else if (month==6){
            spMonth.setSelection(5);
        }

        else if (month==7){
            spMonth.setSelection(6);
        }
        else if (month==8){
            spMonth.setSelection(7);
        }
        else if (month==9){
            spMonth.setSelection(8);
        }
        else if (month==10){
            spMonth.setSelection(9);
        }
        else if (month==11){
            spMonth.setSelection(10);
        }
        else if (month==12){
            spMonth.setSelection(11);
        }


    }

    public  double distance(int i){
        LatLng src=new LatLng(Double.parseDouble(itemList.get(i).getMinlat()),Double.parseDouble(itemList.get(i).getMinlong()));
        LatLng des=new LatLng(Double.parseDouble(itemList.get(i).getMaxlat()),Double.parseDouble(itemList.get(i).getMaxlong()));
        double distance=CalculationByDistance(src,des);


        return  distance;

    }

    public  static double CalculationByDistance(LatLng StartP, LatLng EndP) {
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
}
