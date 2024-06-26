package com.genius.employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.genius.employee.R;
import com.genius.employee.utility.APi;
import com.genius.employee.utility.GPSTracker;
import com.genius.employee.utility.Pref;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class QRCodeScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    Pref pref;
    int qrflag;
    double qrlat,qrlong;
    String qrLAT,qrLONG;
    GPSTracker gps;
    double currentlaat,currentlong;
    String ownLat,ownLong;
    String address="--";
    AlertDialog alerDialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        initView();
    }

    private void initView(){
        gps = new GPSTracker(QRCodeScannerActivity.this);
        if (gps.canGetLocation()) {
            currentlaat = gps.getLatitude();
            currentlong = gps.getLongitude();
            ownLat= String.valueOf(currentlaat);
            ownLong= String.valueOf(currentlong);
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }
        address=getCompleteAddressString(currentlaat,currentlong);
        qrflag=getIntent().getIntExtra("qrflag",0);
        pref=new Pref(QRCodeScannerActivity.this);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (qrflag==1){
                            String coordinates=result.getText();
                            String[] separated = coordinates.split(",");
                            qrlat= Double.parseDouble(separated[0]);
                            qrlong= Double.parseDouble(separated[1]);
                            qrLAT= String.valueOf(qrlat);
                            qrLONG= String.valueOf(qrlong);
                            LatLng p = new LatLng(qrlat, qrlong);
                            LatLng q=new LatLng(currentlaat,currentlong);
                            Double distance=CalculationByDistance(p,q)*1000;
                            if (distance<5||distance==5){
                                attendance();

                            }else {
                                Toast.makeText(QRCodeScannerActivity.this, "Sorry! You are out of location", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Log.d("qrlogin",result.getText());
                            postQRCodeData(result.getText());
                        }

                       // Toast.makeText(QRCodeScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void postQRCodeData(String text) {
        final ProgressDialog progressDialog = new ProgressDialog(QRCodeScannerActivity.this);
        progressDialog.setMessage("Login...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.upload(APi.sQRLoginApi)
                .addMultipartParameter("EmployeeID", pref.getSecureEmpId())
                .addMultipartParameter("KeyText", text)
                .addHeaders("Authorization","Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        String ResponseCode = job1.optString("responseCode");



                            Intent intent = new Intent(QRCodeScannerActivity.this, EDashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                           // Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();




                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        Log.d("errort", error.toString());


                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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


    private void attendance() {
        ProgressDialog pd=new ProgressDialog(QRCodeScannerActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.upload(APi.sUrl+"DailyLogTatGY/QRDailyLog")
                .addMultipartParameter("Longitude", ownLong)
                .addMultipartParameter("Latitude", ownLat)
                .addMultipartParameter("Address", address)
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pd.dismiss();
                        JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        String responseText=job.optString("responseText");
                        if (responseStatus) {
                            successAlert();
                        } else {
                            onBackPressed();
                            Toast.makeText(QRCodeScannerActivity.this,responseText,Toast.LENGTH_LONG).show();


                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        Toast.makeText(QRCodeScannerActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QRCodeScannerActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                onBackPressed();

            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your Attendance saved successfully");


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


}