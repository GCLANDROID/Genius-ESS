package com.genius.employee.activity;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.genius.employee.adapter.AttendanceAdapter;
import com.genius.employee.adapter.SalaryAdapter;
import com.genius.employee.model.AttendanceModel;
import com.genius.employee.model.SalaryModel;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalaryActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<SalaryModel> salaryList = new ArrayList();
    LinearLayout llLoader, llMain;
    Pref pref;
    Spinner spYear, spMonth;
    ArrayList<SpinnerModel> modelYearList = new ArrayList();
    ArrayList<String> yearList = new ArrayList();
    String yearid = "";
    String monthId = "";
    AlertDialog alertDialog;

    ArrayList<SpinnerModel> modelMonthList = new ArrayList();
    ArrayList<String> monthList = new ArrayList();
    int MY_SOCKET_TIMEOUT_MS=60000;
    LinearLayout llSearch;
    LinearLayout llNoData;
    TextView tvWeb;
    String image_url;
    LinearLayout llWEB;
    ImageView imgBack,imgHome;
    LinearLayout llGLogo;
    LinearLayout llCoonection;
    ImageView imgSearch;
    LinearLayout llNoMonth,llMonth;
    LinearLayout llShow;
    TextView tvEmpId;
    String responseID;
    private static int CODE_AUTHENTICATION_VERIFICATION=241;
    TextView tvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary);
        initialize();
        setYearItem();
        onClick();
    }


    private void initialize() {
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        if(km.isKeyguardSecure()) {

            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "password");
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
        }
        else {
            Toast.makeText(this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
        }
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(SalaryActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(layoutManager);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llSearch=(LinearLayout)findViewById(R.id.llSearch);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);
        tvWeb=(TextView)findViewById(R.id.tvWeb);
        llWEB=(LinearLayout)findViewById(R.id.llWEB);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        llGLogo=(LinearLayout)findViewById(R.id.llGLogo);
        llCoonection=(LinearLayout)findViewById(R.id.llCoonection);
        imgSearch=(ImageView)findViewById(R.id.imgSearch);


        spYear = (Spinner)findViewById(R.id.spYear);
        spMonth = (Spinner) findViewById(R.id.spMonth);
        llMonth=(LinearLayout)findViewById(R.id.llMonth);
        llNoMonth=(LinearLayout)findViewById(R.id.llNoMonth);

        llShow = (LinearLayout)findViewById(R.id.llShow);
        tvEmpId=(TextView)findViewById(R.id.tvEmpId);
        tvEmpId.setText("Salary View for "+pref.getEmpName());
        tvToolBar=(TextView) findViewById(R.id.tvToolBar);


//2070002627

    }

    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llGLogo.setVisibility(View.GONE);
        llCoonection.setVisibility(View.GONE);
        String surl = APi.sSalaryHistoryApi+"EmployeeId="+pref.getSecureEmpId()+"&FinYearID="+yearid+"&Month="+monthId;
        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        salaryList.clear();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = responseData.length() - 1; i >= 0; i--) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String MonthName = obj.optString("MonthName");
                                    String FinancialYear = obj.optString("FinancialYear");
                                    Log.d("FinancialYear",FinancialYear);
                                    String MonthlyGross = obj.optString("MonthlyGross");
                                    String ActualGross = obj.optString("ActualGross");
                                    String Deduction = obj.optString("Deduction");
                                    String MonthlyNet = obj.optString("MonthlyNet");
                                    String EsiEmployeeCon = obj.optString("EsiEmployeeCon");
                                    String PTValue = obj.optString("PTValue");
                                    String WebURL=obj.optString("WebURL").replace("www.","");
                                    SalaryModel salModel = new SalaryModel(FinancialYear,MonthlyGross,ActualGross,Deduction,MonthlyNet,EsiEmployeeCon,PTValue,MonthName,WebURL);
                                    salaryList.add(salModel);
                                }
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llSearch.setVisibility(View.GONE);
                                llNoData.setVisibility(View.GONE);
                                llGLogo.setVisibility(View.GONE);
                                llCoonection.setVisibility(View.GONE);
                                setAdapter();
                            } else {
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llSearch.setVisibility(View.GONE);
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
                llSearch.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
                llGLogo.setVisibility(View.GONE);
                llCoonection.setVisibility(View.VISIBLE);
                //Toast.makeText(SalaryActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
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
        SalaryAdapter salAdapter = new SalaryAdapter(salaryList,SalaryActivity.this);
        rvItem.setAdapter(salAdapter);
    }

    private void onClick(){
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

        tvWeb.setOnClickListener(new View.OnClickListener() {
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
                Intent intent=new Intent(getApplicationContext(),EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearid = modelYearList.get(position).getItemId();
                tvToolBar.setText("Salary - "+modelYearList.get(position).getItemName());
                modelMonthList.clear();
                monthList.clear();
                setMonthItem1();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthId=modelMonthList.get(position).getItemId();
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


    private void setYearItem() {
        String surl = APi.sGetFinancialYearApi;
        Log.d("compurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llSearch.setVisibility(View.GONE);
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
                        llSearch.setVisibility(View.GONE);
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
                                        (SalaryActivity.this, android.R.layout.simple_spinner_item,
                                                yearList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                int index = spinnerArrayAdapter.getPosition(responseID);
                                spYear.setAdapter(spinnerArrayAdapter);
                                spYear.setSelection(index);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
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





    private void setMonthItem1() {

        String surl = APi.sGetAttendanceMonthApi+"YearID=" + yearid;
        Log.d("compurl", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        progressBar.dismiss();

                        modelMonthList.add(new SpinnerModel("0", "0"));
                        monthList.add("ALL");
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
                                        (SalaryActivity.this, android.R.layout.simple_spinner_item,
                                                monthList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spMonth.setSelection(0);
                                spMonth.setAdapter(spinnerArrayAdapter);

                            } else {

                                spMonth.setVisibility(View.GONE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SalaryActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SalaryActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.search_dialog, null);
        dialogBuilder.setView(dialogView);






        LinearLayout llShow = (LinearLayout) dialogView.findViewById(R.id.llShow);

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
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

    private void openBrowser(){
        Uri uri = Uri.parse(image_url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==CODE_AUTHENTICATION_VERIFICATION)
        {
           // Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show();
        }
        else
        {
            onBackPressed();
        }
    }
}
