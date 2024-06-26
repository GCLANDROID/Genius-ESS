package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.model.DeptSpinnerModel;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class FeedbackLoginActivity extends AppCompatActivity {
    TextView tvSignIn;
    EditText etUserId, etPassword;
    String userId, password;
    LinearLayout llSignIn;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog, al1;
    Pref pref;
    String UserType;
    String refreshedToken;
    EditText etSecurityCode;
    String AEMEmployeeID;
    String version;
    CheckBox ckRemeber;
    Spinner spDomain;
    ArrayList<DeptSpinnerModel> mDomainList = new ArrayList();
    ArrayList<String> domainList = new ArrayList();
    String securityCode="";
    String domainId="";
    ImageView imgVisible, imginVisible;
    ImageView imgSeen,imgHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_login);
        initialize();
        getDomain();

        onClick();
    }

    private void initialize() {
        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);

        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(FeedbackLoginActivity.this);
        refreshedToken = "hrms";
//        Log.d("token",refreshedToken);
        etSecurityCode = (EditText) findViewById(R.id.etSecuritycode);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        spDomain = (Spinner) findViewById(R.id.spDomain);
        imgSeen=(ImageView)findViewById(R.id.imgSeen);
        imgHide=(ImageView)findViewById(R.id.imgHide);





    }

    private void onClick() {
        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserId.getText().toString().length() > 0) {
                    if (etPassword.getText().toString().length() > 0) {
                        if (connectionCheck.isNetworkAvailable()) {
                            if (!domainId.equals("")) {


                                loginDetils();
                                Date d = new Date();
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                String currentDateTimeString = sdf.format(d);
                                Log.d("ctime", currentDateTimeString);
                            }else {
                                Toast.makeText(getApplicationContext(),"please select domain",Toast.LENGTH_LONG).show();
                            }
                            /*Intent intent=new Intent(LoginActivity.this,.class);
                            startActivity(intent);*/



                        } else {
                            connectionCheck.getNetworkActiveAlert().show();
                        }


                    } else {
                        etPassword.setError("Please enter your password");
                        etPassword.requestFocus();
                    }

                } else {
                    etUserId.setError("Please enter your User Id");
                    etUserId.requestFocus();
                }


            }
        });

        spDomain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                domainId=mDomainList.get(position).getItemId();
                pref.saveDomainId(domainId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHide.setVisibility(View.VISIBLE);
                imgSeen.setVisibility(View.GONE);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

            }
        });

        imgHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSeen.setVisibility(View.VISIBLE);
                imgHide.setVisibility(View.GONE);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        });

    }

    public void loginFunction() {

        byte[] data = new byte[0];
        try {
            data = etPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT).replaceAll("\\s+", "");;
        String surl = "http://111.93.182.174/GeniusiOSApi/api/get_GCLAuthenticateWithEncryption?MasterID=" + etUserId.getText().toString() + "&Password=" + base64 + "&IMEI=0000&Version=" + version + "&SecurityCode=" + domainId + "&DeviceID=" + refreshedToken + "&DeviceType=A";
        Log.d("inputLogin", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Authenticating...");
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
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    AEMEmployeeID = obj.optString("AEMEmployeeID");
                                    pref.saveFeedbackEmpId(AEMEmployeeID);

                                    Log.d("aemp", pref.getEmpId());
                                    String Name = obj.optString("Name");
                                    String LoginDateTime = obj.optString("LoginDateTime");
                                    pref.saveloginTime(LoginDateTime);
                                    String FlagMenu = obj.optString("FlagMenu");
                                    pref.saveMenu(FlagMenu);
                                    Log.d("menud", pref.getMenu());
                                    String AEMConsultantID = obj.optString("AEMConsultantID");
                                    pref.saveEmpConId(AEMConsultantID);
                                    String AEMClientID = obj.optString("AEMClientID");
                                    pref.saveEmpClintId(AEMClientID);
                                    String AEMClientOfficeID = obj.optString("AEMClientOfficeID");
                                    pref.saveEmpClintOffId(AEMClientOfficeID);
                                    String MasterID = obj.optString("MasterID");
                                    pref.saveFeedBackMasterId(MasterID);
                                    Log.d("Master", MasterID);
                                    UserType = obj.optString("UserType");

                                    String CTCUrl = obj.optString("CTCUrl");
                                    pref.saveCTCURL(CTCUrl);
                                    String WeeklyOff = obj.optString("WeeklyOff");

                                    String Leave = obj.optString("LeaveApply");

                                    String LeaveUrl = obj.optString("LeaveUrl");

                                    String AttdImage = obj.optString("AttdImage");

                                    String BackAttd = obj.optString("BackAttd");

                                    String IsSupervisor = obj.optString("IsSupervisor");

                                    String CompanyName = obj.optString("CompanyName");

                                    String FlagAddr = obj.optString("FlagAddr");

                                    String Password = obj.optString("Password");



                                }

                                    Intent intent = new Intent(FeedbackLoginActivity.this, FeedBackDashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                            } else {
                                shoeDialog();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedbackLoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(FeedbackLoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");

    }

    public void RMSloginFunction() {

        byte[] data = new byte[0];
        try {
            data = etPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        String surl = "https://cloud.geniusconsultant.com/GeniusJobsAPI/api/RMSClientFeedBack/RMSLoginAuth?UserName="+etUserId.getText().toString()+"&UserPassword="+base64+"&Webaddress=::1&SourceType="+domainId;
        Log.d("inputLogin", surl);
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Authenticating...");
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
                            int ResponseCode= job1.optInt("ResponseCode");
                            if (ResponseCode==1) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                                JSONArray responseData = job1.optJSONArray("ResponseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String RMSUserLoginID=obj.optString("RMSUserLoginID");
                                    pref.saveFeedbackEmpId(RMSUserLoginID);



                                }

                                Intent intent = new Intent(FeedbackLoginActivity.this, FeedBackDashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                shoeDialog();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedbackLoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(FeedbackLoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getDomain() {

        String surl = "http://111.93.182.174/GeniusiOSApi/api/get_DomianList";
        Log.d("officeapi", surl);
        final ProgressDialog pd = new ProgressDialog(FeedbackLoginActivity.this);
        pd.setMessage("Loading....");
        pd.setCancelable(false);
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseIFBCategory", response);
                        pd.dismiss();
                        mDomainList.clear();
                        domainList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            // String responseText = job1.optString("responseText");
                            boolean ResponseStatus = job1.optBoolean("responseStatus");

                            if (ResponseStatus) {
                                //Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                mDomainList.add(new DeptSpinnerModel("0","0"));
                                domainList.add("Please Select Domain");
                                domainList.add("ITR");
                                domainList.add("FSSR");
                                domainList.add("PSS");
                                mDomainList.add(new DeptSpinnerModel("ITR","ITR"));
                                mDomainList.add(new DeptSpinnerModel("FSSR","FSSR"));
                                mDomainList.add(new DeptSpinnerModel("PSS","PSS"));
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String value = obj.optString("Value");
                                    String id = obj.optString("Id");
                                    domainList.add(value);
                                    DeptSpinnerModel deptmodel = new DeptSpinnerModel(id, value);
                                    mDomainList.add(deptmodel);

                                }


                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (FeedbackLoginActivity.this, android.R.layout.simple_spinner_item,
                                                domainList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spDomain.setAdapter(spinnerArrayAdapter);



                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FeedbackLoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                Toast.makeText(FeedbackLoginActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(FeedbackLoginActivity.this);
        requestQueue.add(stringRequest);


    }

    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FeedbackLoginActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();


    }

    private void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("somthing went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

        alertDialogBuilder.show();


    }

    private void loginDetils(){
        if (domainId.equals("ITR")||domainId.equals("PSS")||domainId.equals("FSSR")){
            RMSloginFunction();
        }else {
            loginFunction();
        }
    }


}
