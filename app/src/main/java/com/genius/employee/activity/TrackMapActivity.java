package com.genius.employee.activity;

import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
import android.util.Log;


import androidx.fragment.app.FragmentActivity;

import com.genius.employee.R;
import com.genius.employee.utility.Pref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.HashMap;

public class TrackMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private HashMap<String, Marker> mMarkers = new HashMap();
    private GoogleMap mMap;
    String trackid;
    Pref pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_map);
        initialize();
    }

    private void initialize(){
        pref=new Pref(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //trackid=getIntent().getStringExtra("trackId")+"-"+pref.getSecurityCode();
        trackid=getIntent().getStringExtra("trackid");
        Log.d("trackerid",trackid);
        subscribeToUpdates();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        mMap.setMaxZoomPreference(20);

    }
    private void subscribeToUpdates() {




    }

}
