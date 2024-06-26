package com.genius.employee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Handler;
/*import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;*/
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.employee.R;
import com.genius.employee.activity.DailyInTimeActivity;
import com.genius.employee.activity.MapReportActivity;
import com.genius.employee.activity.NumberVisitActivity;
import com.genius.employee.activity.TrackingReportActivity;
import com.genius.employee.model.TrackingDetailsModel;
import com.genius.employee.utility.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TrackMapListAdapter extends RecyclerView.Adapter<TrackMapListAdapter.MyViewHolder> {
    ArrayList<TrackingDetailsModel> trackingList = new ArrayList();
    Context context;
    Date date1,date2;
    GoogleMap map;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);


    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    double eLat,eLon;



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_tracklist,viewGroup,false);
        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        double sLat = Double.parseDouble(trackingList.get(i).getEntryMarkLat());
        double sLon = Double.parseDouble(trackingList.get(i).getEntryMarkLon());
        if (!trackingList.get(i).getOutMarkLat().equals("")) {
            eLat = Double.parseDouble(trackingList.get(i).getOutMarkLat());
        }else {
            eLat=00.00;
        }
        if (!trackingList.get(i).getOutMarkLon().equals("")) {
            eLon = Double.parseDouble(trackingList.get(i).getOutMarkLon());
        }else {
            eLon=00.00;
        }



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String menu = trackingList.get(i).getRegisteredOn().replaceAll("  ","-").replaceAll(" ","-");
        String[] separated = menu.split("-");
        String s1 = separated[0];
        String s2 = separated[1];
        String s3=separated[2];
        String s4=separated[3];
        String s=s4;
        Log.d("s4",s4);


        String menu1 = trackingList.get(i).getEnteredOn().replaceAll("  ","-").replaceAll(" ","-");
        String[] separated1 = menu1.split("-");
        String a = separated1[0];
        String b = separated1[1];
        String c=separated1[2];
        String f=separated1[3];
        String ss=f;
        Log.d("s4",ss);

        try {
            date1 = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            date2 = simpleDateFormat.parse(ss);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        hours = (hours < 0 ? -hours : hours);
        Log.i("iiii", " :: " + min);

        String calculateTime= (hours+"hr"+min+"min");

        myViewHolder.tvTime.setText(calculateTime);

        String date=((TrackingReportActivity) context).timeget(i);
        myViewHolder.tvcurrentDate.setText(date);
        LatLng strt=new LatLng(sLat,sLon);
        LatLng end=new LatLng(eLat,eLon);
        String d=CalculationByDistance(strt,end);
        myViewHolder.tvDistance.setText(d);

//        myViewHolder.mapView.getMapAsync((OnMapReadyCallback) this);

        GoogleMap thisMap = myViewHolder.mapCurrent;
        if (thisMap != null)
            thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.572645, 88.363892), 22));

        if (myViewHolder == null) {
            return;
        }
        myViewHolder.bindView(i);

    }







    public  static String CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        String distance ="0";
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
        //  Toast.makeText(getApplicationContext(),"Radious Value:  "+valueResult+"  KM: "+kmInDec+" Meter: "+meterInDec,Toast.LENGTH_LONG).show();
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        distance=kmInDec+"KM"+meterInDec+"meter";
        return distance;
    }


    @Override
    public void onViewRecycled(@NonNull MyViewHolder myViewHolder) {


        // Cleanup MapView here?
        if (myViewHolder.mapCurrent != null)
        {
            myViewHolder.mapCurrent.clear();
            myViewHolder.mapCurrent.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

    }




    @Override
    public int getItemCount() {
        return trackingList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        TextView tvDistance, tvcurrentDate, tvTime;
        MapView mapView;

        View layout;
        GoogleMap mapCurrent;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDistance = (TextView)itemView.findViewById(R.id.tvDistance);
            tvcurrentDate = (TextView)itemView.findViewById(R.id.tvcurrentDate);
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
            mapView = (MapView)itemView.findViewById(R.id.mapView);

            if (mapView != null)
            {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }

        }






        @Override
        public void onMapReady(GoogleMap googleMap) {

            MapsInitializer.initialize(context.getApplicationContext());
            mapCurrent = googleMap;
            setMapLocation();






        }

        private void setMapLocation() {
            if (mapCurrent == null) return;

            TrackingDetailsModel data = (TrackingDetailsModel) mapView.getTag();
            if (data == null) return;

           final LatLng d = new LatLng(Double.parseDouble(data.getEntryMarkLat()),Double.parseDouble(data.getEntryMarkLon()));
            final LatLng e = new LatLng(Double.parseDouble(data.getOutMarkLat()),Double.parseDouble(data.getOutMarkLon()));

            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String url2 = getDirectionsUrl(d, e);
                    DownloadTask downloadTask2 = new DownloadTask();
                    Log.d("sturl", url2);

                    // Start downloading json data from Google Directions API
                    downloadTask2.execute(url2);

                }
            }, 100);

            String enteryaddress=getCompleteAddressString(Double.parseDouble(data.getEntryMarkLat()),Double.parseDouble(data.getEntryMarkLon()),context);
            createMarker(Double.parseDouble(data.getEntryMarkLat()),Double.parseDouble(data.getEntryMarkLon()),enteryaddress,mapCurrent);
           createMarker1(Double.parseDouble(data.getOutMarkLat()),Double.parseDouble(data.getOutMarkLon()),data.getOutMarkAddress(),mapCurrent);
            // Set the map type back to normal.
            mapCurrent.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }



        private void bindView(int pos) {
            TrackingDetailsModel item = trackingList.get(pos);
            // Store a reference of the ViewHolder object in the layout.

            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
            mapView.setTag(item);
            setMapLocation();

        }
    }


    public TrackMapListAdapter(ArrayList<TrackingDetailsModel> trackingList, Context context) {
        this.trackingList = trackingList;
        this.context = context;

    }




    protected Marker createMarker(double latitude, double longitude, String title,GoogleMap googleMap) {

        CameraPosition cameraPosition3 = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(12)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        Log.d("markerset", "okk");

        return googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.sourceicon)));

    }

    protected Marker createMarker1(double latitude, double longitude, String title,GoogleMap googleMap) {

        CameraPosition cameraPosition3 = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                .zoom(12)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition3));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
        Log.d("markerset", "okk");

        return googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));

    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";
        String key = "key=AIzaSyCB0j4JDAEYdWakIncYaOrIKsN6Ql7Nmbc";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d("urlprint", url);

        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            //Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  setUpProgressDialog();

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {


            try {

                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = new PolylineOptions();


                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    //lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);


                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    List<PatternItem> pattern = Arrays.asList(DOT, GAP, DOT, GAP);
                    lineOptions.pattern(pattern);
                    lineOptions.width(10);

                    lineOptions.color(Color.parseColor("#033301"));


                }
                //  textView.setText("Distance:"+distance + ", Duration:"+duration);
                // Drawing polyline in the Google Map for the i-th route
                map.addPolyline(lineOptions);
                //  progressDialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE,Context dcontext) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(dcontext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrent", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrent", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrent", "Canont get Address!");
        }
        return strAdd;
    }





}
