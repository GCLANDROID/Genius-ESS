package com.genius.employee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.genius.employee.R;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.model.SpinnerModelForReim;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CliamManageActivity extends AppCompatActivity {
    Button btnRetunTrip;
    LinearLayout llReturnTrip,llConvence,llUtilities;
    Spinner spType;
    ArrayList<String>reimbursementType=new ArrayList<String>();
    String reimType="";
    Pref pref;
    TextView tvApproverName,tvEligiBleAmount;
    ArrayList<SpinnerModelForReim>mreimbursementType=new ArrayList<SpinnerModelForReim>();
    ProgressDialog progressDialog;
    int reimID;
    Spinner spTransportMode;
    ArrayList<String>transmodeList=new ArrayList<String>();
    ArrayList<SpinnerModelForReim>mTransModeList=new ArrayList<SpinnerModelForReim>();
    AlertDialog al5;
    int IsEligible;
    Button btnOtherSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliam_manage);
        initView();
        getApproverName();
        onClick();
    }

    private void initView(){
        pref=new Pref(CliamManageActivity.this);

        progressDialog=new ProgressDialog(CliamManageActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");

        tvApproverName=(TextView)findViewById(R.id.tvApproverName);
        btnRetunTrip=(Button)findViewById(R.id.btnRetunTrip);
        llReturnTrip=(LinearLayout)findViewById(R.id.llReturnTrip);
        llConvence=(LinearLayout)findViewById(R.id.llConvence) ;
        llUtilities=(LinearLayout)findViewById(R.id.llUtilities);
        spType=(Spinner)findViewById(R.id.spType);
        spTransportMode=(Spinner)findViewById(R.id.spTransportMode);
        tvEligiBleAmount=(TextView)findViewById(R.id.tvEligiBleAmount);
        btnOtherSubmit=(Button)findViewById(R.id.btnOtherSubmit);



    }

    private void onClick(){
        btnRetunTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llReturnTrip.setVisibility(View.VISIBLE);
            }
        });
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>0){
                    reimID=mreimbursementType.get(i).getItemId();
                    checkEligbleorNot(reimID);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnOtherSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClaim();
            }
        });
    }


    public void getApproverName() {

        progressDialog.show();
        String surl = "https://cloud.geniusconsultant.com/GeniusEssReim/api/Reimbursement/GetApproverName?EmpID="+pref.getEmpId();
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.show();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String ApproverName = job1.optString("ApproverName");
                            tvApproverName.setText(ApproverName);
                            getReimursementType();

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void getReimursementType() {
        progressDialog.show();
        String surl = "https://cloud.geniusconsultant.com/GeniusEssReim/api/Reimbursement/GetReimbursementType";
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.show();
                        mreimbursementType.clear();
                        reimbursementType.clear();
                        mreimbursementType.add(new SpinnerModelForReim("0",0,0));
                        reimbursementType.add("Please Select");

                        try {
                            JSONArray job1 = new JSONArray(response);
                            for (int i=0;i<job1.length();i++){
                                JSONObject jobb=job1.getJSONObject(i);
                                String name=jobb.optString("SubCategoryName");
                                int id=jobb.optInt("SubCategoryID");
                                int CategoryID=jobb.optInt("CategoryID");
                                SpinnerModelForReim spModel=new SpinnerModelForReim(name,id,CategoryID);
                                mreimbursementType.add(spModel);
                                reimbursementType.add(name);

                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (CliamManageActivity.this, android.R.layout.simple_spinner_item,
                                            reimbursementType); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spType.setAdapter(spinnerArrayAdapter);

                            getTransportmode();


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void getTransportmode() {
        progressDialog.show();
        String surl = "https://cloud.geniusconsultant.com/GeniusEssReim/api/Reimbursement/GetTransportMode";
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();
                        mTransModeList.clear();
                        transmodeList.clear();
                        mTransModeList.add(new SpinnerModelForReim("0",0,0));
                        transmodeList.add("Please Select");

                        try {
                            JSONArray job1 = new JSONArray(response);
                            for (int i=0;i<job1.length();i++){
                                JSONObject jobb=job1.getJSONObject(i);
                                String name=jobb.optString("TransportMode");
                                int id=jobb.optInt("TransportModeID");
                                SpinnerModelForReim spModel=new SpinnerModelForReim(name,id,0);
                                mTransModeList.add(spModel);
                                transmodeList.add(name);

                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (CliamManageActivity.this, android.R.layout.simple_spinner_item,
                                            transmodeList); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTransportMode.setAdapter(spinnerArrayAdapter);


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(LoginActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void checkEligbleorNot( int reimid) {
        progressDialog.show();
        String surl = "https://cloud.geniusconsultant.com/GeniusEssReim/api/Reimbursement/GetEligiblityForReimbursement?EmpID="+pref.getEmpId()+"&SubCategoryID="+reimid;
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                             IsEligible=job1.optInt("IsEligible");
                            double EligiblityAmount=job1.optDouble("EligiblityAmount");
                            String amount= String.valueOf(EligiblityAmount);
                            tvEligiBleAmount.setText(amount);
                            if (IsEligible==1){
                                if (reimID==211){
                                    llConvence.setVisibility(View.VISIBLE);
                                    llUtilities.setVisibility(View.GONE);
                                }else {
                                    llConvence.setVisibility(View.GONE);
                                    llUtilities.setVisibility(View.VISIBLE);
                                }
                            }else {
                                eligbleDialog();
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

                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void eligbleDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CliamManageActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_elligble, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al5.dismiss();
                llConvence.setVisibility(View.GONE);
                llUtilities.setVisibility(View.GONE);




            }
        });
        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });



        al5 = dialogBuilder.create();
        al5.setCancelable(false);
        Window window = al5.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al5.show();


    }


    private void saveClaim(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmployeeID", "207200087");
            jsonObject.put("CategoryID", "111");
            jsonObject.put("SubCategoryID", "210");
            jsonObject.put("MonthID", "6");
            jsonObject.put("ClaimAmount", "250");
            jsonObject.put("EligibleAmt", "0");
            jsonObject.put("SupportingDoc", null);
            jsonObject.put("Remarks", "Shekhar");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressDialog progressDialog=new ProgressDialog(CliamManageActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post("https://cloud.geniusconsultant.com/GeniusEssReim/api/Reimbursement/SaveClaimDetails")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });
    }
}