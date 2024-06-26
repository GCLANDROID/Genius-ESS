package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyInTimeActivity extends AppCompatActivity {
    Button btnIN;
    Pref pref;
    TextView tvClientname;
    GPSTracker gps;
    double latitude, longitude;
    TextView tvAddress, tvRegTime;
    String ID;
    AlertDialog alerDialog1;
    String cuuaddress="";
    String sLat, sLong;
    TextView tvInTime;
    ProgressDialog pd;
    LinearLayout llLoader,llRetry,llPD;
    ImageView imgBack,imgHome;
    TextView tvText;
    LinearLayout llLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_in_time);
        initialize();
        getvalue();
        onClick();
    }

    private void initialize() {
        pref = new Pref(getApplicationContext());
        btnIN = (Button) findViewById(R.id.btnIN);
        tvClientname = (TextView) findViewById(R.id.tvClientname);
        tvRegTime = (TextView) findViewById(R.id.tvRegTime);


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

        cuuaddress = getAddress(latitude, longitude);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAddress.setText(cuuaddress);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();
        // Change base URL to your upload server URL.



        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String str = sdf.format(new Date());

        tvInTime = (TextView) findViewById(R.id.tvInTime);
        String dateTime = formattedDate + " " + str;
        tvInTime.setText(dateTime);
        pd = new ProgressDialog(DailyInTimeActivity.this);
        pd.setMessage("Loading...");
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llRetry=(LinearLayout)findViewById(R.id.llRetry);
        llPD=(LinearLayout)findViewById(R.id.llPD);
        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);
        tvText=(TextView)findViewById(R.id.tvText);
        llLoad=(LinearLayout)findViewById(R.id.llLoad);




    }

    private void onClick() {
        btnIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cuuaddress.equals("")||cuuaddress!=null) {

                    postFunction();
                }else {
                    Toast.makeText(DailyInTimeActivity.this,"Sorry!your cuurent address not found.Please check your google map",Toast.LENGTH_LONG).show();
                }
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
                Intent intent=new Intent(DailyInTimeActivity.this,EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        llRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalue();
            }
        });
    }


    private void getvalue() {

        String surl = APi.sClientVisitApi+"EmpID=" + pref.getSecureEmpId();
        Log.d("compurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        llRetry.setVisibility(View.GONE);
        llPD.setVisibility(View.VISIBLE);
        btnIN.setEnabled(false);
        imgHome.setEnabled(false);
        imgBack.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

                        llLoader.setVisibility(View.GONE);
                        btnIN.setEnabled(true);
                        imgHome.setEnabled(true);
                        imgBack.setEnabled(true);


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
                                    String ClientName = obj.optString("ClientName");
                                    tvClientname.setText(ClientName);
                                    ID = obj.optString("ID");
                                    String CreatedOn = obj.optString("CreatedOn");
                                    tvRegTime.setText(CreatedOn);

                                }


                                btnIN.setEnabled(true);
                                imgHome.setEnabled(true);
                                imgBack.setEnabled(true);




                            } else {


                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DailyInTimeActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llRetry.setVisibility(View.VISIBLE);
                llPD.setVisibility(View.GONE);
                btnIN.setEnabled(false);
                imgHome.setEnabled(false);
                imgBack.setEnabled(false);
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
                8000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(DailyInTimeActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
    }




    private void postFunction() {
        AndroidNetworking.upload(APi.sAddVisitLocationMasterApi)
                .addMultipartParameter("ClientVisitMasterID", ID)
                .addMultipartParameter("LocationLat", sLat)
                .addMultipartParameter("LocationLon", sLong)
                .addMultipartParameter("LocationAddress", cuuaddress)
                .addMultipartParameter("EmployeeId", pref.getSecureEmpId())
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        llLoad.setVisibility(View.VISIBLE);
                        btnIN.setVisibility(View.GONE);
                        tvText.setVisibility(View.GONE);

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
                            btnIN.setVisibility(View.GONE);
                            tvText.setVisibility(View.GONE);

                        }else
                        {
                            llLoad.setVisibility(View.GONE);
                            btnIN.setVisibility(View.VISIBLE);
                            tvText.setVisibility(View.GONE);

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        llLoad.setVisibility(View.GONE);
                        btnIN.setVisibility(View.VISIBLE);
                        tvText.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
                    }
                });
    }


    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DailyInTimeActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                pref.saveInFlag("1");
                Intent intent = new Intent(getApplicationContext(), DailyActivityDashBoardActivity.class);
                startActivity(intent);
                finish();
                stopService();
            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your In-Time has been marked successfully");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    public String getAddress(double lat, double lng) {
        String address = null;
        Geocoder geocoder = new Geocoder(DailyInTimeActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            address = add;
            ;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return address;
    }

    private void stopService() {

        Intent startIntent = new Intent(this, LocationTrackingService.class);
        //startIntent.setAction(Constants.ACTION.START_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopService(startIntent);
        } else {
            stopService(startIntent);
        }
    }
}
