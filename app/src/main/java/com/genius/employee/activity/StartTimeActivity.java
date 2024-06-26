package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.employee.R;
import com.genius.employee.model.SpinnerModel;
import com.genius.employee.service.LocationTrackingService;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.Pref;
import com.genius.employee.utility.RetrofitService;
import com.genius.employee.utility.UploadObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartTimeActivity extends AppCompatActivity {
    Button btnReg;
    Spinner spPurpose;
    EditText etClintName, etContactperson, etRemarks;
    ArrayList<SpinnerModel> mPurposeList = new ArrayList();
    ArrayList<String> purposeList = new ArrayList();
    Pref pref;
    String purposeId = "";
    AlertDialog alerDialog1;
    TextView tvDate;
    ImageView imgBack, imgHome;

    GPSTracker gps;
    double latitude, longitude;
    String sLat="", sLong="";
    ProgressDialog pd;
    LinearLayout llLoader, llRetry, llPD, llLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_time);
        init();
        setPurposeItem();
        onClick();
    }

    private void init() {
        pref = new Pref(getApplicationContext());
        btnReg = (Button) findViewById(R.id.btnReg);
        etClintName = (EditText) findViewById(R.id.etClintName);
        etContactperson = (EditText) findViewById(R.id.etContactperson);
        spPurpose = (Spinner) findViewById(R.id.spPurpose);
        etRemarks = (EditText) findViewById(R.id.etRemarks);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // Change base URL to your upload server URL.

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();


        tvDate = (TextView) findViewById(R.id.tvDate);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());


        String dateTime = formattedDate + " " + str;
        tvDate.setText(dateTime);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);


        gps = new GPSTracker(this);

// check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            sLat = String.valueOf(latitude);
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
            sLong = String.valueOf(longitude);
        } else {

            gps.showSettingsAlert();
        }
        pd = new ProgressDialog(StartTimeActivity.this);
        pd.setMessage("Loading..");
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llRetry = (LinearLayout) findViewById(R.id.llRetry);
        llPD = (LinearLayout) findViewById(R.id.llPD);
        llLoad = (LinearLayout) findViewById(R.id.llLoad);



    }

    private void onClick() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latitude!=0.0) {
                    if (etClintName.getText().toString().length() > 0) {
                        if (etContactperson.getText().toString().length() > 0) {
                            if (!purposeId.equals("")) {

                                postFunction();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select your purpose of visit", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            etContactperson.requestFocus();
                            etContactperson.setError("Please enter contact person name");
                        }
                    } else {
                        etClintName.requestFocus();
                        etClintName.setError("Please enter client name");
                    }
                }else {
                    Toast.makeText(StartTimeActivity.this,"Sorry!your current address not found.Please check your google map",Toast.LENGTH_LONG).show();

                }


            }
        });
        spPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                purposeId = "";
                if (position > 0) {
                    purposeId = mPurposeList.get(position).getItemId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPurposeItem();
            }
        });
    }

    private void setPurposeItem() {

        String surl = APi.sUrl+"ClientVisit/VisitPurposeMaster";
        Log.d("compurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llRetry.setVisibility(View.GONE);
        llPD.setVisibility(View.VISIBLE);
        btnReg.setEnabled(false);
        imgHome.setEnabled(false);
        imgBack.setEnabled(false);
        etClintName.setEnabled(false);
        etContactperson.setEnabled(false);
        etRemarks.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        llLoader.setVisibility(View.GONE);
                        btnReg.setEnabled(true);
                        imgHome.setEnabled(true);
                        imgBack.setEnabled(true);
                        etClintName.setEnabled(true);
                        etContactperson.setEnabled(true);
                        etRemarks.setEnabled(true);
                        mPurposeList.clear();
                        purposeList.clear();
                        mPurposeList.add(new SpinnerModel("----SELECT------", ""));
                        purposeList.add("----------SELECT------------");


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
                                    String VisitPurposeName = obj.optString("VisitPurposeName");
                                    String VisitPurposeID = obj.optString("VisitPurposeID");
                                    purposeList.add(VisitPurposeName);
                                    SpinnerModel mainDocModule = new SpinnerModel(VisitPurposeName, VisitPurposeID);
                                    mPurposeList.add(mainDocModule);

                                }
                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                        (StartTimeActivity.this, android.R.layout.simple_spinner_item,
                                                purposeList); //selected item will look like a spinner set from XML
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPurpose.setAdapter(spinnerArrayAdapter);


                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StartTimeActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llRetry.setVisibility(View.VISIBLE);
                llPD.setVisibility(View.GONE);
                btnReg.setEnabled(false);
                imgHome.setEnabled(false);
                imgBack.setEnabled(false);
                etClintName.setEnabled(false);
                etContactperson.setEnabled(false);
                etRemarks.setEnabled(false);
                //   Toast.makeText(DocumentManageActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }


    private void postFunction() {

        AndroidNetworking.upload(APi.sAddClientVisitMasterApi)
                .addMultipartParameter("ID", "0")
                .addMultipartParameter("ClientName", etClintName.getText().toString())
                .addMultipartParameter("ClientContactPerson", etContactperson.getText().toString())
                .addMultipartParameter("EmployeeId", pref.getSecureEmpId())
                .addMultipartParameter("VisitPurpose", purposeId)
                .addMultipartParameter("Remark", etRemarks.getText().toString())
                .addMultipartParameter("PunchLat", sLat)
                .addMultipartParameter("PunchLon", sLong)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);
                        btnReg.setVisibility(View.GONE);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        if (responseText.equals("Success")) {
                            successAlert();
                            llLoad.setVisibility(View.VISIBLE);
                            btnReg.setVisibility(View.GONE);
                        } else {
                            llLoad.setVisibility(View.GONE);
                            btnReg.setVisibility(View.VISIBLE);

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        llLoad.setVisibility(View.GONE);
                        btnReg.setVisibility(View.VISIBLE);
                    }
                });


    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartTimeActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                pref.saveInFlag("3");
                Intent intent = new Intent(getApplicationContext(), DailyActivityDashBoardActivity.class);
                startActivity(intent);
                finish();
                startTracking();
            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void startTracking() {

        Intent startIntent = new Intent(this, LocationTrackingService.class);
        //startIntent.setAction(Constants.ACTION.START_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            startService(startIntent);
        }
    }


}
