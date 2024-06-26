package com.genius.employee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.ValidUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactPersonActivity extends AppCompatActivity {

    LinearLayout llNext;
    EditText etClintName,etDesignation,etClintPhn,etClintEmail;
    NetworkConnectionCheck networkConnectionCheck;
    Pref pref;
    ImageView imgBack,imgHome;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_person);
        initialize();

        onClick();
    }

    private void initialize(){
        String color = "<font color='#EE0000'>*</font>";
        networkConnectionCheck=new NetworkConnectionCheck(ContactPersonActivity.this);
        pref=new Pref(getApplicationContext());

        llNext=(LinearLayout)findViewById(R.id.llNext);
        etClintName=(EditText)findViewById(R.id.etClientName);
        etDesignation=(EditText)findViewById(R.id.etDesignation);
        etClintPhn=(EditText)findViewById(R.id.etClintPhn);
        etClintEmail=(EditText)findViewById(R.id.etClintEmail);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        if (pref.getDomainId().equals("ITR")||pref.getDomainId().equals("FSSR")||pref.getDomainId().equals("PSS")){
            getinformationForRMS();
        }else {
            getinformation();
        }

    }

    private void getinformation() {
        String surl = "http://111.93.182.174/GeniusiOSApi/api/gcl_ClientofficeContactDetails?ConsultantId="+pref.getEmpConId()+"&ClientId="+pref.getFClintId()+"&BranchId=0&clientOfficeId="+pref.getFOfficeId()+"&SecurityCode="+pref.getDomainId();
        Log.d("clintname", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String ContactPerson=obj.optString("ContactPerson");
                                    etClintName.setText(ContactPerson);
                                    String CPhone=obj.optString("CPhone");
                                    etClintPhn.setText(CPhone);
                                    String CDesignation=obj.optString("CDesignation");
                                    etDesignation.setText(CDesignation);
                                    String CEmailID=obj.optString("CEmailID");
                                    etClintEmail.setText(CEmailID);

                                }


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ContactPersonActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                internetAlert();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ContactPersonActivity.this);
        requestQueue.add(stringRequest);


    }

    private void getinformationForRMS() {
        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/GetRMSClientOfficeContactDe?ClientID="+pref.getFClintId()+"&ClientOfficeID="+pref.getFOfficeId()+"&SourceType="+pref.getDomainId();
        Log.d("clintname", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressBar.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            int ResponseCode = job1.optInt("ResponseCode");
                            if (ResponseCode==1) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("ResponseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String ContactPerson=obj.optString("RMSClientContactPersonName");
                                    etClintName.setText(ContactPerson);
                                    String CPhone=obj.optString("RMSClientContactPersonMobile");
                                    etClintPhn.setText(CPhone);
                                    String CDesignation=obj.optString("RMSClientContactPersonDesignation");
                                    etDesignation.setText(CDesignation);
                                    String CEmailID=obj.optString("RMSClientContactPersonEmail");
                                    etClintEmail.setText(CEmailID);

                                }


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ContactPersonActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                internetAlert();

                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(ContactPersonActivity.this);
        requestQueue.add(stringRequest);


    }

    private void onClick(){
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etClintName.getText().toString().length()>0){
                    if (etDesignation.getText().toString().length()>0){
                        if (etClintPhn.getText().toString().length()>9){
                            if (etClintEmail.getText().toString().length()>0){
                                if (!etClintName.getText().toString().contains(".com")) {
                                    if (!etClintName.getText().toString().contains(".net")) {
                                        if (!etClintName.getText().toString().contains(".NET")) {
                                            if (!etClintName.getText().toString().contains(".COM")) {
                                                if (ValidUtils.isValidEmail(etClintEmail.getText().toString())) {
                                                    if (!etClintPhn.getText().toString().equals("0000000000")) {
                                                        if (!etClintPhn.getText().toString().contains("1111111111")) {
                                                            if (!etClintPhn.getText().toString().contains("2222222222")) {
                                                                if (!etClintPhn.getText().toString().contains("3333333333")) {
                                                                    if (!etClintPhn.getText().toString().contains("4444444444")) {
                                                                        if (!etClintPhn.getText().toString().contains("4444444444")) {
                                                                            if (!etClintPhn.getText().toString().contains("5555555555")) {
                                                                                if (!etClintPhn.getText().toString().contains("6666666666")) {
                                                                                    if (!etClintPhn.getText().toString().contains("7777777777")) {
                                                                                        if (!etClintPhn.getText().toString().contains("8888888888")) {
                                                                                            if (!etClintPhn.getText().toString().contains("9999999999")) {
                                                                                                if (networkConnectionCheck.isNetworkAvailable()) {
                                                                                                    if (networkConnectionCheck.isGPSEnabled()) {
                                                                                                        pref.saveFClintName(etClintName.getText().toString());
                                                                                                        pref.saveClintPhn(etClintPhn.getText().toString());
                                                                                                        pref.saveClintDes(etDesignation.getText().toString());
                                                                                                        pref.saveClintEmail(etClintEmail.getText().toString());
                                                                                                        Intent intent = new Intent(ContactPersonActivity.this, FeedBackActivity.class);
                                                                                                        intent.putExtra("name", etClintName.getText().toString());
                                                                                                        intent.putExtra("designation", etDesignation.getText().toString());
                                                                                                        intent.putExtra("phone", etClintPhn.getText().toString());
                                                                                                        intent.putExtra("email", etClintEmail.getText().toString());
                                                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                        startActivity(intent);
                                                                                                        finish();
                                                                                                    }else {
                                                                                                        networkConnectionCheck.getSettingsAlert();
                                                                                                    }
                                                                                                }else {
                                                                                                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            }else {
                                                                                                etClintPhn.setError("Please enter Valid Phone Number");
                                                                                                etClintPhn.requestFocus();
                                                                                            }
                                                                                        }else {
                                                                                            etClintPhn.setError("Please enter Valid Phone Number");
                                                                                            etClintPhn.requestFocus();
                                                                                        }
                                                                                    }else {
                                                                                        etClintPhn.setError("Please enter Valid Phone Number");
                                                                                        etClintPhn.requestFocus();
                                                                                    }
                                                                                }else {
                                                                                    etClintPhn.setError("Please enter Valid Phone Number");
                                                                                    etClintPhn.requestFocus();
                                                                                }
                                                                            }else {
                                                                                etClintPhn.setError("Please enter Valid Phone Number");
                                                                                etClintPhn.requestFocus();
                                                                            }
                                                                        }else {
                                                                            etClintPhn.setError("Please enter Valid Phone Number");
                                                                            etClintPhn.requestFocus();
                                                                        }
                                                                    }else {
                                                                        etClintPhn.setError("Please enter Valid Phone Number");
                                                                        etClintPhn.requestFocus();
                                                                    }
                                                                }else {
                                                                    etClintPhn.setError("Please enter Valid Phone Number");
                                                                    etClintPhn.requestFocus();
                                                                }
                                                            }else {
                                                                etClintPhn.setError("Please enter Valid Phone Number");
                                                                etClintPhn.requestFocus();
                                                            }
                                                        }else {
                                                            etClintPhn.setError("Please enter Valid Phone Number");
                                                            etClintPhn.requestFocus();
                                                        }
                                                    }else {
                                                        etClintPhn.setError("Please enter Valid Phone Number");
                                                        etClintPhn.requestFocus();
                                                    }
                                                }else {
                                                    etClintEmail.setError("Please enter Valid Email Id");
                                                    etClintEmail.requestFocus();
                                                }
                                            }else {
                                                etClintName.setError("Please enter Valid name");
                                                etClintName.requestFocus();
                                            }
                                        }else {
                                            etClintName.setError("Please enter Valid name");
                                            etClintName.requestFocus();
                                        }
                                    }else {
                                        etClintName.setError("Please enter Valid name");
                                        etClintName.requestFocus();
                                    }
                                }else {
                                    etClintName.setError("Please enter Valid name");
                                    etClintName.requestFocus();
                                }

                            }else {
                                etClintEmail.setError("Please enter Client Email Id");
                                etClintEmail.requestFocus();
                            }

                        }else {
                            etClintPhn.setError("Please enter Client Phone Number");
                            etClintPhn.requestFocus();
                        }

                    }else {
                        etDesignation.setError("Please enter Designation");
                        etDesignation.requestFocus();
                    }

                }else {
                    etClintName.setError("Please enter Client Name");
                    etClintName.requestFocus();
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
                Intent intent=new Intent(ContactPersonActivity.this,FeedBackDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void internetAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactPersonActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.internetconnectiondialog, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();
    }
}
