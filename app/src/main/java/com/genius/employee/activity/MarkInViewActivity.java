package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.adapter.MarkViewAdapter;
import com.genius.employee.model.MarkInViewModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MarkInViewActivity extends AppCompatActivity {
    RecyclerView rvItem;
    ArrayList<MarkInViewModel> itemList = new ArrayList();
    Button btnAdd;
    ImageView imgAdd;
    LinearLayout llLoader, llMain, llNoData;
    ProgressDialog pd, pd1;
    Pref pref;
    String formattedDate;
    TextView tvDate;
    ImageView imgBack, imgHome;
    GPSTracker gps;
    ;
    double latitude = 0.00, longitude = 0.00;
    String cuDate;
    int co;
    LinearLayout llAdd;
    FloatingActionButton fbAdd;
    private NetworkConnectionCheck connectionCheck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_location);
        initView();

        onClick();
    }

    private void initView() {
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(getApplicationContext());
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(MarkInViewActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        pd = new ProgressDialog(this);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
        Log.d("formattedDate", formattedDate);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(formattedDate);
        pd1 = new ProgressDialog(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);

        gps = new GPSTracker(MarkInViewActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            longitude = gps.getLongitude();
        } else {
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        }

        String[] sep = formattedDate.split("-");
        String date = sep[0];
        String month = sep[1];
        String year = sep[2];
        if (month.contains("Jan")) {
            co = 1;
        } else if (month.contains("Feb")) {
            co = 2;
        } else if (month.contains("Mar")) {
            co = 3;
        } else if (month.contains("Apr")) {
            co = 4;
        } else if (month.contains("May")) {
            co = 5;
        } else if (month.contains("Jun")) {
            co = 6;
        } else if (month.contains("Jul")) {
            co = 7;
        } else if (month.contains("Aug")) {
            co = 8;
        } else if (month.contains("Sep")) {
            co = 9;
        } else if (month.contains("Oct")) {
            co = 10;
        } else if (month.contains("Nov")) {
            co = 11;
        } else if (month.contains("Dec")) {
            co = 12;
        }

        cuDate = co + "-" + date + "-" + year;
        Log.d("cuDate", cuDate);
        llAdd = (LinearLayout) findViewById(R.id.llAdd);
        fbAdd = (FloatingActionButton) findViewById(R.id.fbAdd);


    }

    private void getItem() {
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        String surl = APi.sGetOfflineDailyLogActivityApi+"AEMEmployeeID=" + pref.getSecureEmpId() + "&Year=0&Month=0&AttendanceDate=" + cuDate + "&Operation=1";
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();
                        itemList.clear();

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

                                    String PunchInTime = obj.optString("PunchInTime");
                                    String AddressIN = obj.optString("AddressIN");
                                    String RemarksIN = obj.optString("RemarksIN");
                                    String FNameIN = obj.optString("FNameIN");
                                    String imgUrlIN = obj.optString("imgUrlIN");
                                    String AccessFrom=obj.optString("AccessFrom");
                                    String PunchOutTime=obj.optString("PunchOutTime");

                                    MarkInViewModel obj2 = new MarkInViewModel(AddressIN, PunchInTime, imgUrlIN, FNameIN, RemarksIN, "");
                                    obj2.setAccessForm(AccessFrom);
                                    obj2.setPunchOut(PunchOutTime);
                                    itemList.add(obj2);


                                }
                                setAdapter();
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);

                            } else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MarkInViewActivity.this);
        requestQueue.add(stringRequest);
    }

    private void setAdapter() {
        MarkViewAdapter vAdapter = new MarkViewAdapter(itemList, getApplicationContext());
        rvItem.setAdapter(vAdapter);
    }

    private void onClick() {


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    pd.setMessage("Loading....");
                    pd.setCancelable(false);
                    pd.show();
                    Intent intent = new Intent(MarkInViewActivity.this, MarkInManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });


        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionCheck.isNetworkAvailable()) {
                    pd.setMessage("Loading....");
                    pd.setCancelable(false);
                    pd.show();
                    Intent intent = new Intent(MarkInViewActivity.this, MarkInManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
                }


            }
        });

        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionCheck.isNetworkAvailable()) {
                    pd.setMessage("Loading....");
                    pd.setCancelable(false);
                    pd.show();
                    Intent intent = new Intent(MarkInViewActivity.this, MarkInManageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    connectionCheck.getNetworkActiveAlert().show();
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
                Intent intent = new Intent(MarkInViewActivity.this, EDashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        pd.dismiss();
        pd1.dismiss();
    }

    private void showFaceAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please recognize your face to give attendance");
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                       /* Intent intent = new Intent(MarkInViewActivity.this, FaceRecognitation.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        arg0.dismiss();*/
                    }
                });
        alertDialogBuilder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getItem();
    }
}
