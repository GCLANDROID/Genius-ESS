package com.genius.employee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
/*import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity*/;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.DownloadTask;
import com.genius.employee.utility.Pref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppointmentLetterActivity extends AppCompatActivity {
    TextView text_view;
    Button btnAccept,btnDownlaod;
    String pdfPath;
    ImageView imgBack,imgHome;
    Pref pref;
    ProgressDialog mProgressDialog;
    AlertDialog alerDialog2;
    String downloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_letter);
        initView();
        appointmentLetterCheck();
        onClick();
    }

    private void initView(){
        pref=new Pref(AppointmentLetterActivity.this);
        text_view=(TextView)findViewById(R.id.text_view);
        btnAccept=(Button)findViewById(R.id.btnAccept);
        btnDownlaod=(Button)findViewById(R.id.btnDownlaod);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        mProgressDialog=new ProgressDialog(AppointmentLetterActivity.this);
        mProgressDialog.setMessage("Downloading...");
    }

    private void onClick(){
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DownloadTask(AppointmentLetterActivity.this, pdfPath);

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
                Intent intent=new Intent(AppointmentLetterActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnDownlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDownlaod.setEnabled(false);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadPath));
                startActivity(browserIntent);
            }
        });

    }

    private void appointmentLetterCheck() {
        final ProgressDialog pd=new ProgressDialog(AppointmentLetterActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        final String surl = APi.sManageAppointmentIsAcceptedApi+"EmpID="+pref.getSecureEmpId();
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
                            String responseCode = job1.optString("responseCode");

                            if (responseCode.equals("1")) {
                                text_view.setText("Your Appointment Letter is Ready to Accept");
                                btnAccept.setVisibility(View.VISIBLE);
                                btnDownlaod.setVisibility(View.GONE);
                                appointmentLetterget();

//Alert


                            } else {

                                text_view.setText("Your Appointment Letter is Already Accepted");
                                btnAccept.setVisibility(View.GONE);
                                btnDownlaod.setVisibility(View.VISIBLE);
                                appointmentLetterget();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AppointmentLetterActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AppointmentLetterActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void appointmentLetterget() {
        final ProgressDialog pd=new ProgressDialog(AppointmentLetterActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        final String surl = APi.sGetAppointmentApi+"EmpID="+pref.getSecureEmpId();
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
                            String responseCode = job1.optString("responseCode");

                            if (responseCode.equals("1")) {
                                JSONObject job=job1.optJSONObject("responseData");
                                String FILEPATH=job.optString("FILEPATH");
                                pdfPath=FILEPATH;
                                downloadPath=pdfPath.replace("http://111.93.182.170/GeniusHR/DGDoc/","https://cloud.geniusconsultant.com/GeniusESS/AttcRes/");


                            } else {



                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AppointmentLetterActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AppointmentLetterActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void download(){

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() ;
        java.io.File xmlFile = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/Filename.xml");
        AndroidNetworking.download(downloadPath, path,"Appointmentletter.pdf")
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .setPercentageThresholdForCancelling(50) // even if at the time of cancelling it will not cancel if 50%
                .build()                                 // downloading is done.But can be cancalled with forceCancel.
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        btnDownlaod.setEnabled(true);
                        successAlert("Your appointment letter has been downloaded successfully");
                        // do anything after completion

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(AppointmentLetterActivity.this,"error",Toast.LENGTH_LONG).show();
                    }
                });
    }




    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AppointmentLetterActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog2.dismiss();

            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(text);


        alerDialog2 = dialogBuilder.create();
        alerDialog2.setCancelable(false);
        Window window = alerDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog2.show();
    }
}