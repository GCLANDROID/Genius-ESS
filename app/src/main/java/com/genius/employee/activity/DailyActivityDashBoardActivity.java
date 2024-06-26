package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AppCompatActivity;

public class DailyActivityDashBoardActivity extends AppCompatActivity {
    LinearLayout llManage, llReport;
    Pref pref;
    ImageView imgBack, imgHome;
    String lebelId = "";
    String deptId;
    NetworkConnectionCheck connectionCheck;
    double latitude,longitude;
    GPSTracker gps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_dash_board);
        init();
        onClick();
    }

    private void init() {
        pref = new Pref(getApplicationContext());
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);


        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);


        lebelId = pref.getLebelId();


        deptId = pref.getDepttId();
        connectionCheck=new NetworkConnectionCheck(DailyActivityDashBoardActivity.this);

        gps = new GPSTracker(this);

// check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();

            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();

        } else {

            gps.showSettingsAlert();
        }


    }

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (latitude!=0.0) {
                        if (connectionCheck.isNetworkAvailable()) {
                            getStatus();
                        }else {
                            Toast.makeText(getApplicationContext(), "Please enable your network connection", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Sorry your current address not found.Please check your GPS connection", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lebelId.equals("2060000003")||pref.getEmpId().equals("2070002087")) {
                    Intent intent = new Intent(DailyActivityDashBoardActivity.this, SupReportDashboardActivity.class);
                    intent.putExtra("lebelId", lebelId);
                    intent.putExtra("deptId", deptId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DailyActivityDashBoardActivity.this, NumberVisitActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
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
                Intent intent = new Intent(getApplicationContext(), EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    private void getStatus() {
        final ProgressDialog pd = new ProgressDialog(DailyActivityDashBoardActivity.this);
        pd.setMessage("loading...");
        pd.setCancelable(false);
        pd.show();

        final String surl = APi.sUrl+"?UserID=" + pref.getEmpId();
        Log.d("inputLogin", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                String  responseCode = job1.optString("responseCode");
                                if (responseCode.equals("1")){
                                    Intent intent = new Intent(DailyActivityDashBoardActivity.this, StartTimeActivity.class);
                                    startActivity(intent);
                                }else if (responseCode.equals("2")){
                                    Intent intent = new Intent(DailyActivityDashBoardActivity.this, DailyInTimeActivity.class);
                                    startActivity(intent);
                                }else if (responseCode.equals("3")){
                                    Intent intent = new Intent(DailyActivityDashBoardActivity.this, DailyOutTimeActivity.class);
                                    startActivity(intent);
                            } else {

                                pd.dismiss();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DailyActivityDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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

        };
        RequestQueue requestQueue = Volley.newRequestQueue(DailyActivityDashBoardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
}
