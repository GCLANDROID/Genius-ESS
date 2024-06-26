package com.genius.employee.fragment;

import static maes.tech.intentanim.CustomIntent.customType;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


/*import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.genius.employee.activity.EDashBoardActivity;
import com.genius.employee.activity.MarkInAttendanceDashboardActivity;
import com.genius.employee.activity.MarkInReportActivity;
import com.genius.employee.activity.WFHEmpListActivity;
import com.genius.employee.activity.WFHReportDashboardActivity;
import com.genius.employee.adapter.MarkReportAdapter;
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


public class OnlineAttendanceReportFragment extends Fragment {
    LinearLayout llLoader,llMain,llNoData;
    RecyclerView rvItem;
    Spinner spYear,spMonth;
    ArrayList<String> yearList=new ArrayList();
    ArrayList<String>monthList=new ArrayList();
    Button btnShow;
    String year="";
    String month="";
    int iMonth;
    Pref pref;
    ArrayList<MarkInViewModel>itemList=new ArrayList();
    View view;
    String lebelId;
    TextView tvViewTeamReport;
    LinearLayout llOwnReport,llTeamReport,llJR,llSR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_online_attendance_report, container, false);
        initView();
        getLebelId();
        onClick();
        return view;
    }


    private void initView(){
        pref=new Pref(getContext());
        llLoader=(LinearLayout)view.findViewById(R.id.llLoader);
        llMain=(LinearLayout)view.findViewById(R.id.llMain);
        llNoData=(LinearLayout)view.findViewById(R.id.llNoData);
        rvItem=(RecyclerView)view.findViewById(R.id.rvItem);
        tvViewTeamReport=(TextView) view.findViewById(R.id.tvViewTeamReport);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String y= String.valueOf(year);
        rvItem.setLayoutManager(new GridLayoutManager(getContext(), 3));
        spYear=(Spinner)view.findViewById(R.id.spYear);
        spMonth=(Spinner)view.findViewById(R.id.spMonth);

        yearList.add("2020");
        yearList.add("2021");
        yearList.add("2022");
        yearList.add("2023");
        yearList.add("2024");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
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
                (getContext(), android.R.layout.simple_spinner_item,
                        monthList); //selected item will look like a spinner set from XML
        spinnerMonthArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int mindex = spinnerMonthArrayAdapter.getPosition(month_name);
        spMonth.setAdapter(spinnerMonthArrayAdapter);
        spMonth.setSelection(mindex);

        btnShow=(Button)view.findViewById(R.id.btnShow);

        llOwnReport=(LinearLayout)view.findViewById(R.id.llOwnReport);
        llTeamReport=(LinearLayout)view.findViewById(R.id.llTeamReport);
        llJR=(LinearLayout)view.findViewById(R.id.llJR);
        llSR=(LinearLayout)view.findViewById(R.id.llSR);




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
                        Toast.makeText(getContext(),"Please select month",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Please select Year",Toast.LENGTH_LONG).show();
                }
            }
        });
        llTeamReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WFHEmpListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(getContext(), "left-to-right");
            }
        });

        llOwnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MarkInReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(getContext(), "left-to-right");
            }
        });
    }

    private void getItem(){

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void setAdapter(){
        MarkReportAdapter vAdapter=new MarkReportAdapter(itemList,getContext());
        rvItem.setAdapter(vAdapter);
    }


    private void getLebelId() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("loading...");
        pd.setCancelable(false);
        pd.show();

        final String surl = APi.sManageEmployeeHierarchyApi + "EmployeeID=" + pref.getSecureEmpId();
        Log.d("inputLogin", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

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
                                    lebelId = obj.optString("LevelID");
                                    pref.saveLebelId(lebelId);
                                }
                                pd.dismiss();
                                if (lebelId.equals("2060000003")||lebelId.equals("2060000005")||lebelId.equals("2060000010")||lebelId.equals("2060000012")||pref.getEmpId().equals("2070002087")) {
                                    tvViewTeamReport.setVisibility(View.GONE);
                                    llJR.setVisibility(View.GONE);
                                    llSR.setVisibility(View.VISIBLE);
                                }else {
                                    tvViewTeamReport.setVisibility(View.GONE);
                                    llJR.setVisibility(View.VISIBLE);
                                    llSR.setVisibility(View.GONE);
                                   /* Intent intent = new Intent(MarkInAttendanceDashboardActivity.this, MarkInReportActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    customType(MarkInAttendanceDashboardActivity.this, "left-to-right");*/
                                }

                            } else {

                                pd.dismiss();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}