package com.genius.employee.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
/*import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;*/
/*import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;*/
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.genius.employee.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.employee.model.MarkInViewModel;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.AppController;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.NetworkConnectionCheck;
import com.genius.employee.utility.Pref;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class EDashBoardActivity extends AppCompatActivity {
    private static final String TAG = "EDashBoardActivity";
    LinearLayout llProfile, llLeave, llAttendance, llPayroll, llDeclaration, llApprisel;
    TextView tvName;
    Pref pref;
    boolean doubleBackToExitPressedOnce = false;
    TextView tvGreeting, tvTime;
    NetworkConnectionCheck connectionCheck;
    int MY_SOCKET_TIMEOUT_MS = 30000;
    boolean responseStatus;
    String playversion;
    String version;
    AlertDialog alertDialog, al1, al2, al3, al4, al5, al6, al7;
    LinearLayout llDailyActivity;
    String lebelId;
    String deptId;
    LinearLayout llLoader, llLogout;
    ArrayList<String> arraList = new ArrayList();
    LinearLayout llHoliday, llFeedBack, llMarkIn, llCP, llAppointment, llReimbursement, llPF, llVoiceAssistant, llFAQ;
    boolean responseStatusForDec;
    String refreshedToken, uniqueID;
    String workingStatus;
    String appointmentletter;
    String covidCode;
    TextView tvPasswordText;
    LinearLayout llPassword;

    String skipCode, jrCode;
    GPSTracker gps;
    LinearLayout llAdmin;

    String toolTipsCode = "0";
    String pfURL;
    LinearLayout llSuggestion;
    FloatingActionButton fbQR;
    DrawerLayout dlMain;
    boolean mslideState;
    ImageView imgMenu, imglogout;
    TextView tvsEmpName;
    ImageView imgUser;
    ImageView imgVoice;
    LinearLayout llLeaveApplication,llESSWeb,llInsurance;
    String cuDate;
    int co;
    String formattedDate;
    TextView  tvLoginTime;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dash_board);
        initialize();


            if (!pref.getLogOutFlag().equals("1")) {
                loginFunction();
            } else {
                workingStatus();
            }

        onClick();
    }

    private void initialize() {
        tvLoginTime=(TextView)findViewById(R.id.tvLoginTime);
        Date cd = Calendar.getInstance().getTime();
        SimpleDateFormat def = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = def.format(cd);
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
        pref = new Pref(getApplicationContext());
        imgVoice=(ImageView)findViewById(R.id.imgVoice);
        fbQR = (FloatingActionButton) findViewById(R.id.fbQR);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        try {
            byte[] decodedString = Base64.decode(pref.getProfileImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgUser.setImageBitmap(decodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        connectionCheck = new NetworkConnectionCheck(EDashBoardActivity.this);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llFAQ = (LinearLayout) findViewById(R.id.llFAQ);
        llLeave = (LinearLayout) findViewById(R.id.llLeave);
        llVoiceAssistant = (LinearLayout) findViewById(R.id.llVoiceAssistant);
        llSuggestion = (LinearLayout) findViewById(R.id.llSuggestion);
        llAttendance = (LinearLayout) findViewById(R.id.llAttendance);
        llLeaveApplication = (LinearLayout) findViewById(R.id.llLeaveApplication);
        llESSWeb=(LinearLayout)findViewById(R.id.llESSWeb);
        llInsurance=(LinearLayout)findViewById(R.id.llInsurance);
        llPayroll = (LinearLayout) findViewById(R.id.llPayroll);
        tvName = (TextView) findViewById(R.id.tvName);
        tvsEmpName = (TextView) findViewById(R.id.tvsEmpName);
        tvName.setText(pref.getEmpName());
        tvsEmpName.setText(pref.getEmpName());
        tvGreeting = (TextView) findViewById(R.id.tvGreeting);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            tvGreeting.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            tvGreeting.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            tvGreeting.setText("Good Evening");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {

            tvGreeting.setText("Good Evening");
        }

        Date d = Calendar.getInstance().getTime();
        System.out.println("Current time => " + d);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(d);

        Date e = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf.format(e);

        String datetime = formattedDate + " " + currentDateTimeString;

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText(datetime);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("sddk", version);
            Log.d("sdkl", String.valueOf(verCode));
        } catch (PackageManager.NameNotFoundException f) {
            f.printStackTrace();
        }

        llDailyActivity = (LinearLayout) findViewById(R.id.llDailyActivity);
        llAdmin = (LinearLayout) findViewById(R.id.llAdmin);
        if (pref.getEmpId().equalsIgnoreCase("2070002087")) {
            llAdmin.setVisibility(View.VISIBLE);
        } else {
            llAdmin.setVisibility(View.GONE);
        }
        llLoader = (LinearLayout) findViewById(R.id.llLoader);

        llLogout = (LinearLayout) findViewById(R.id.llLogout);
        llHoliday = (LinearLayout) findViewById(R.id.llHoliday);
        llFeedBack = (LinearLayout) findViewById(R.id.llFeedBack);
        llMarkIn = (LinearLayout) findViewById(R.id.llMarkIn);
        llCP = (LinearLayout) findViewById(R.id.llCP);
        llDeclaration = (LinearLayout) findViewById(R.id.llDeclaration);
        llAppointment = (LinearLayout) findViewById(R.id.llAppointment);
        llReimbursement = (LinearLayout) findViewById(R.id.llReimbursement);
        llApprisel = (LinearLayout) findViewById(R.id.llApprisel);
        llPF = (LinearLayout) findViewById(R.id.llPF);
        refreshedToken = "101";
        uniqueID = UUID.randomUUID().toString();
        llAttendance.setEnabled(false);
        llDailyActivity.setEnabled(false);
        llPayroll.setEnabled(false);
        llProfile.setEnabled(false);
        llLeave.setEnabled(false);
        llMarkIn.setEnabled(false);
        llPassword = (LinearLayout) findViewById(R.id.llPassword);
        tvPasswordText = (TextView) findViewById(R.id.tvPasswordText);
        gps = new GPSTracker(EDashBoardActivity.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            Log.d("saikatdas", String.valueOf(latitude));
            double longitude = gps.getLongitude();
            APi.lattitude = latitude;
            APi.longitude = longitude;
        } else {}

        SharedPreferences prefs = getSharedPreferences("com.genius.employee", MODE_PRIVATE);

        int launch_count = prefs.getInt("launch_count", 0);



        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@androidx.annotation.NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@androidx.annotation.NonNull View view) {
                mslideState = true;

            }

            @Override
            public void onDrawerClosed(@androidx.annotation.NonNull View view) {
                mslideState = false;

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        imglogout = (ImageView) findViewById(R.id.imglogout);

        getToolTipsShowingStatus();

    }

    private void onClick() {
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlMain.openDrawer(Gravity.LEFT);
            }
        });
        llInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EDashBoardActivity.this, InsuranceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        fbQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EDashBoardActivity.this, QRCodeScannerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("qrflag",1);
                startActivity(intent);
            }
        });

        llESSWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EDashBoardActivity.this, QRCodeScannerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("qrflag",2);
                startActivity(intent);
            }
        });

        llFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFAQBrowser();
            }
        });

        llSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, FeatureSuggestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llLeaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, LeaveApplicationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, FirebaseAdminActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        llPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");
            }
        });

        llVoiceAssistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, VoiceAssistantActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");
            }
        });

        imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, VoiceAssistantActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");
            }
        });

        llApprisel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jrCode.equals("Jr.")) {
                    apprisalPdfForView();
                } else {
                    apprisalPdfForViewSnr();
                }
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (workingStatus.equals("1")) {
                        Intent intent = new Intent(EDashBoardActivity.this, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    } else {
                        Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llReimbursement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (workingStatus.equals("1")) {
                        Intent intent = new Intent(EDashBoardActivity.this, ReimbursementDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    } else {
                        Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (workingStatus.equals("1")) {
                        pfLinkget();
                    } else {
                        Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });
        llAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EDashBoardActivity.this, AppointmentLetterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llMarkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workingStatus.equals("1")) {
                    Intent intent = new Intent(EDashBoardActivity.this, MarkInAttendanceDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    customType(EDashBoardActivity.this, "left-to-right");
                } else {
                    Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    customType(EDashBoardActivity.this, "left-to-right");
                }
            }
        });

        llDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        llDailyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isGPSEnabled()) {
                    getLebelId();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enable your GPS", Toast.LENGTH_LONG).show();
                }
            }
        });

        llLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (workingStatus.equals("1")) {
                        Intent intent = new Intent(EDashBoardActivity.this, LeaveBalanceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    } else {
                        Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    }

                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }

            }
        });

        llCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EDashBoardActivity.this, ChangePasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");
            }
        });

        llAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    if (workingStatus.equals("1")) {
                        Intent intent = new Intent(EDashBoardActivity.this, AttendanceReportActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    } else {
                        Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        customType(EDashBoardActivity.this, "left-to-right");
                    }
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }
            }
        });

        llPayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionCheck.isNetworkAvailable()) {
                    Intent intent = new Intent(EDashBoardActivity.this, PayrollActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    customType(EDashBoardActivity.this, "left-to-right");
                } else {
                    connectionCheck.getNetworkActiveAlert().show();
                }

            }
        });

        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                pref.saveNormalFlag("1");
                pref.saveVersionHitFlag("2");
                pref.saveLoginFlag("2");
                pref.saveLogOutFlag("1");
            }
        });

        llHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, HolidayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");
            }
        });

        llFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EDashBoardActivity.this, FeedbackLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                pref.saveVersionHitFlag("2");
                pref.saveLoginFlag("2");
            }
        }, 2000);
    }


    public void loginFunction(){
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();


        AndroidNetworking.post(APi.sLoginApi)
                .addBodyParameter("username", pref.getMasterId())
                .addBodyParameter("password", pref.getPassword())
                .addBodyParameter("grant_type", "password")
                //.addHeaders("Content-Type: application/x-www-form-urlencoded")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "LOGIN: "+response);
                        progressBar.dismiss();
                        if (response.has("access_token")) {
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
                            getDetailsOfUser();
                            // saveDeviceID(); 404 error



                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.dismiss();
                        Log.e(TAG, "LOGIN_error: "+anError.getErrorBody());
                        try {
                            JSONObject jobError = new JSONObject(anError.getErrorBody());
                            Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                            startActivity(intent);
                            customType(EDashBoardActivity.this, "left-to-right");
                            finish();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void getDetailsOfUser() {
        final ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Loading...");
        progressBar.show();
        progressBar.show();

        String surl = APi.sPostLoginApi+"id1="+pref.getSecureEmpId()+"&id2="+pref.getPassword();
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
                            tvName.setText(UserName);
                            String UserID=obj.optString("UserID");
                            pref.saveEmpId(UserID);
                            String profileImage=obj.optString("profileImage");
                            pref.saveProfileImage(profileImage);
                            String DepartmentID=obj.optString("DepartmentID");
                            pref.saveDeptId(DepartmentID);
                            String MasterPage=obj.optString("MasterPage");
                            pref.saveBranchId(MasterPage);
                            workingStatus();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // attendabceInfiList.clear();



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.dismiss();
                Toast.makeText(EDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();
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

    private void noticeAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView tvLink = (TextView) dialogView.findViewById(R.id.tvLink);
        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://111.93.182.170/GeniusEAM/DocFile/FAQ_EMP_.pdf"));
                startActivity(browserIntent);
                al1.dismiss();
            }
        });


        LinearLayout lnCancel = (LinearLayout) dialogView.findViewById(R.id.lnCancel);

        lnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al1.dismiss();
            }
        });

        al1 = dialogBuilder.create();
        al1.setCancelable(false);
        Window window = al1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al1.show();
    }

    private void checkBersion() {

        Log.d("hit", "1");
        pref.saveVersionHitFlag("1");
        //https://www.cloud.geniusconsultant.com/GeniusESS/API/Utility/Get_apkversion
        String surl = APi.sGet_apkversionApi;
        Log.d("version", surl);
        llLoader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            playversion = job1.optString("responseText");
                            Log.d("playvcersion", playversion);
                            responseStatus = job1.optBoolean("responseStatus");
                            try {
                                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                                version = pInfo.versionName;
                                int verCode = pInfo.versionCode;
                                Log.d("sddk", version);
                                Log.d("sdkl", String.valueOf(verCode));
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            if (version.equals(playversion)) {

                                passwordExpCheck();

                            } else {
                                Intent intent = new Intent(EDashBoardActivity.this, UpdateActivity.class);
                                startActivity(intent);
                                finish();
                            }


                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void workingStatus() {
        llLoader.setVisibility(View.VISIBLE);
        llAttendance.setEnabled(false);
        llDailyActivity.setEnabled(false);
        llPayroll.setEnabled(false);
        llProfile.setEnabled(false);
        llLeave.setEnabled(false);
        llMarkIn.setEnabled(false);
        Log.d("hit", "1");
        pref.saveVersionHitFlag("1");
        String surl = APi.sGetLastWorkingDayApi + "EmployeeId=" + pref.getSecureEmpId();
        Log.d("version", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            String responseCode = job1.optString("responseCode");
                            if (responseCode.equals("1")) {
                                llSuggestion.setVisibility(View.VISIBLE);
                            } else {
                                llSuggestion.setVisibility(View.GONE);
                            }
                            workingStatus = responseText;
                            //String responseText="0";
                            if (responseText.equals("0")) {
                                llLoader.setVisibility(View.GONE);
                                Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                checkBersion();
                            }


                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void passwordExpCheck() {
        llAttendance.setEnabled(false);
        llDailyActivity.setEnabled(false);
        llPayroll.setEnabled(false);
        llProfile.setEnabled(false);
        llLeave.setEnabled(false);
        llMarkIn.setEnabled(false);
        Log.d("hit", "1");
        pref.saveVersionHitFlag("1");
        //https://cloud.geniusconsultant.com/GeniusESS/API/Utility/Get_apkversion
        String surl = APi.sisPasswordExpireApi + "EmpID=" + pref.getSecureEmpId();
        Log.d("passwordckurl", surl);
        llLoader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLeave", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            String responseData = job1.optString("responseData");
                            String responseCode = job1.optString("responseCode");
                            jrCode = job1.optString("responseID");
                            int count = Integer.parseInt(responseData);
                            if (count > 1) {
                                tvPasswordText.setText(responseData + " days left for your password to expire. ");
                            } else {
                                tvPasswordText.setText(responseData + " day left for your password to expire. ");
                            }
                            if (count < 4 && count > 0) {
                                llPassword.setVisibility(View.VISIBLE);

                            } else {
                                llPassword.setVisibility(View.GONE);
                            }
                            if (responseCode.equals("0")) {
                                showPasswordDialog(responseText);
                            } else {
                                saveDeviceID();
                            }


                            // boolean _status = job1.getBoolean("status")

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.VISIBLE);
                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void getLebelId() {
        final ProgressDialog pd = new ProgressDialog(EDashBoardActivity.this);
        pd.setMessage("loading...");
        pd.setCancelable(false);
        pd.show();

        final String surl = APi.sManageEmployeeHierarchyApi + "EmployeeID=" + pref.getSecureEmpId();
        Log.d("inputLogin", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);

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
                                    lebelId = obj.optString("LevelID");
                                    pref.saveLebelId(lebelId);
                                    deptId = obj.optString("DepartmentID");
                                    pref.saveDepttId(deptId);
                                    arraList.add(deptId);

                                }
                                int size = arraList.size();
                                pref.saveArraySize(size);
                                Log.d("zz", String.valueOf(size));
                                pd.dismiss();
                                Intent intent = new Intent(EDashBoardActivity.this, DailyActivityDashBoardActivity.class);
                                startActivity(intent);
                                customType(EDashBoardActivity.this, "left-to-right");

                            } else {

                                pd.dismiss();

                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();

                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("Get_Lebel_Id_erreo", error.toString());
                open();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EDashBoardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private void showPasswordDialog(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_invalidcredential, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al2.dismiss();
                Intent intent = new Intent(EDashBoardActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //tvText
        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        tvText.setText(text);
        al2 = dialogBuilder.create();
        al2.setCancelable(false);
        Window window = al2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al2.show();


    }

    private void appointmentLetterCheck() {
        llLoader.setVisibility(View.VISIBLE);
        final String surl = APi.sManageAppointmentIsAcceptedApi + "EmpID=" + pref.getSecureEmpId();
        Log.e(TAG,"Manage_Appointment_URL: "+surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,"Manage_Appointment: "+ response);

                        llLoader.setVisibility(View.VISIBLE);
                        llAttendance.setEnabled(false);
                        llDailyActivity.setEnabled(false);
                        llPayroll.setEnabled(false);
                        llProfile.setEnabled(false);
                        llLeave.setEnabled(false);
                        llMarkIn.setEnabled(false);
                        if (jrCode.equals("Jr.")) {
                            bulkApparisel();
                        } else {
                            apparisel();
                        }

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG, "ManageAppointment: " + job1);
                            String responseCode = job1.optString("responseCode");
                            appointmentletter = responseCode;

                            if (responseCode.equals("1")) {
                                appointmentDialog();
                            } else {

                            }


                            if (appointmentletter.equals("2")) {
                                llAppointment.setVisibility(View.VISIBLE);
                            } else {
                                llAppointment.setVisibility(View.GONE);
                            }

                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e(TAG,"Manage_Appointment_error: "+error.toString());
                open();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(EDashBoardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void appriselLetterCheck() {
        llLoader.setVisibility(View.VISIBLE);
        final String surl = APi.sBulkAppLetterIsAcceptedApi + "EmpID=" + pref.getSecureEmpId();
        Log.d(TAG,"Apprisel_Letter_URL"+surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Apprisel_Letter_Response: "+response);
                        llLoader.setVisibility(View.GONE);
                        llAttendance.setEnabled(true);
                        llDailyActivity.setEnabled(true);
                        llPayroll.setEnabled(true);
                        llProfile.setEnabled(true);
                        llLeave.setEnabled(true);
                        llMarkIn.setEnabled(true);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("Apprisel_Letter", "@@@@@@" + job1);
                            String responseCode = job1.optString("responseCode");
                            appointmentletter = responseCode;

                            if (responseCode.equals("1")) {
                                appriselLetterDialog();

                            } else if (responseCode.equals("2")) {
                                llApprisel.setVisibility(View.VISIBLE);


                            }

                            // covidDecCheck();


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e(TAG,"Apprisel_Letter_error: "+ error.toString());
                open();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EDashBoardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void appriselLetterCheckForSnr() {
        llLoader.setVisibility(View.VISIBLE);
        final String surl = APi.sSrAppLetterIsAcceptedApi + "EmpID=" + pref.getSecureEmpId();
        Log.d(TAG,"Apprisel_Letter_Check_For_Snr_URL: "+ surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Apprisel_Letter_Check_For_Snr_Response"+ response);
                        llLoader.setVisibility(View.GONE);
                        llAttendance.setEnabled(true);
                        llDailyActivity.setEnabled(true);
                        llPayroll.setEnabled(true);
                        llProfile.setEnabled(true);
                        llLeave.setEnabled(true);
                        llMarkIn.setEnabled(true);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e(TAG, "Apprisel_Letter_Check_For_Snr: " + job1);
                            String responseCode = job1.optString("responseCode");
                            appointmentletter = responseCode;

                            if (responseCode.equals("1")) {
                                appriselLetterDialog();

                            } else if (responseCode.equals("2")) {
                                llApprisel.setVisibility(View.VISIBLE);


                            }

                            // covidDecCheck();


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                //Toast.makeText(LoginActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e(TAG,"Apprisel_Letter_Check_For_error: "+ error.toString());
                open();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+pref.getAccessToken());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EDashBoardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



    private void appointmentDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_appointment, null);
        dialogBuilder.setView(dialogView);
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        TextView tvFAQ = (TextView) dialogView.findViewById(R.id.tvFAQ);
        tvFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFAQBrowser();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al4.dismiss();
                Intent intent = new Intent(EDashBoardActivity.this, AppointmentLetterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                customType(EDashBoardActivity.this, "left-to-right");

            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al4.dismiss();
            }
        });


        al4 = dialogBuilder.create();
        al4.setCancelable(true);
        Window window = al4.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al4.show();


    }

    private void appriselLetterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_appointment, null);
        dialogBuilder.setView(dialogView);
        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        TextView tvFAQ = (TextView) dialogView.findViewById(R.id.tvFAQ);
        tvFAQ.setVisibility(View.GONE);
        tvText.setText("Your Appraisal Letter has been Generated");
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setText("View and Accept");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al6.dismiss();
                if (jrCode.equals("Jr.")) {
                    apprisalPdf();
                } else {
                    apprisalPdfSnr();
                }


            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al6.dismiss();
            }
        });


        al6 = dialogBuilder.create();
        al6.setCancelable(true);
        Window window = al6.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al6.show();


    }

    private void covidDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_appointment, null);
        dialogBuilder.setView(dialogView);
        TextView tvText = (TextView) dialogView.findViewById(R.id.tvText);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        tvTitle.setText("Covid Declaration Alert");
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        Button btnSkip = (Button) dialogView.findViewById(R.id.btnSkip);
        LinearLayout llSkip = (LinearLayout) dialogView.findViewById(R.id.llSkip);
        if (covidCode.equals("0")) {
            tvText.setText("As per Company Policy and Current Situation, please fill up the Covid Declaration Form.\nIt is Mandatory.");
            btnOk.setText("Fill Up Now");
            if (skipCode.equals("1")) {
                llSkip.setVisibility(View.VISIBLE);
            } else {
                llSkip.setVisibility(View.GONE);
            }
        } else {
            tvText.setText("As per Our Record, either You or one of Your Family Member(s) have Completed 17 days, so You need to Upload Negative Report if you have done the Covid Test");
            btnOk.setText("Upload Covid Negative Report");
            llSkip.setVisibility(View.VISIBLE);
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al7.dismiss();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al7.dismiss();
                if (covidCode.equals("0")) {

                } else {
                    Intent intent = new Intent(EDashBoardActivity.this, CovidNegativeReportActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setVisibility(View.GONE);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        al7 = dialogBuilder.create();
        al7.setCancelable(false);
        Window window = al7.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        al7.show();


    }


    public void saveDeviceID() {
        String surl = APi.sAddDeviceDetailsApi + "EmpID=" + pref.getSecureEmpId() + "&serverkey=" + refreshedToken + "&senderID=" + uniqueID;
        Log.d("Save_Device_ID_URL", surl);
        llLoader.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Save_Device_ID_Response", response);
                        llLoader.setVisibility(View.GONE);

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                 appointmentLetterCheck();



                                String responseCode = job1.optString("responseCode");
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

                Log.e(TAG,"Save_Device_ID_error: "+error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void pfLinkget() {
        final ProgressDialog progressDialog = new ProgressDialog(EDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String surl = APi.sGetPFManagementURLApi + "EmployeeId=" + pref.getSecureEmpId();
        Log.d("PF_Link_URL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PF_Link_Response", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("PF_Link: ", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                pfURL = job1.optString("responseData");
                                getOTP(pfURL);
                                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);*/


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

                Log.e("PF_Link_Error: ", error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void getOTP(String pfURL) {
        final ProgressDialog progressDialog = new ProgressDialog(EDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String surl = APi.sGetPFGetOTPApi + "EmpID=" + pref.getSecureEmpId();
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                Intent intent = new Intent(EDashBoardActivity.this, OTPActivity.class);
                                intent.putExtra("pfURL", pfURL);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(EDashBoardActivity.this, "OTP has been sent to your registered mobile Number", Toast.LENGTH_LONG).show();


                                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);*/


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
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public void apprisalPdf() {
        final ProgressDialog progressDialog = new ProgressDialog(EDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String surl = APi.sBulkAppLetterGetBulkLetterApi + "EmpID=" + pref.getSecureEmpId();
        Log.d("Apprisal_Pdf_URL: ", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Apprisal_Pdf_Response: "+ response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONObject jsonObject = job1.optJSONObject("responseData");
                                String path = jsonObject.optString("FILEPATH");
                                String id = jsonObject.optString("Remarks");
                                Intent intent = new Intent(EDashBoardActivity.this, AppriselLetterActivity.class);
                                intent.putExtra("path", path);
                                intent.putExtra("id", id);
                                intent.putExtra("jrCode", jrCode);
                                startActivity(intent);
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

                Log.e(TAG,"Apprisal_Pdf: "+ error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void apprisalPdfSnr() {
        final ProgressDialog progressDialog = new ProgressDialog(EDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String surl = APi.sSrAppLetterGetSrLetterApi + "EmpID=" + pref.getSecureEmpId();
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONObject jsonObject = job1.optJSONObject("responseData");
                                String path = jsonObject.optString("FILEPATH");
                                String id = jsonObject.optString("Remarks");
                                Intent intent = new Intent(EDashBoardActivity.this, AppriselLetterActivity.class);
                                intent.putExtra("path", path);
                                intent.putExtra("id", id);
                                intent.putExtra("jrCode", jrCode);
                                startActivity(intent);


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
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void apprisalPdfForView() {
        final ProgressDialog progressDialog = new ProgressDialog(EDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String surl = APi.sBulkAppLetterGetBulkLetterApi + "EmpID=" + pref.getSecureEmpId();
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONObject jsonObject = job1.optJSONObject("responseData");
                                String path = jsonObject.optString("FILEPATH");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                                startActivity(browserIntent);


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

                Log.e(TAG,"Apprisal_Pdf_For_View_error: "+ error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void apprisalPdfForViewSnr() {
        final ProgressDialog progressDialog = new ProgressDialog(EDashBoardActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        String surl = APi.sSrAppLetterGetSrLetterApi + "EmpID=" + pref.getSecureEmpId();
        Log.d("inputDeviceURL", surl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseLogin", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                JSONObject jsonObject = job1.optJSONObject("responseData");
                                String path = jsonObject.optString("FILEPATH");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                                startActivity(browserIntent);


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

                Log.e(TAG,"Apprisal_Pdf_For_View_Snr_error"+ error.toString());
                open();
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
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Something Went Wrong.");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(EDashBoardActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




    private void sendNotification(String messageBody) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
            notificationBuilder.setColor(getResources().getColor(R.color.color1));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        }

        notificationBuilder.setContentTitle("Genius Consultant");
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void bulkApparisel() {
        appriselLetterCheck();
    }

    private void apparisel() {
        appriselLetterCheckForSnr();
    }

    private void getToolTipsShowingStatus() {


        toolTipsCode = "0";


        if (toolTipsCode.equalsIgnoreCase("1")) {
            noticeAlert();
        } else {
            // noticeAlert();
        }


    }


    private void OpenFAQBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://111.93.182.170/GeniusHR/Content/FAQs.pdf"));
        startActivity(browserIntent);
    }




    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d("RadiusValue", " KM " + kmInDec
                + " Meter " + meterInDec);
        String distance = String.format("%.3f", valueResult);
        final double ddis = Double.parseDouble(distance);
        Log.d("distance", String.valueOf(ddis));
        final Handler handler = new Handler();

        // Toast.makeText(getApplicationContext(),distance+"KM",Toast.LENGTH_LONG).show();
        return ddis;
    }


    private void getLoginTime() {
        ProgressDialog pd=new ProgressDialog(EDashBoardActivity.this);
        pd.setMessage("Loading");
        pd.show();
        pd.setCancelable(false);

        String surl = APi.sGetOfflineDailyLogActivityApi+"AEMEmployeeID=" + pref.getSecureEmpId() + "&Year=0&Month=0&AttendanceDate=" + cuDate + "&Operation=1";
        Log.d("inputactivity", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();

                        // attendabceInfiList.clear();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");

                            if (responseStatus) {
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData = job1.optJSONArray("responseData");


                                JSONObject obj = responseData.getJSONObject(0);

                                String time = obj.optString("PunchInTime");
                                tvLoginTime.setText("Your login time is "+time);



                            } else {
                                tvLoginTime.setText("");


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               pd.dismiss();


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
        RequestQueue requestQueue = Volley.newRequestQueue(EDashBoardActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoginTime();
    }
}
