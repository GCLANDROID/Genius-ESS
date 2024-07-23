package com.genius.employee.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.CreativePermission;
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
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import static maes.tech.intentanim.CustomIntent.customType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;


public class SplashScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PERMISSION_ALL = 100;
    private CreativePermission myPermission;
    GoogleApiClient googleApiClient;
    private NetworkConnectionCheck connectionCheck;
    boolean responseStatus;
    String playversion, version;
    AlertDialog alertDialog, al1, al2;

    Pref pref;
    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 124;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialize();
        CheckPermission();
    }

    private void showSplash() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( pref.getLogOutFlag().equals("1")) {
                    pref.saveIntentFlag("dashboard");
                    pref.saveVersionHitFlag("2");
                    pref.saveLoginFlag("2");
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    customType(SplashScreenActivity.this, "left-to-right");
                    finish();
                } else {
                    elsepart();
                }
            }
        }, 3000);

    }


    private void CheckPermission() {
        if (!myPermission.hasPermissions()) {
            myPermission.reqPermisions();
        } else {
            setup();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL) {
            setup();
        } else {
            finish();
        }
    }


    private void initialize() {
        connectionCheck = new NetworkConnectionCheck(this);
        pref = new Pref(getApplicationContext());
        myPermission = new CreativePermission(this, PERMISSION_ALL);
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
    }

    private void setup() {
        if (connectionCheck.isNetworkAvailable()) {
            if (connectionCheck.isGPSEnabled()) {
                showSplash();
            } else {
                turnGPSOn();
            }
        } else {
            connectionCheck.getNetworkActiveAlert().show();
        }
    }

    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(SplashScreenActivity.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            showSplash();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                try {
                                    status.startResolutionForResult(SplashScreenActivity.this, 1000);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                            } catch (Exception e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            finish();

                            break;
                        case LocationSettingsStatusCodes.ERROR:
                            finish();
                            break;
                    }
                }
            });
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                showSplash();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void checkBersion() {
        String surl = APi.sGet_apkversionApi;
        Log.d("version", surl);
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
                            // boolean _status = job1.getBoolean("status")
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SplashScreenActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SplashScreenActivity.this);
        requestQueue.add(stringRequest);

    }


    private void elsepart() {
        pref.saveIntentFlag("login");
        startActivity(new Intent(SplashScreenActivity.this, EDashBoardActivity.class));
        customType(SplashScreenActivity.this, "left-to-right");
        finish();
    }
}



