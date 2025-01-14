package com.genius.employee.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.genius.employee.adapter.MarkReportAdapter;
import com.genius.employee.adapter.TeamMarkReportAdapter;
import com.genius.employee.model.MarkInViewModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WFHTeamReportActivity extends AppCompatActivity {
    TextView tvDate;
    int cuMonth;
    String cuuDate;
    ImageView imgCalender;

    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    String empId;
    ArrayList<MarkInViewModel> itemList = new ArrayList();
    TextView tvMonthWise,tvDateWise;
    LinearLayout llDate, llShow;
    Spinner spYear, spMonth;
    ArrayList<String> yearList = new ArrayList();
    ArrayList<SpinnerModel> modelYearList = new ArrayList();
    String month_name;
    ArrayList<String> monthList = new ArrayList();
    String responseID;

    String year;
    String monthname;
    Button btnShow;

    ImageView imBack,imgHome;
    TextView tvEmpName;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wfhteam_report);
        initView();
        onClick();
    }

    private void initView() {
        tvDate = (TextView) findViewById(R.id.tvDate);
        pref=new Pref(WFHTeamReportActivity.this);
        String formaDate =getIntent().getStringExtra("dDate");
        String[] sep = formaDate.split("-");
        String month = sep[1];
        String day = sep[0];
        String year = sep[2];
        if (month.contains("Jan")) {
            cuMonth = 1;
        } else if (month.contains("Feb")) {
            cuMonth = 2;
        } else if (month.contains("Mar")) {
            cuMonth = 3;
        } else if (month.contains("Apr")) {
            cuMonth = 4;
        } else if (month.contains("May")) {
            cuMonth = 5;
        } else if (month.contains("Jun")) {
            cuMonth = 6;
        } else if (month.contains("Jul")) {
            cuMonth = 7;
        } else if (month.contains("Aug")) {
            cuMonth = 8;
        } else if (month.contains("Sep")) {
            cuMonth = 9;
        } else if (month.contains("Oct")) {
            cuMonth = 10;
        } else if (month.contains("Nov")) {
            cuMonth = 11;
        } else if (month.contains("Dec")) {
            cuMonth = 12;
        }

        cuuDate = cuMonth + "-" + day + "-" + year;
        String showDate = day + "-" + cuMonth + "-" + year;
        tvDate.setText(formaDate);

        imgCalender = (ImageView) findViewById(R.id.imgCalender);
        empId = getIntent().getStringExtra("SecureID");

        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(this, 3));
        getItem(cuuDate,"0","0");

        tvMonthWise = (TextView) findViewById(R.id.tvMonthWise);
        tvDateWise = (TextView) findViewById(R.id.tvDateWise);
        llDate = (LinearLayout) findViewById(R.id.llDate);
        llShow = (LinearLayout) findViewById(R.id.llShow);

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));

        String month1 = dateFormat.format(date);
        if (month.equals("01")) {
            month_name = "January";

        } else if (month1.equals("02")) {
            month_name = "February";
        } else if (month1.equals("03")) {
            month_name = "March";
        } else if (month1.equals("04")) {
            month_name = "April";
        } else if (month1.equals("05")) {
            month_name = "May";
        } else if (month1.equals("06")) {
            month_name = "June";
        } else if (month1.equals("07")) {
            month_name = "July";
        } else if (month1.equals("08")) {
            month_name = "August";
        } else if (month1.equals("09")) {
            month_name = "September";
        } else if (month1.equals("10")) {
            month_name = "October";
        } else if (month1.equals("11")) {
            month_name = "November";
        } else if (month1.equals("12")) {
            month_name = "December";
        }

        spYear = (Spinner) findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);

        btnShow=(Button)findViewById(R.id.btnShow);

        imBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        tvEmpName=(TextView)findViewById(R.id.tvEmpName);
        tvEmpName.setText("Report of "+getIntent().getStringExtra("empName")+" :");




    }

    private void onClick() {
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePicker();
            }
        });

        tvMonthWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDate.setVisibility(View.GONE);
                llShow.setVisibility(View.VISIBLE);
                setYearItem();
            }
        });

        tvDateWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDate.setVisibility(View.VISIBLE);
                llShow.setVisibility(View.GONE);
                getItem(cuuDate,"0","0");
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItemForM("0",year,monthname);
            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year=yearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthname=monthList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WFHTeamReportActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void showEndDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(WFHTeamReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mmonth = null;
                        int md = (monthOfYear + 1);
                        if (md == 1) {
                            mmonth = "Jan";
                        } else if (md == 2) {
                            mmonth = "Feb";
                        } else if (md == 3) {
                            mmonth = "Mar";
                        } else if (md == 4) {
                            mmonth = "Apr";
                        } else if (md == 5) {
                            mmonth = "May";
                        } else if (md == 6) {
                            mmonth = "Jun";
                        } else if (md == 7) {
                            mmonth = "Jul";
                        } else if (md == 8) {
                            mmonth = "Aug";
                        } else if (md == 9) {
                            mmonth = "Sep";
                        } else if (md == 10) {
                            mmonth = "Oct";
                        } else if (md == 11) {
                            mmonth = "Nov";
                        } else if (md == 12) {
                            mmonth = "Dec";
                        }
                        cuuDate = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                        
                        tvDate.setText(dayOfMonth + "-" + mmonth + "-" + year);
                        getItem(cuuDate,"0","0");

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }


    private void getItem(String date,String y,String m) {

        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = APi.sGetOfflineDailyLogActivityApi+"AEMEmployeeID=" + empId + "&Year="+y+"&Month="+m+"&AttendanceDate=" + date + "&Operation=1";
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
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);

                                    String PunchInTime = obj.optString("PunchInTime");
                                    String AccessFrom = obj.optString("AccessFrom");
                                    String AddressIN=obj.optString("AddressIN");
                                    String FNameIN=obj.optString("FNameIN");
                                    String imgUrlIN=obj.optString("imgUrlIN");
                                    String date=obj.optString("PunchOut");
                                    String PunchOutTime=obj.optString("PunchOutTime");

                                    MarkInViewModel obj2 = new MarkInViewModel(AddressIN,PunchInTime,"https://cloud.geniusconsultant.com/GeniusESS"+imgUrlIN,FNameIN,"",date);
                                    obj2.setPunchOutTime(PunchOutTime);;
                                    obj2.setFlag("0");
                                    obj2.setAccessForm(AccessFrom);
                                    itemList.add(obj2);


                                }
                                TeamMarkReportAdapter teamMarkReportAdapter=new TeamMarkReportAdapter(itemList,WFHTeamReportActivity.this);
                                rvItem.setAdapter(teamMarkReportAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(WFHTeamReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getItemForM(String date,String y,String m) {

        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = APi.sGetOfflineDailyLogActivityApi+"AEMEmployeeID=" + empId + "&Year="+y+"&Month="+m+"&AttendanceDate=" + date + "&Operation=6";
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
                                for (int i = 0; i < responseData.length(); i++) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(WFHTeamReportActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        MarkReportAdapter vAdapter = new MarkReportAdapter(itemList, getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }


    private void setYearItem() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String y= String.valueOf(year);

        yearList.add("2023");
        yearList.add("2024");
        yearList.add("2025");
        yearList.add("2026");
        yearList.add("2027");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (WFHTeamReportActivity.this, android.R.layout.simple_spinner_item,
                        yearList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = spinnerArrayAdapter.getPosition(y);
        spYear.setAdapter(spinnerArrayAdapter);
        spYear.setSelection(index);

        setMonth();


    }

    private void setMonth() {


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
                (WFHTeamReportActivity.this, android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = spinnerArrayAdapter.getPosition(month_name);
        spMonth.setAdapter(spinnerArrayAdapter);
        spMonth.setSelection(index);

    }
}
