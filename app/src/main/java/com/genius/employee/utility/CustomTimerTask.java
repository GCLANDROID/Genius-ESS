package com.genius.employee.utility;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
//import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.genius.employee.service.LocationTrackingService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import java.util.Timer;
import java.util.TimerTask;

public class CustomTimerTask  extends TimerTask {
    private Context context;
    private Handler mHandler = new Handler();
    Pref pref;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    public CustomTimerTask(Context con) {
        this.context = con;
    }



    @Override
    public void run() {
        new Thread(new Runnable() {

            public void run() {

                mHandler.post(new Runnable() {
                    public void run() {
                        Log.d("PRINT","0000");
                        track();
                        Intent startIntent = new Intent(context, LocationTrackingService.class);
                        //startIntent.setAction(Constants.ACTION.START_ACTION);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                           context. startForegroundService(startIntent);
                        } else {
                            context.startService(startIntent);
                        }

                    }
                });
            }
        }).start();

    }

    private void track(){
        pref=new Pref(context);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        final String path = pref.getEmpId() + "/" + pref.getEmpName();
        int permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase

        }
    }
}
