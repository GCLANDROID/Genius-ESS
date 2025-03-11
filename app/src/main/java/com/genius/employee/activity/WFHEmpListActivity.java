package com.genius.employee.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.adapter.EmpDetailsAdapter;
import com.genius.employee.model.EmpDetailsModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.RecyclerItemClickListener;

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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WFHEmpListActivity extends AppCompatActivity {
    Spinner spYear, spMonth;
    LinearLayout llLoader, llMain, llNoData;
    String responseID;
    ArrayList<String> yearList = new ArrayList();
    ArrayList<SpinnerModel> modelYearList = new ArrayList();
    String month_name;
    ArrayList<String> monthList = new ArrayList();
    RecyclerView rvItem;
    ArrayList<EmpDetailsModel> itemList = new ArrayList();
    Pref pref;
    String yearid;
    String monthid, monName;
    Button btnShow;
    ImageView imBack, imgHome;
    TextView tvDate;
     String formaDate;
    String mmonth;
    ImageView imgCalender;
    String showDate;
    String empId;
    EditText etSearch;
    EmpDetailsAdapter vAdapter;
    ArrayList<String>presetList=new ArrayList<>();
    ArrayList<String>absentList=new ArrayList<>();
    TextView tvPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wfhemp_list);
        initView();
        getItem();
        onClick();
    }

    private void initView() {
        pref = new Pref(WFHEmpListActivity.this);
        etSearch=(EditText)findViewById(R.id.etSearch);
        spYear = (Spinner) findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);

        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month", dateFormat.format(date));

        String month = dateFormat.format(date);
        if (month.equals("01")) {
            month_name = "Jan";

        } else if (month.equals("02")) {
            month_name = "Feb";
        } else if (month.equals("03")) {
            month_name = "Mar";
        } else if (month.equals("04")) {
            month_name = "Apr";
        } else if (month.equals("05")) {
            month_name = "May";
        } else if (month.equals("06")) {
            month_name = "Jun";
        } else if (month.equals("07")) {
            month_name = "Jul";
        } else if (month.equals("08")) {
            month_name = "Aug";
        } else if (month.equals("09")) {
            month_name = "Sep";
        } else if (month.equals("10")) {
            month_name = "Oct";
        } else if (month.equals("11")) {
            month_name = "Nov";
        } else if (month.equals("12")) {
            month_name = "Dec";
        }

        Log.d("mon", month_name);

        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(WFHEmpListActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);



        btnShow = (Button) findViewById(R.id.btnShow);

        imBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);


        tvDate = (TextView) findViewById(R.id.tvDate);
        tvPresent=(TextView)findViewById(R.id.tvPresent);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
        SimpleDateFormat dfe = new SimpleDateFormat("dd-MMM-yyyy");
        formaDate = df.format(c);
        showDate=dfe.format(c);
        tvDate.setText(showDate);


        imgCalender = (ImageView) findViewById(R.id.imgCalender);
        if (pref.getEmpId().equals("2070002087") || pref.getEmpId().equals("2070000032")){
            empId="2070000031";
            //empId="2070000135",207000134;

        }else {
            empId=pref.getEmpId();
        }


    }

    private void onClick() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yearid = modelYearList.get(i).getItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monName = monthList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem();
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
                Intent intent = new Intent(WFHEmpListActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDatePicker();
            }
        });
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
                (WFHEmpListActivity.this, android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int index = spinnerArrayAdapter.getPosition(month_name);
        spMonth.setAdapter(spinnerArrayAdapter);
        spMonth.setSelection(index);

    }


    private void getItem() {

        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl="https://cloud.geniusconsultant.com/GeniusESS/API/ManageEmployee/AttendanceDateReport?EmpID=2070000031&curDate="+formaDate;
       // String surl = APi.sAttendanceDateReportApi+"EmpID=" + pref.getSecureEmpId() + "&curDate=" + formaDate;
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

                                    String EmployeeID = obj.optString("EmployeeID");
                                    String SecureID = obj.optString("SecureID");
                                    String EmpName = obj.optString("EmpName");
                                    String Branch = obj.optString("Branch");
                                    String Department = obj.optString("Department");
                                    String Mobile=obj.optString("Mobile");
                                    boolean IsPresent = obj.optBoolean("IsPresent");
                                    if (IsPresent){
                                        presetList.add(EmpName);
                                    }else {
                                        absentList.add(EmpName);
                                    }


                                    EmpDetailsModel obj2 = new EmpDetailsModel(EmployeeID, EmpName, Department, Branch, IsPresent,Mobile);
                                    obj2.setSecureID(SecureID);
                                    itemList.add(obj2);


                                }
                                int present=presetList.size();
                                tvPresent.setText("Total Present: "+present+" Out of "+itemList.size());

                                vAdapter = new EmpDetailsAdapter(itemList, getApplicationContext(),showDate);
                                rvItem.setAdapter(vAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(WFHEmpListActivity.this);
        requestQueue.add(stringRequest);
    }




    private void showEndDatePicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(WFHEmpListActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

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
                         String py;
                        String yy= String.valueOf(year);
                        py = yy.substring(yy.length() - 2);


                        formaDate = dayOfMonth + "-" + mmonth + "-" + py;
                         showDate=dayOfMonth + "-" + mmonth + "-" + year;
                        tvDate.setText(formaDate);
                        getItem();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public void dDate(){
        String dDatee=showDate;


    }




    void filter(String text){
        ArrayList<EmpDetailsModel> temp = new ArrayList();
        for(EmpDetailsModel d: itemList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getEmpName().toLowerCase().contains(text)||(d.getEmpName().toUpperCase().contains(text))){
                temp.add(d);
            }
        }
        //update recyclerview
        vAdapter.updateList(temp);
    }


}
