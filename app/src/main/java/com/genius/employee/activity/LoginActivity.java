package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.employee.R;
import com.genius.employee.adapter.HolidayAdapter;
import com.genius.employee.model.HolidayModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.StringEncryption;
import com.genius.employee.utility.Util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    EditText etUserId, etPassword;
    LinearLayout llSignIn;
    NetworkConnectionCheck connectionCheck;
    AlertDialog alertDialog, al1, al2;
    Pref pref;
    String UserType;
    String refreshedToken;
    EditText etSecurityCode;
    String version;
    CheckBox ckRemember;
    int MY_SOCKET_TIMEOUT_MS=60000;
    NetworkConnectionCheck networkConnectionCheck;
    ImageView imgSeen,imgHide;
    boolean responseStatus;
    String playversion;
    LinearLayout llForgotPassword;
    String uniqueID;

    private static final String TAG = "LoginActivity";
     ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        //checkBersion();
        onClick();
    }

    private void initialize() {
        llSignIn = (LinearLayout) findViewById(R.id.llSignIn);
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");

        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(LoginActivity.this);
        refreshedToken = "101";
        uniqueID = UUID.randomUUID().toString();
        Log.d("uniqueID",uniqueID);
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

        ckRemember = (CheckBox) findViewById(R.id.ckRemember);

        if (pref.getNormalFlag().equals("1")) {
            ckRemember.setChecked(true);
            etUserId.setText(pref.getEmpId());
            etPassword.setText(pref.getPassword());

        }

        if (pref.getNormalFlag().equals("2")) {
            ckRemember.setChecked(false);
            etUserId.setText("");
            etPassword.setText("");
        }

        imgSeen=(ImageView)findViewById(R.id.imgSeen);
        imgHide=(ImageView)findViewById(R.id.imgHide);


        llForgotPassword=(LinearLayout)findViewById(R.id.llForgotPassword);

        String encryptedString = encryption("Arpan");
    }

    private void onClick() {
        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUserId.getText().toString().contains("TEMP") || etUserId.getText().toString().contains("temp")) {
                    etPassword.setText("password");
                } else {
                    etPassword.setText("");
                }
            }
        });
        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUserId.getText().toString().length() > 0) {
                    if (etPassword.getText().toString().length() > 0) {
                        if (connectionCheck.isNetworkAvailable()) {

                                loginFunction();

                        } else {
                            connectionCheck.getNetworkActiveAlert().show();
                        }
                    } else {
                        etPassword.setError("please enter your password");
                        etPassword.requestFocus();
                    }
                } else {
                    etUserId.setError("please enter your user id");
                    etUserId.requestFocus();
                }
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

        ckRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pref.saveCheckFlag("1");
                } else {
                    pref.saveCheckFlag("2");
                    pref.saveNormalFlag("2");
                    pref.saveMasterId("");
                    pref.savePassword("");
                }
            }
        });

        llForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
//172.16.0.145
    }


    public void loginFunction(){
        progressBar.show();

        Log.e(TAG, "username: "+etUserId.getText().toString()
                +"\npassword: "+etPassword.getText().toString());
        AndroidNetworking.post(APi.sLoginApi)
                .addBodyParameter("username", etUserId.getText().toString())
                .addBodyParameter("password", etPassword.getText().toString())
                .addBodyParameter("grant_type", "password")
                //.addHeaders("Content-Type: application/x-www-form-urlencoded")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "LOGIN: "+response);

                        if (response.has("access_token")) {
                            progressBar.show();
                            Log.e(TAG, "onResponse: SUCCESS");
                            JSONObject job = response;
                            String access_token = job.optString("access_token");
                            String token_type = job.optString("token_type");
                            String expires_in = job.optString("expires_in");
                            String SecurID = job.optString("EmpID");
                            String issued = job.optString(".issued");
                            String expires = job.optString(".expires");
                            pref.saveAccessToken(access_token);
                            pref.saveSecureEmpId(SecurID);
                            pref.saveMasterId(etUserId.getText().toString());
                            pref.savePassword(etPassword.getText().toString());
                           // saveDeviceID(); 404 error
                            pref.saveLogOutFlag("2");

                            /*;
*/
                            getDetailsOfUser();
                        }else {
                            progressBar.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "LOGIN_error: "+anError.getErrorBody());
                        try {
                            JSONObject jobError = new JSONObject(anError.getErrorBody());
                            if (jobError.has("error_description")){
                                Toast.makeText(LoginActivity.this, jobError.getString("error_description"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Network issue, please try after some time", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

  /*  public void loginFunction() throws UnsupportedEncodingException {
        byte[] data = new byte[0];
        try {
            data = etPassword.getText().toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        String surl = APi.sUrl+"ManageLogin?id1="+etUserId.getText().toString().replaceAll("\\s+", "").trim()+"&id2="+base64.replaceAll("\\s+", "").trim();
        Log.d("inputLogin", surl);
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
                             responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String RoleTitle=obj.optString("RoleTitle");
                                    pref.saveRollTitle(RoleTitle);
                                    String UserName=obj.optString("UserName");
                                    //loginSave(UserName);
                                    pref.saveEmpName(UserName);
                                    String UserID=obj.optString("UserID");
                                    pref.saveEmpId(UserID);
                                    String profileImage=obj.optString("profileImage");
                                    pref.saveProfileImage(profileImage);
                                    String DepartmentID=obj.optString("DepartmentID");
                                    pref.saveDeptId(DepartmentID);
                                    String MasterPage=obj.optString("MasterPage");
                                    pref.saveBranchId(MasterPage);
                                    if (pref.getCheckFlag().equals("1")){
                                        pref.saveNormalFlag("1");
                                        pref.saveMasterId(etUserId.getText().toString());
                                        pref.savePassword(etPassword.getText().toString());
                                        pref.saveLogOutFlag("2");
                                    }else {
                                        pref.saveNormalFlag("2");
                                        pref.saveMasterId("");
                                        pref.savePassword("");
                                    }
                                }
                                if (responseData.length() > 0) {
                                    saveDeviceID();
                                } else {
                                    shoeDialog();
                                    progressBar.dismiss();
                                }
                            } else {
                                shoeDialog();
                                progressBar.dismiss();
                            }
                            // boolean _status = job1.getBoolean("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.dismiss();
                Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
                showAlert();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/

    private void getDetailsOfUser() {
        progressBar.show();

        String surl = APi.sPostLoginApi+"id1="+pref.getSecureEmpId();
        Log.d("reporturl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseLeave", response);
                        progressBar.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            String RoleTitle=obj.optString("RoleTitle");
                            pref.saveRollTitle(RoleTitle);
                            String UserName=obj.optString("UserName");
                            //loginSave(UserName);
                            pref.saveEmpName(UserName);
                            String UserID=obj.optString("UserID");
                            pref.saveEmpId(UserID);
                            String profileImage=obj.optString("profileImage");
                            pref.saveProfileImage(profileImage);
                            String DepartmentID=obj.optString("DepartmentID");
                            pref.saveDeptId(DepartmentID);
                            String MasterPage=obj.optString("MasterPage");
                            pref.saveBranchId(MasterPage);

                            Intent intent = new Intent(LoginActivity.this, EDashBoardActivity.class);
                            startActivity(intent);
                            customType(LoginActivity.this, "left-to-right");
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // attendabceInfiList.clear();



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.dismiss();
                Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void shoeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.CustomDialogNew);
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
        alertDialogBuilder.setMessage("Something went wrong");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.show();
    }

    public String encryption(String strNormalText){
        String seedValue = "Geni0us";
        String normalTextEnc="";
        try {
            normalTextEnc = StringEncryption.encrypt(seedValue, strNormalText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalTextEnc;
    }
    public String decryption(String strEncryptedText){
        String seedValue = "YourSecKey";
        String strDecryptedText="";
        try {
            strDecryptedText = StringEncryption.decrypt(seedValue, strEncryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDecryptedText;
    }



    public class ToSort implements Comparable<ToSort> {

        private Float val;
        private String id;

        public ToSort(Float val, String id){
            this.val = val;
            this.id = id;
        }

        @Override
        public int compareTo(ToSort f) {
            if (val.floatValue() > f.val.floatValue()) {
                return 1;
            }
            else if (val.floatValue() <  f.val.floatValue()) {
                return -1;
            }
            else {
                return 0;
            }

        }

        @Override
        public String toString(){
            return this.id;
        }
    }












}
