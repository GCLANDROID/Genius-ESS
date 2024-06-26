package com.genius.employee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
/*import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
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
import com.genius.employee.adapter.AttendanceAdapter;
import com.genius.employee.adapter.LeaveAdapter;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.LeaveModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceReportActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<AttendanceModel> attendanceList = new ArrayList();
    LinearLayout llLoader, llMain;
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
    TextView tvEmpId;
    String YearID;
    ;
    String responseID;
    int MonthId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        initialize();
        setYearItem();
        onClick();
        // setYearItem();
    }


    private void initialize() {
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(AttendanceReportActivity.this, LinearLayoutManager.VERTICAL, false);
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
        tvEmpId = (TextView) findViewById(R.id.tvEmpId);
        tvEmpId.setText("Attendance View for " + pref.getEmpName());


    }

//    private void getItem() {
//        llLoader.setVisibility(View.VISIBLE);
//        llMain.setVisibility(View.GONE);
//        llNoData.setVisibility(View.GONE);
//        llGLogo.setVisibility(View.GONE);
//        llCoonection.setVisibility(View.GONE);
//
//        String surl = "https://cloud.geniusconsultant.com/GeniusESS/API/Utility/GetMonthWiseAttendance?YearID=" + yearid + "&DepartmentID=" + pref.getDeptId() + "&EmployeeID=" + pref.getEmpId() + "&MonthID=" + monthId;
//        Log.d("reporturl", surl);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("responseLeave", response);
//                        attendanceList.clear();
//
//                        // attendabceInfiList.clear();
//
//                        try {
//                            JSONObject job1 = new JSONObject(response);
//                            Log.e("response12", "@@@@@@" + job1);
//                            String responseText = job1.optString("responseText");
//
//                            boolean responseStatus = job1.optBoolean("responseStatus");
//                            if (responseStatus) {
//
//                                image_url = job1.optString("image_url");
//                                Log.d("imageurl", image_url);
//                                if (image_url.equals("")) {
//                                    llWEB.setVisibility(View.GONE);
//                                } else {
//                                    llWEB.setVisibility(View.VISIBLE);
//                                }
//
//
//                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
//                                JSONArray responseData = job1.optJSONArray("responseData");
//                                for (int i = responseData.length() - 1; i >= 0; i--) {
//                                    JSONObject obj = responseData.getJSONObject(i);
//                                    String AWDays = obj.optString("AWDays");
//                                    String AttDays = obj.optString("AttDays");
//                                    String Abs = obj.optString("Abs");
//                                    String OtherAbs = obj.optString("OtherAbs");
//                                    String LS = obj.optString("LS");
//                                    String OtherLS = obj.optString("OtherLS");
//                                    String LWP = obj.optString("LWP");
//                                    String MLwp = obj.optString("MLwp");
//                                    String PaybleDays = obj.optString("PaybleDays");
//                                    String MonthName = obj.optString("MonthName");
//                                    String MonthView = obj.optString("MonthView");
//                                    AttendanceModel aModel = new AttendanceModel(AWDays, AttDays, PaybleDays, Abs, OtherAbs, LS, OtherLS, LWP, MLwp, MonthName, MonthView);
//                                    attendanceList.add(aModel);
//
//
//                                }
//
//                                llLoader.setVisibility(View.GONE);
//                                llMain.setVisibility(View.VISIBLE);
//
//                                llNoData.setVisibility(View.GONE);
//                                llGLogo.setVisibility(View.GONE);
//                                llCoonection.setVisibility(View.GONE);
//                                setAdapter();
//
//
//                            } else {
//
//                                llLoader.setVisibility(View.GONE);
//                                llMain.setVisibility(View.GONE);
//
//                                llNoData.setVisibility(View.VISIBLE);
//                                llGLogo.setVisibility(View.GONE);
//                                llCoonection.setVisibility(View.GONE);
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
//
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                llLoader.setVisibility(View.GONE);
//                llMain.setVisibility(View.GONE);
//
//                llNoData.setVisibility(View.GONE);
//                llGLogo.setVisibility(View.GONE);
//                llCoonection.setVisibility(View.VISIBLE);
//                Toast.makeText(AttendanceReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
//                Log.e("ert", error.toString());
//            }
//        }) {
//
//        };
//        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }

    private void getItem1() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llGLogo.setVisibility(View.GONE);
        llCoonection.setVisibility(View.GONE);

            String surl = APi.sGetMonthWiseAttendanceApi+"YearID=" + yearid + "&DepartmentID=" + pref.getDeptId() + "&EmployeeID=" + pref.getSecureEmpId() + "&MonthID=" + monthId;
        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        attendanceList.clear();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                image_url = job1.optString("image_url");
                                Log.d("imageurl", image_url);
                                if (image_url.equals("")) {
                                    llWEB.setVisibility(View.GONE);
                                } else {
                                    llWEB.setVisibility(View.GONE);
                                }


                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String AWDays = obj.optString("AWDays");
                                    String AttDays = obj.optString("AttDays");
                                    String Abs = obj.optString("Abs");
                                    String OtherAbs = obj.optString("OtherAbs");
                                    String LS = obj.optString("LS");
                                    String OtherLS = obj.optString("OtherLS");
                                    String LWP = obj.optString("LWP");
                                    String MLwp = obj.optString("MLwp");
                                    String PaybleDays = obj.optString("PaybleDays");
                                    String MonthName = obj.optString("MonthName");
                                    String MonthView = obj.optString("MonthView");
                                    MonthId = obj.optInt("MonthId");

                                    AttendanceModel aModel = new AttendanceModel(AWDays, AttDays, PaybleDays, Abs, OtherAbs, LS, OtherLS, LWP, MLwp, MonthName, MonthView);
                                    attendanceList.add(aModel);



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
                Toast.makeText(AttendanceReportActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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

    private void setAdapter() {
        AttendanceAdapter lAdapter = new AttendanceAdapter(attendanceList, AttendanceReportActivity.this);
        rvItem.setAdapter(lAdapter);
    }

    private void onClick() {
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

        llWEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceReportActivity.this, EDashBoardActivity.class);
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
                                        (AttendanceReportActivity.this, android.R.layout.simple_spinner_item,
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
                            Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }


    private void setMonthItem(String yid) {

        String surl = APi.sGetAttendanceMonthApi+"YearID=" + yid;
        Log.d("compurl", surl);
        final ProgressDialog progressDialog = new ProgressDialog(AttendanceReportActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        progressDialog.dismiss();
                        monthList.add("ALL");
                        modelMonthList.add(new SpinnerModel("0", "0"));
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
                                        (AttendanceReportActivity.this, android.R.layout.simple_spinner_item,
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
                            Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }


    private void showSearchDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceReportActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.search_dialog, null);
        dialogBuilder.setView(dialogView);


        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void openBrowser() {
        Uri uri = Uri.parse(image_url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public class ToSort implements Comparable<ToSort> {

        private Float val;


        public ToSort(Float val) {
            this.val = val;

        }

        @Override
        public int compareTo(ToSort f) {

            if (val.floatValue() == 4 && val.floatValue() < 12) {
                return 1;
            } else if (val.floatValue() < f.val.floatValue()) {
                return -1;
            } else {
                return 0;
            }

        }

    }


}
