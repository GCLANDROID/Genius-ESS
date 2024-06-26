package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity*/;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.genius.employee.R;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class AttendanceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    LinearLayout llAttandanceManage, llAttendanceReport, llBackAttendance, llOnLeave, llWeekly, llApproval, llWeekly1;
    ImageView imgBack, imgHome;

    String month, year;
    int y;
    Pref pref;
    NetworkConnectionCheck connectionCheck;
    int flag;
    LinearLayout llManageD, llmanageD1, llReportD, llReportD1, llBackD, llBackD1, llLeaveD, llLeaveD1, llWeekD, llWeekD1, llWeel1D, llWeek1D1;
    String formattedDate;
    String AttendanceType;
    String ApproverStatus;
    GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initialize();
        onClick();

    }

    private void initialize() {
        pref = new Pref(getApplicationContext());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        formattedDate = df.format(c);


        y = Calendar.getInstance().get(Calendar.YEAR);
        year = String.valueOf(y);
        Log.d("year", year);

        int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Log.d("month", String.valueOf(m));
        if (m == 1) {
            month = "January";
        } else if (m == 2) {
            month = "February";
        } else if (m == 3) {
            month = "March";
        } else if (m == 4) {
            month = "April";
        } else if (m == 5) {
            month = "May";
        } else if (m == 6) {
            month = "June";
        } else if (m == 7) {
            month = "July";
        } else if (m == 8) {
            month = "August";
        } else if (m == 9) {
            month = "September";
        } else if (m == 10) {
            month = "October";
        } else if (m == 11) {
            month = "November";
        } else if (m == 12) {
            month = "December";
        }

        if (pref.getWeeklyoff().equals("1")) {
            llWeekly.setVisibility(View.VISIBLE);

        } else {
            llWeekly.setVisibility(View.GONE);
            llWeekly1.setVisibility(View.GONE);
        }

        if (pref.getOnLeave().equals("1")) {
            llOnLeave.setVisibility(View.VISIBLE);
            llWeekly.setVisibility(View.VISIBLE);

        } else {
            llOnLeave.setVisibility(View.GONE);
            llWeekly.setVisibility(View.GONE);


        }

        if (pref.getBackAttd().equals("1")) {
            llBackAttendance.setVisibility(View.VISIBLE);
        } else {
            llBackAttendance.setVisibility(View.GONE);
            llWeekly.setVisibility(View.GONE);
        }
        llApproval = (LinearLayout) findViewById(R.id.llApproval);
        if (pref.getSup().equals("0")) {
            llApproval.setVisibility(View.GONE);
        } else {
            llApproval.setVisibility(View.VISIBLE);
        }
    }

    private void onClick() {
        llAttandanceManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    llManageD.setVisibility(View.GONE);
                    llmanageD1.setVisibility(View.VISIBLE);
                    llReportD.setVisibility(View.VISIBLE);
                    llReportD1.setVisibility(View.GONE);
                    llBackD.setVisibility(View.VISIBLE);
                    llBackD1.setVisibility(View.GONE);
                    llLeaveD.setVisibility(View.VISIBLE);
                    llLeaveD1.setVisibility(View.GONE);
                    llWeekD.setVisibility(View.VISIBLE);
                    llWeekD1.setVisibility(View.GONE);
                    llWeel1D.setVisibility(View.VISIBLE);
                    llWeek1D1.setVisibility(View.GONE);

                        /*Intent intent = new Intent(AttendanceActivity.this, AttendanceManageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/

                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llAttendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionCheck.isNetworkAvailable()) {
                    llManageD.setVisibility(View.VISIBLE);
                    llmanageD1.setVisibility(View.GONE);
                    llReportD.setVisibility(View.GONE);
                    llReportD1.setVisibility(View.VISIBLE);
                    llBackD.setVisibility(View.VISIBLE);
                    llBackD1.setVisibility(View.GONE);
                    llLeaveD.setVisibility(View.VISIBLE);
                    llLeaveD1.setVisibility(View.GONE);
                    llWeekD.setVisibility(View.VISIBLE);
                    llWeekD1.setVisibility(View.GONE);
                    llWeel1D.setVisibility(View.VISIBLE);
                    llWeek1D1.setVisibility(View.GONE);
                   /* Intent intent = new Intent(AttendanceActivity.this, AttendanceReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });



        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceActivity.this, EDashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //  finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }





    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
