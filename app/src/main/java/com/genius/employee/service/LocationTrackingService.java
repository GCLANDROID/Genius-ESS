package com.genius.employee.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.genius.employee.R;
import com.genius.employee.activity.EDashBoardActivity;
import com.genius.employee.receiver.TrackingAlarm;
import com.genius.employee.utility.Constants;
import com.genius.employee.utility.CustomTimerTask;
import com.genius.employee.utility.Pref;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LocationTrackingService extends Service {
    private static final int ALARM_REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private LocationRequest locationRequest;
    Pref pref;
    float radiusValue;
    String addrrd;
    String lattitude, longitude;
    TrackingAlarm alarm = new TrackingAlarm();
    Timer timer = new Timer();
    TimerTask updateProfile = new CustomTimerTask(LocationTrackingService.this);




    @Override
    public void onCreate() {
        super.onCreate();
        pref = new Pref(this);


        //  initFirebaseDatabase();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();





    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        alarm.setAlarm(this);
        showNotification();

        new Timer().scheduleAtFixedRate(new TimerTask() {

            private Handler handler = new Handler(){
                @Override
                public void dispatchMessage(Message msg) {
                    startLocaionUpdate();
                    Log.d("arpan_D","1");
                }
            };
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },0,1000);
       // timer.scheduleAtFixedRate(updateProfile, 0, 1000);
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarm.cancelAlarm(this);
        stopSelf();
        Log.d("arpan_D","2");
        stopForeground(true);

    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, EDashBoardActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(this, "channel-05", "Channel Name");
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)

        }
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Updating your location")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        startForeground(1,
                notification);
        track();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(Context context, String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ;
        service.createNotificationChannel(chan);
        return channelId;
    }

    protected LocationRequest createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * 14 * 60);
        locationRequest.setFastestInterval(1000 * 14 * 60);
        //  locationRequest.setSmallestDisplacement(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void track(){

    }



    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("Farman-Accuray", String.valueOf(location.getAccuracy()));
                    // if (location.getAccuracy() < 18 && location.getAccuracy() > 0) {


                    Intent intent = new Intent();
                    intent.setAction("com.example.foody.LOCATION_UPDATE_INTENT");
                    intent.putExtra(Constants.ACTION.LATITUDE, location.getLatitude());
                    intent.putExtra(Constants.ACTION.LONGITUDE, location.getLongitude());
                    sendBroadcast(intent);
                    //  }
                }
                Location location = locationResult.getLastLocation();
                Intent intent = new Intent();
                intent.setAction("com.rogagocorp.yatnow.LOCATION_UPDATE_INTENT");
                intent.putExtra(Constants.ACTION.LATITUDE, location.getLatitude());
                intent.putExtra(Constants.ACTION.LONGITUDE, location.getLongitude());
                sendBroadcast(intent);
                lattitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                track();


            }
        };
    }


    private void startLocaionUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);


    }















}
