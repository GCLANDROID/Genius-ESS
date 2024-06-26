package com.genius.employee.activity;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CountDownTimer;


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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;


public class PayrollActivity extends AppCompatActivity {
    LinearLayout llSalary,llCTC;
    ImageView imgBack,imgHome;
    Pref pref;
    LinearLayout llCTCD,llCTCD1,llSalaryD,llSalaryD1;
    AlertDialog alertDialog;
    String date,oldCTC,newCTC,growth;
    LinearLayout llNoData,llMain;
    boolean responseStatus;
    NetworkConnectionCheck connectionCheck;
    private CancellationSignal cancellationSignal = null;

    // create an authenticationCallback
    private BiometricPrompt.AuthenticationCallback authenticationCallback;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);
        initialize();
        onClick();

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private  void initialize(){

        connectionCheck=new NetworkConnectionCheck(PayrollActivity.this);
        pref=new Pref(this);
        llSalary=(LinearLayout)findViewById(R.id.llSalary);
        llCTC=(LinearLayout)findViewById(R.id.llCTC);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);



    }

    private  void onClick(){
        llSalary.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                   // workingStatusCheckForSalary();
                    workingStatusCheckForSalary();
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(PayrollActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llCTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {

                   workingStatusCheckForCTC();
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
                //openBrowser();
            }
        });

    }

    private void workingStatusCheckForSalary() {

        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        String surl = APi.sGetLastWorkingDayApi + "EmployeeId=" + pref.getSecureEmpId();
        Log.d("version", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);

                        pd.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            //String responseText="0";
                            if (responseText.equals("0")){
                                Intent intent = new Intent(PayrollActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(PayrollActivity.this, SalaryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }


                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PayrollActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
        requestQueue.add(stringRequest);

    }

    private void workingStatusCheckForCTC() {
        final ProgressDialog pd=new ProgressDialog(PayrollActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        String surl = APi.sGetLastWorkingDayApi + "EmployeeId=" + pref.getSecureEmpId();
        Log.d("version", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);
                        pd.dismiss();



                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");
                            //String responseText="0";
                            if (responseText.equals("0")){

                                Intent intent = new Intent(PayrollActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(PayrollActivity.this, CuurentCTCActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                customType(PayrollActivity.this, "left-to-right");
                            }


                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PayrollActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        RequestQueue requestQueue = Volley.newRequestQueue(PayrollActivity.this);
        requestQueue.add(stringRequest);
    }
    // it checks whether the
    // app the app has fingerprint
    // permission

    // this is a toast method which is responsible for
    // showing toast it takes a string as parameter
}
