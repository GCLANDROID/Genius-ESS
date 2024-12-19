package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class InsuranceActivity extends AppCompatActivity {
    ImageView circleImage;
    TextView tvName, tvDOB, tvGender, tvSum, tvUHID, tvHealthCard, tvHospitalList, tvHCardManual, tvPolicyBenefit, tvHospitalManual, tvDeHospital, tvClaimForm,tvPolicy,tvInsurance,tvAge;
    Pref pref;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    String CardManualDoc,HospitalNetworkDoc,PolicyBenefitDoc,DeListedHospitalsDoc,ClaimFormDoc,CardLink,HospitalsNetworkLink;
    ImageView imgBack,imgHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        initView();


    }

    private void initView() {
        pref = new Pref(InsuranceActivity.this);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        circleImage = (ImageView) findViewById(R.id.circleImage);
        tvUHID = (TextView) findViewById(R.id.tvUHID);
        tvSum = (TextView) findViewById(R.id.tvSum);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvDOB = (TextView) findViewById(R.id.tvDOB);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPolicy=(TextView)findViewById(R.id.tvPolicy);
        tvInsurance=(TextView)findViewById(R.id.tvInsurance);
        tvAge=(TextView)findViewById(R.id.tvAge);

        tvHealthCard = (TextView) findViewById(R.id.tvHealthCard);
        tvHospitalList = (TextView) findViewById(R.id.tvHospitalList);
        tvHCardManual = (TextView) findViewById(R.id.tvHCardManual);
        tvPolicyBenefit = (TextView) findViewById(R.id.tvPolicyBenefit);
        tvHospitalManual = (TextView) findViewById(R.id.tvHospitalManual);
        tvDeHospital = (TextView) findViewById(R.id.tvDeHospital);
        tvClaimForm = (TextView) findViewById(R.id.tvClaimForm);

        String profileimage = pref.getProfileImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(profileimage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        if (pref.getProfileImage().equals("null")) {
            circleImage.setImageResource(R.drawable.user);
        } else {
            circleImage.setImageBitmap(decodedImage);
        }

        tvName.setText(pref.getEmpName());
        getInsuranceDetails();

        tvHealthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(CardLink);
            }
        });


        tvHospitalList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(HospitalsNetworkLink);
            }
        });


        tvHCardManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(CardManualDoc);
            }
        });


        tvPolicyBenefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(PolicyBenefitDoc);
            }
        });


        tvHospitalManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(HospitalNetworkDoc);
            }
        });


        tvDeHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(DeListedHospitalsDoc);
            }
        });


        tvClaimForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(ClaimFormDoc);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InsuranceActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void getInsuranceDetails() {
        ProgressDialog progressDialog = new ProgressDialog(InsuranceActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String surl = APi.shealthcardApi + "EmployeeID=" + pref.getSecureEmpId();
        Log.d("Save_Device_ID_URL", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Save_Device_ID_Response", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONObject responseData = job1.optJSONObject("responseData");
                                String DOB = responseData.optString("DOB");
                                tvDOB.setText(DOB);
                                String POLICY_NUMBER = responseData.optString("POLICY_NUMBER");
                                tvPolicy.setText(POLICY_NUMBER);
                                String Gender = responseData.optString("Gender");
                                tvGender.setText(Gender);
                                String UHID_NO = responseData.optString("UHID_NO");
                                tvUHID.setText(UHID_NO);
                                String SUM_INSURED = responseData.optString("SUM_INSURED");
                                tvSum.setText(SUM_INSURED);
                                String InsuranceProvider = responseData.optString("InsuranceProvider");
                                tvInsurance.setText(InsuranceProvider);
                                String Age = responseData.optString("Age");
                                tvAge.setText(Age);

                                CardManualDoc = APi.insurancedocurl + responseData.optString("CardManualDoc");
                                HospitalNetworkDoc = APi.insurancedocurl + responseData.optString("HospitalNetworkDoc");
                                PolicyBenefitDoc = APi.insurancedocurl + responseData.optString("PolicyBenefitDoc");
                                DeListedHospitalsDoc = APi.insurancedocurl + responseData.optString("DeListedHospitalsDoc");
                                ClaimFormDoc = APi.insurancedocurl + responseData.optString("ClaimFormDoc");
                                CardLink = APi.insurancedocurl + responseData.optString("CardLink");
                                HospitalsNetworkLink = APi.insurancedocurl + responseData.optString("HospitalsNetworkLink");

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
                progressDialog.dismiss();
                Intent intent=new Intent(InsuranceActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + pref.getAccessToken());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private void openBrowser(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
